@file:Suppress("NAME_SHADOWING")

import org.gradle.testkit.runner.GradleRunner
import java.nio.file.Path
/* ktlint-disable no-wildcard-imports */
import kotlin.io.path.*
import kotlin.test.assertEquals

class GradleSpigotDependencyLoaderTestBuilder(
    var buildFileContent: String = "",
    var pluginYmlContent: String = "",
    var expectedPluginYmlContent: String = "",
    var gradleArgs: String = ""
)

inline fun runTest(path: Path, builder: GradleSpigotDependencyLoaderTestBuilder.() -> Unit) {
    val builder = GradleSpigotDependencyLoaderTestBuilder().apply(builder)
    val buildFile = path.resolve("build.gradle.kts").createFile()
    buildFile.writeText(builder.buildFileContent)
    val pluginYml = (path / "src" / "main" / "resources")
        .createDirectories() / "plugin.yml"
    pluginYml.createFile().writeText(builder.pluginYmlContent)
    GradleRunner.create()
        .withProjectDir(path.toFile())
        .withArguments(builder.gradleArgs)
        .withDebug(true)
        .withPluginClasspath()
        .build()
    val generatedPluginYml = (path / "build" / "resources" / "main" / "plugin.yml").readText()
    assertEquals(builder.expectedPluginYmlContent, generatedPluginYml)
}
