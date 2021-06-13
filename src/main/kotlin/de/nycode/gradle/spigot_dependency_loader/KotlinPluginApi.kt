package de.nycode.gradle.spigot_dependency_loader

import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider

/**
 * Default task to export dependencies from [spigot] into the modules `plugin.yml` file.
 */
inline val TaskContainer.exportDependenciesToPluginYml: TaskProvider<ExportPluginDependenciesTask>
    get() = named("exportDependenciesToPluginYml", ExportPluginDependenciesTask::class.java)
