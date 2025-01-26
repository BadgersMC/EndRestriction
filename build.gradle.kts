plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "org.atrimilan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

// Uncomment if the project should support Spigot on Minecraft >=1.20.5
//paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

dependencies {
//    compileOnly("io.papermc.paper:paper-api:${project.property("paperApiVersion")}") // Included with paperweight-userdev
    paperweight.paperDevBundle(project.property("paperApiVersion") as String)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

val serverDir = "local-server"

tasks {
    register("buildAndRunServer") {
        group = "build and run"
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

        finalizedBy("runDevBundleServer") // Run a local Paper server
    }

    /**
     * run-paper from https://github.com/jpenilla/run-task
     * "runServer" when using Spigot mappings
     * "runDevBundleServer" when using Mojang mappings (paperweight-userdev)
     */
    runDevBundleServer {
        runDirectory.set(file(serverDir))

        // minecraftVersion("1.21.4") // Automatically managed by paperweight-userdev
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

    runServer { // Should not be used as paperweight-userdev has been implemented
        throw GradleException("Please use the 'runDevBundleServer' task instead.")
    }

    test {
        useJUnitPlatform()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}