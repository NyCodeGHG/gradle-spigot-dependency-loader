package de.nycode.gradle.spigot_dependency_loader

import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider

/**
 * Default task to export dependencies from spigot dependency configuration into the modules `plugin.yml` file.
 *
 * This task will export your plugin.yml directly into the build folder
 * after resourceProcessing (processResources task) to not interfere with anything.
 * The dependencies won't show up in the origin source
 *
 * @see SpigotDependencyLoaderExtension.createDefaultTask
 */
inline val TaskContainer.exportDependenciesToPluginYml: TaskProvider<ExportPluginDependenciesTask>
    get() = named("exportDependenciesToPluginYml", ExportPluginDependenciesTask::class.java)
