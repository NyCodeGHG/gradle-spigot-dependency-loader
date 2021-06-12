plugins {
    kotlin("jvm") version "1.4.32"
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
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
}
