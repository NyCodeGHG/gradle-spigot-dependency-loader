package de.nycode.gradle.spigot_dependency_loader

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class GradleSpigotDependencyLoaderTask : DefaultTask() {

    @TaskAction
    fun sampleAction() {
        logger.lifecycle("hello world")
    }

}
