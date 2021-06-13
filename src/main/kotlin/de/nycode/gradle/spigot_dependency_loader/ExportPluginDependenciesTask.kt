package de.nycode.gradle.spigot_dependency_loader

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import de.nycode.gradle.spigot_dependency_loader.spigot.PluginYml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.language.jvm.tasks.ProcessResources
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

// https://regex101.com/r/fsKymw/1
private var dependencyPattern = """(\S*):(\S*):(\S*)""".toRegex()
private var yaml = Yaml(configuration = YamlConfiguration(encodeDefaults = false))

@Suppress("MemberVisibilityCanBePrivate")
abstract class ExportPluginDependenciesTask : ProcessResources() {

    /**
     * The [Path] of the `plugin.yml` file of this project
     */
    @get:InputFile
    @get:Optional
    abstract val pluginYml: Property<Path>

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
    abstract val fileCharset: Property<Charset> //= Charsets.UTF_8


    private val sourceSets: SourceSetContainer
        get() = project.extensions.getByName("sourceSets") as SourceSetContainer

    fun pluginYml(file: File): Unit = pluginYml.set(file.toPath())

    @TaskAction
    fun exportDependencies() {
        val file =
            pluginYml.getOrElse((sourceSets.getByName("main").resources.files.firstOrNull { it.name == "plugin.yml" }
                ?: File(
                    "src/main/resources/plugin.yaml"
                )).toPath())
        val extraDependencies = this.extraDependencies.getOrElse(mutableListOf())
        require(Files.exists(file)) { "plugin.yml file does not exist please specify a file path to an existing plugin.yml" }

        require(extraDependencies.all {
            it.matches(dependencyPattern)
        }) { """Extra dependencies must be in("group", "name", "version") format""" }


        val charset = fileCharset.getOrElse(Charsets.UTF_8)
        val fileContent = Files.readAllBytes(file).toString(charset)
        val pluginYml = yaml.decodeFromString<PluginYml>(fileContent)

        val rawDependencies =
            configurations.get() + listOf(project.configurations.getByName("spigot"))
        val dependencies = rawDependencies.flatMap {
            it.dependencies
        }.map {
            try {
                requireNotNull(it.group) { "Runtime dependencies must specify a groupId" }
                requireNotNull(it.name) { "Runtime dependencies must specify a artifactId" }
                requireNotNull(it.version) { "Runtime dependencies must specify a version" }
            } catch (e: NullPointerException) {
                throw IllegalArgumentException(
                    "Artifact ${it.notation} did not qualify for runtime dependency management",
                    e
                )
            }

            it.notation
        } + extraDependencies

        val newPluginYml = pluginYml.copy(libraries = dependencies)
        val newPluginYmlExport = yaml.encodeToString(newPluginYml)

        val output = project.tasks.getByPath("processResources").outputs.files.first().toPath()
        Files.write(output, newPluginYmlExport.toByteArray(charset))
    }
}

private val Dependency.notation: String
    get() = "${group}:${name}:${version}"
