plugins {
    java
    // version gets ignored here because we include the plugin build in the example build for local testing
    // normal users need to specify the version from the Gradle plugin portal
    id("de.nycode.spigot-dependency-loader") /* version "1.0.1" */
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    compileOnly("org.spigotmc", "spigot-api", "1.17-R0.1-SNAPSHOT")

    // the plugin adds a spigot dependency configuration, which will automatically add
    // all it's dependencies to the plugin.yml
    // compileOnly will automatically inherit from spigot so all spigot dependencies are visible to the compiler and
    // your IDE
    // Also only mavenCentral() dependencies should be added here
    spigot("com.squareup.okhttp3", "okhttp", "4.9.0")
}

tasks {
    processResources {
        from(sourceSets["main"].resources) {
            val tokens = mapOf("version" to version)
            filter(org.apache.tools.ant.filters.ReplaceTokens::class, mapOf("tokens" to tokens))

            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }
}

spigotDependencyLoader {
    // This configures the "exportDependenciesToPluginYml" tasks
    createDefaultTask.set(true) // this defaults to true
}
