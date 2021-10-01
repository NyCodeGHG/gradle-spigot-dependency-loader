package de.nycode.gradle.spigot_dependency_loader

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import de.nycode.gradle.spigot_dependency_loader.spigot.PluginYml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.div
import kotlin.io.path.name

// https://regex101.com/r/fsKymw/1
private var dependencyPattern = """(\S*):(\S*):(\S*)""".toRegex()
private var yaml = Yaml(configuration = YamlConfiguration(encodeDefaults = false))

@Suppress("MemberVisibilityCanBePrivate")
abstract class ExportPluginDependenciesTask : DefaultTask() {

    /**
     * The [Path] of the `plugin.yml` file of this project.
     */
    @get:InputFile
    @get:Optional
    abstract val pluginYml: Property<Path>

    /**
     * The [Path] to export the `plugin.yml` file to.
     */
    @get:InputDirectory
    @get:Optional
    abstract val outputDirectory: Property<Path>

    /**
     * A list of [Configurations][Configuration] that contain dependencies to export.
     */
    @get:Input
    @get:Optional
    abstract val configurations: ListProperty<Configuration>

    /**
     * A list of extra dependencies to export as well.
     */
    @get:Input
    @get:Optional
    abstract val extraDependencies: ListProperty<String>

    /**
     * The [Charset] for of the [pluginYml] file.
     */
    @get:Input
    @get:Optional
    abstract val fileCharset: Property<Charset>

    fun pluginYml(file: File): Unit = pluginYml.set(file.toPath())

    fun dependency(notation: String) = extraDependencies.add(notation)
    fun dependency(group: String, name: String, version: String) = dependency("$group:$name:$version")

    @OptIn(ExperimentalPathApi::class)
    @TaskAction
    fun exportDependencies() {
        val file = pluginYml.orNull ?: error("Please specify a plugin.yml location")
        val outputPath = outputDirectory.orNull ?: error("Please specify an output directly")
        val extraDependencies = this.extraDependencies.getOrElse(mutableListOf())
        require(Files.exists(file)) { "plugin.yml file does not exist please specify a file path to an existing plugin.yml" }

        require(
            extraDependencies.all {
                it.matches(dependencyPattern)
            }
        ) { """Extra dependencies must be in("group", "name", "version") format""" }

        val charset = fileCharset.getOrElse(Charsets.UTF_8)
        val fileContent = Files.readAllBytes(file).toString(charset)
        val pluginYml = yaml.decodeFromString<PluginYml>(fileContent)

        val rawDependencies =
            configurations.get() + listOf(project.configurations.getByName("spigot"))
        val dependencies = rawDependencies.flatMap { configuration ->
            configuration.resolvedConfiguration.firstLevelModuleDependencies
                .filter { it.moduleArtifacts.isNotEmpty() }
                .map { it.module.toString() }
        } + extraDependencies

        val newPluginYml = pluginYml.copy(libraries = dependencies)
        val newPluginYmlExport = yaml.encodeToString(newPluginYml)

        Files.write(outputPath / file.name, newPluginYmlExport.toByteArray(charset))
        didWork = true
    }
}
