plugins {
    id("com.gradle.plugin-publish") version "0.15.0"
    `java-gradle-plugin`
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
}

version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.charleskorn.kaml", "kaml", "0.34.0")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.2.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

gradlePlugin {
    plugins {
        create("gradle-spigot-dependency-loader") {
            id = "de.nycode.spigot-dependency-loader"
            implementationClass = "de.nycode.gradle.spigot_dependency_loader.GradleSpigotDependencyLoaderPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/NyCodeGHG/gradle-spigot-dependency-loader"
    vcsUrl = "https://github.com/NyCodeGHG/gradle-spigot-dependency-loader"

    description = "Automatically download your dependencies at runtime on Spigot 1.16.5+"

    (plugins) {
        "gradle-spigot-dependency-loader" {
            displayName = "Spigot Dependency Loader"
            description = this@pluginBundle.description
            tags = listOf("spigot", "gradle", "dependency-management", "runtime-dependency-management")
            version = project.version.toString()
        }
    }

    mavenCoordinates {
        groupId = "de.nycode"
        artifactId = "spigot-dependency-loader"
        version = project.version.toString()
    }
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        }
    }
}
