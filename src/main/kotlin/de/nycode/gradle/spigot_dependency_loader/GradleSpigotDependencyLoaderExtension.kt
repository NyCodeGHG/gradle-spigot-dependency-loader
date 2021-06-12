package de.nycode.gradle.spigot_dependency_loader

import org.gradle.api.provider.Property

abstract class GradleSpigotDependencyLoaderExtension {
    abstract val message: Property<String>

    init {
        message.convention("Hello world")
    }
}
