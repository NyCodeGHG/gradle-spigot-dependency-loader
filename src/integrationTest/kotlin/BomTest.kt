import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.test.Test

class BomTest {

    @Test
    fun `test gradle boms`(@TempDir path: Path) = runTest(path) {
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
            |    spigot(platform("com.squareup.okhttp3:okhttp-bom:4.9.2"))
            |    spigot("com.squareup.okhttp3", "okhttp")
            |}
        """.trimMargin()

        pluginYmlContent = testPluginYml
        expectedPluginYmlContent = testGeneratedPluginYml
        gradleArgs = "exportDependenciesToPluginYml"
    }
}
