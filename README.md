# gradle-spigot-dependency loader

[![Gradle Plugin Release](https://img.shields.io/badge/gradle%20plugin-1.0-blue.svg)](https://plugins.gradle.org/plugin/de.nycode.spigot-dependency-loaderj) [![GitHub Release](https://img.shields.io/github/release/jetbrains/gradle-intellij-plugin.svg)](https://github.com/NyCodeGHG/gradle-spigot-dependency-loaderreleases)

A simple Gradle plugin, to use the new MC 1.16.5+ runtime dependency loader to load your dependencies at runtime

## How to use

- Add the plugin

<details open>
  <summary>Kotlin DSL</summary>

```kotlin
plugins {
    id("de.nycode.spigot-dependency-loader") version "1.0"
}
```

</details>

<details>
  <summary>Groovy DSL</summary>

```groovy
plugins {
    id "de.nycode.spigot-dependency-loader" version "1.0"
}
```

</details>

- Call the `exportDependenciesToPluginYml` Task when building

## Features

- Load your Spigot dependencies at runtime

# The exportDependenciesToPluginYml task

By default, the `exportDependenciesToPluginYml` task gets created which sources the plugin.yml from
the `processResources` output, or the main resources source set and exports it directly into the build output to not
interfere with any YAML guidelines your project might have and poison your source with generated code. If you want to
disable the default task you can create your own like this. The tasks get automatically added as a dependency
on `classes` and depends on `processResources`

```kotlin
spigotDependencyLoader {
    createDefaultTask.set(false) // this defaults to true
}

tasks {
    task<ExportPluginDependenciesTask>("exportDependenciesToPluginYml") {
        pluginYml.set(Path("<path to plugin.yml>"))
        outputDirectory.set("<path to output dir>") //file name is as specified above

        // Dependency configurations to export
        configurations.add(configurations.spigot) // default configuration

        // Extra dependencies not in the configs
        dependency("extra:dependency:1.0.0")

        // Charset to encode plugin.yml
        fileCharset.set(Charsets.UTF_8) // default
    }
}
```

## Contributing

Feel free to open an issue or submit a pull request for any bugs/improvements.

## License

This Gradle Plugin is licensed under the MIT License - see the [License](LICENSE) file for details.
