plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "org.atrimilan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${project.property("paperApiVersion")}")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

val serverDir = "local-server"

tasks {
    register("buildAndRunServer") {
        group = "run paper"
        description = "Build the plugin's JAR file and run a Paper test server that includes it"

        dependsOn("build") // Build plugin JAR

        doFirst { // Copy plugin JAR
            val jarFile = file("build/libs/${project.name}-${version}.jar")
            val pluginsDir = file("${serverDir}/plugins")
            pluginsDir.mkdirs()

            if (jarFile.exists())
                jarFile.copyTo(file("${pluginsDir}/${jarFile.name}"), overwrite = true)
            else
                throw GradleException("File ${jarFile.name} not found.")
        }

        finalizedBy("runServer") // Run a local Paper server
    }

    runServer { // https://github.com/jpenilla/run-task
        runDirectory.set(file(serverDir))

        minecraftVersion("1.21.4")
        jvmArgs(
            "-Dcom.mojang.eula.agree=true",
            "-Dserver.port=25565"
        )

        doFirst {
            // Configure server.properties
            val serverPropertiesFile = file("${serverDir}/server.properties")
            serverPropertiesFile.parentFile.mkdirs()
            serverPropertiesFile.writeText("""
                allow-nether=false
                """.trimIndent())

            // Configure bukkit.yml
            val bukkitYmlFile = file("${serverDir}/bukkit.yml")
            bukkitYmlFile.parentFile.mkdirs()
            bukkitYmlFile.writeText("""
                settings:
                  allow-end: false
                """.trimIndent())
        }
    }

    test {
        useJUnitPlatform()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}