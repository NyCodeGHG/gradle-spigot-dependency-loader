plugins {
    id("com.gradle.plugin-publish") version "0.16.0"
    `java-gradle-plugin`
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
}

version = "1.1.1"

repositories {
    mavenCentral()
}

val integrationTest: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
}

val integrationTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

dependencies {
    implementation("com.charleskorn.kaml", "kaml", "0.36.0")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.3.0")
    integrationTestImplementation(kotlin("test-junit5"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        vendor.set(JvmVendorSpec.ADOPTOPENJDK)
    }
}

gradlePlugin {
    plugins {
        create("gradle-spigot-dependency-loader") {
            id = "de.nycode.spigot-dependency-loader"
            implementationClass = "de.nycode.gradle.spigot_dependency_loader.GradleSpigotDependencyLoaderPlugin"
        }
    }
    testSourceSets(integrationTest)
}

val integrationTestTask = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = integrationTest.output.classesDirs
    classpath = integrationTest.runtimeClasspath
    shouldRunAfter("test")
}

tasks {
    check {
        dependsOn(integrationTestTask)
    }
    withType<Test> {
        useJUnitPlatform()
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
