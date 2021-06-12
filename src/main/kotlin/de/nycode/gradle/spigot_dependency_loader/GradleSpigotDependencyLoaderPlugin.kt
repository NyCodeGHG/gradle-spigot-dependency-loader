package de.nycode.gradle.spigot_dependency_loader

import de.nycode.gradle.spigot_dependency_loader.utils.create
import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleSpigotDependencyLoaderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension =
            project.extensions.create<GradleSpigotDependencyLoaderExtension>("spigotDependencyExtension")
    }
}
