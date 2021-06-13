package de.nycode.gradle.spigot_dependency_loader

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleSpigotDependencyLoaderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val spigotDependencies = project.configurations.create("spigot")

        project.afterEvaluate {
            val exportTask =
                project.tasks.create("exportDependenciesToPluginYml", ExportPluginDependenciesTask::class.java)
            project.tasks.named("classes") { it.dependsOn(exportTask) }

            project.configurations.getByName("compileOnly").extendsFrom(spigotDependencies)
        }
    }

}
