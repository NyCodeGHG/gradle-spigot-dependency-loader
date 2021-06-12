package de.nycode.gradle.spigot_dependency_loader.utils

import org.gradle.api.plugins.ExtensionContainer

inline fun <reified T> ExtensionContainer.create(name: String, vararg constructionArguments: Any): T =
    create(name, T::class.java, *constructionArguments)
