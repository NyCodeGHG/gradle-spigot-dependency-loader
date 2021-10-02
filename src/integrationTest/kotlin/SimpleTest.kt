import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.Test

class SimpleTest {

    @Test
    fun `test simple pluginyml generation`(@TempDir path: Path) = runTest(path) {
        buildFileContent = """
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
            |    spigot("com.squareup.okhttp3", "okhttp", "4.9.2")
            |}
        """.trimMargin()

        pluginYmlContent = testPluginYml
        expectedPluginYmlContent = testGeneratedPluginYml
        gradleArgs = "exportDependenciesToPluginYml"
    }
}
