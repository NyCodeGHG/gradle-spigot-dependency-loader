package de.nycode.gradle.spigot_dependency_loader

import org.gradle.api.provider.Property

abstract class SpigotDependencyLoaderExtension {
    /**
     * Whether to create the default `exportDependenciesToPluginYml` task or not.
     *
     * This task will export your plugin.yml directly into the build folder
     * after resourceProcessing (processResources task) to not interfere with anything.
     * The dependencies won't show up in the origin source
     *
     * @see exportDependenciesToPluginYml
     */
    abstract val createDefaultTask: Property<Boolean>
}
