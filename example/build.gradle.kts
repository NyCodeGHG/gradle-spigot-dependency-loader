plugins {
    java
    id("de.nycode.spigot-dependency-loader")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    compileOnly("org.spigotmc", "spigot-api", "1.17-R0.1-SNAPSHOT")
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
