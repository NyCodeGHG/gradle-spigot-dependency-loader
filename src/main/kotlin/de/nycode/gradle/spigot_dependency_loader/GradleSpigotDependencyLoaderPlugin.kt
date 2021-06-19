package de.nycode.gradle.spigot_dependency_loader

import de.nycode.gradle.spigot_dependency_loader.utils.create
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.language.jvm.tasks.ProcessResources
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.div
import kotlin.io.path.exists

class GradleSpigotDependencyLoaderPlugin : Plugin<Project> {
    @OptIn(ExperimentalPathApi::class)
    override fun apply(project: Project) {
        val extension = project.makeExtension()
        val spigotDependencies = project.makeDependencyConfig()

        if (extension.createDefaultTask.getOrElse(true)) {
            project.afterEvaluate {
                @Suppress("UnstableApiUsage")
                val processResources = project.tasks.findByPath("processResources") as ProcessResources
                val main = project.sourceSets.getByName("main")
                val exportTask =
                    project.tasks.create("exportDependenciesToPluginYml", ExportPluginDependenciesTask::class.java) {
                        with(it) {
                            dependsOn(processResources)

                            val processedYaml = project.buildDir.toPath() / "resources" / main.name / "plugin.yml"
                            val file = if (processedYaml.exists()) {
                                processedYaml
                            } else {
                                main.resources.sourceDirectories.files.firstOrNull { file -> file.name == "plugin.yaml" }
                                    ?.toPath()
                                    ?: error("Please create a plugin.yml in the main resources directory or use spigotDependencyLoader { defaultTask = false }")
                            }

                            pluginYml.set(file)
                            outputDirectory.set(processedYaml.parent)
                        }
                    }
                
                project.tasks.named("classes") { it.dependsOn(exportTask) }
                project.configurations.getByName("compileOnly").extendsFrom(spigotDependencies)
            }
        }
    }

}

private fun Project.makeExtension() =
    extensions.create<SpigotDependencyLoaderExtension>("spigotDependencyLoader")

private fun Project.makeDependencyConfig(): Configuration {
    val spigotDependencies = configurations.create("spigot")
    repositories.forEach {
        it.apply {
            content { content ->
                content.notForConfigurations(spigotDependencies.name)
            }
        }
    }
    repositories.apply {
        mavenCentral {
            it.apply {
                content { content ->
                    content.onlyForConfigurations(spigotDependencies.name)
                }
            }
        }
    }

    return spigotDependencies
}

private val Project.sourceSets: SourceSetContainer
    get() = extensions.getByType(SourceSetContainer::class.java)
