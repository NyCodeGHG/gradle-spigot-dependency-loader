import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.Test

class MPPTest {

    @Test
    fun `test kotlin mpp dependencies`(@TempDir path: Path) = runTest(path) {
        buildFileContent = """
            |plugins {
            |    kotlin("jvm") version "1.6.10"
            |    id("de.nycode.spigot-dependency-loader")
            |}
            |
            |repositories {
            |    mavenCentral()
            |}
            |
            |dependencies {
            |    spigot("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.3.2")
            |}
        """.trimMargin()

        pluginYmlContent = testPluginYml
        expectedPluginYmlContent = testGeneratedPluginYmlMPP
        gradleArgs = "exportDependenciesToPluginYml"
    }
}
