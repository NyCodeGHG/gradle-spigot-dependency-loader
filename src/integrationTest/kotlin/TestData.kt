val testPluginYml = """
            |name: "ExamplePlugin"
            |version: "1.0.0"
            |main: "org.example.plugin.ExamplePlugin"
            |commands:
            |  time:
            |    description: "Displays the current time"
            |    usage: "/time"
        """.trimMargin()

val testGeneratedPluginYml = """
            |name: "ExamplePlugin"
            |version: "1.0.0"
            |main: "org.example.plugin.ExamplePlugin"
            |commands:
            |  "time":
            |    description: "Displays the current time"
            |    usage: "/time"
            |libraries:
            |- "com.squareup.okhttp3:okhttp:4.9.2"
            """.trimMargin()

val testGeneratedPluginYmlMPP = """
            |name: "ExamplePlugin"
            |version: "1.0.0"
            |main: "org.example.plugin.ExamplePlugin"
            |commands:
            |  "time":
            |    description: "Displays the current time"
            |    usage: "/time"
            |libraries:
            |- "org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2"
            """.trimMargin()
