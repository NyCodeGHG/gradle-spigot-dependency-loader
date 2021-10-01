import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
/* ktlint-disable no-wildcard-imports */
import kotlin.io.path.*
import kotlin.test.Test
import kotlin.test.assertEquals

class BomTest {

    @Test
    fun `test gradle boms`(@TempDir path: Path) {
        val buildFile = path.resolve("build.gradle.kts").createFile()
        //language=kts
        buildFile.writeText(
            """
            |plugins {
            |    java
            |    id("de.nycode.spigot-dependency-loader")
            |}
            |
            |repositories {
            |    mavenCentral()
            |}
            |
            |dependencies {
            |    spigot(platform("com.squareup.okhttp3:okhttp-bom:4.9.2"))
            |    spigot("com.squareup.okhttp3", "okhttp")
            |}
            |
        """.trimMargin()
        )

        val pluginYml = path.resolve("src")
            .resolve("main")
            .resolve("resources")
            .createDirectories()
            .resolve("plugin.yml")
            .createFile()

        pluginYml.writeText(
            """
            |name: "ExamplePlugin"
            |version: "1.0.0"
            |main: "org.example.plugin.ExamplePlugin"
            |commands:
            |  time:
            |    description: "Displays the current time"
            |    usage: "/time"
        """.trimMargin()
        )

        GradleRunner.create()
            .withProjectDir(path.toFile())
            .withArguments("exportDependenciesToPluginYml")
            .withDebug(true)
            .withPluginClasspath()
            .build()

        val generatedPluginYml = (path / "build" / "resources" / "main" / "plugin.yml").readText()
        assertEquals(
            """
            |name: "ExamplePlugin"
            |version: "1.0.0"
            |main: "org.example.plugin.ExamplePlugin"
            |commands:
            |  "time":
            |    description: "Displays the current time"
            |    usage: "/time"
            |libraries:
            |- "com.squareup.okhttp3:okhttp:4.9.2"
            """.trimMargin(),
            generatedPluginYml
        )
    }
}
