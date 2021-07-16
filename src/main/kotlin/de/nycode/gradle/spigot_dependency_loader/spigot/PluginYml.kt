package de.nycode.gradle.spigot_dependency_loader.spigot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PluginYml(
    val name: String? = null,
    val provides: List<String> = emptyList(),
    val version: String? = null,
    val main: String? = null,
    val author: String? = null,
    val authors: List<String> = emptyList(),
    val contributors: List<String> = emptyList(),
    val description: String? = null,
    val website: String? = null,
    val prefix: String? = null,
    val load: LoadOrder? = null,
    val depend: List<String> = emptyList(),
    @Suppress("SpellCheckingInspection")
    @SerialName("softdepend")
    val softDepend: List<String> = emptyList(),
    @Suppress("SpellCheckingInspection")
    @SerialName("loadbefore")
    val loadBefore: List<String> = emptyList(),
    val commands: Map<String, Command> = emptyMap(),
    val permissions: Map<String, Permission> = emptyMap(),
    @SerialName("default-permission")
    val defaultPermission: PermissionDefault? = null,
    val awareness: Set<String> = emptySet(),
    @SerialName("api-version")
    val apiVersion: String? = null,
    val libraries: List<String> = emptyList(),
) {

    @Serializable
    data class Command(
        val description: String? = null,
        val aliases: List<String> = emptyList(),
        val permission: String? = null,
        val usage: String? = null
    )

    @Serializable
    data class Permission(
        val description: String? = null,
        val children: Map<String, Boolean> = emptyMap(),
        val default: String? = null
    )

    @Serializable
    enum class LoadOrder {
        @Suppress("SpellCheckingInspection")
        @SerialName("POSTWORLD")
        POST_WORLD,
        STARTUP
    }

    @Serializable
    enum class PermissionDefault {
        FALSE,
        NOT_OP,
        OP,
        TRUE
    }
}
