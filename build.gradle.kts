plugins {
    kotlin("jvm") version "1.5.10"
    kotlin("plugin.serialization") version "1.5.10"
    `java-gradle-plugin`
}

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

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    sourceSets

    jar {

    }
}
