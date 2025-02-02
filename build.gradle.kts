plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

fun getProperty(key: String): String {
    return project.property(key) as String
}

group = "org.atrimilan"
version = getProperty("projectVersion")

val localServerDir = "local-server"
val projectVersion = getProperty("projectVersion")

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

// Uncomment if the project should support Spigot on Minecraft >=1.20.5
// paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

dependencies {
    // compileOnly("io.papermc.paper:paper-api:${getProperty("paperApiVersion")}") // Included with paperweight-userdev
    paperweight.paperDevBundle(getProperty("paperApiVersion"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks {

    /********** Versioning **********/

    fun incrementVersion(type: String) {
        val (major, minor, patch) = projectVersion.split(".").map { it.toInt() }
        val newVersion = when (type) {
            "major" -> "${major + 1}.0.0"
            "minor" -> "$major.${minor + 1}.0"
            "patch" -> "$major.$minor.${patch + 1}"
            else -> throw GradleException("Invalid version type")
        }
        file("gradle.properties").apply {
            writeText(readText().replace(Regex("projectVersion=.*"), "projectVersion=$newVersion"))
        }
    }

    register("incrementMajorVersion") {
        group = "2- versioning"
        doLast {
            incrementVersion("major")
        }
    }

    register("incrementMinorVersion") {
        group = "2- versioning"
        doLast {
            incrementVersion("minor")
        }
    }

    register("incrementPatchVersion") {
        group = "2- versioning"
        doLast {
            incrementVersion("patch")
        }
    }

    /********** Build plugin and run a local server **********/

    register("buildPluginAndRunServer") {
        group = "1- local server"
        description = "Build the plugin's JAR file and run a Paper test server that includes it"

        dependsOn("jar") // Build plugin JAR

        doFirst { // Copy plugin JAR
            val jarFile = file("build/libs/${project.name}-${version}.jar")
            val pluginsDir = file("${localServerDir}/plugins").apply { mkdirs() }

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
        group = "1- local server"
        runDirectory.set(file(localServerDir))
        // minecraftVersion("1.21.4") // Automatically set by paperweight-userdev

        jvmArgs(
            "-Dcom.mojang.eula.agree=true",
            "-Dserver.port=25565"
        )

        doFirst {
            val serverPropertiesFile = file("${localServerDir}/server.properties")
            val bukkitYmlFile = file("${localServerDir}/bukkit.yml")

            listOf(serverPropertiesFile, bukkitYmlFile).forEach {
                file -> file.parentFile.mkdirs()
            }

            serverPropertiesFile.writeText("""
                allow-nether=false
                """.trimIndent())

            bukkitYmlFile.writeText("""
                settings:
                  allow-end: false
                """.trimIndent())
        }
    }

    named("runServer") { // Should not be used as paperweight-userdev has been implemented
        throw GradleException("Please use the 'runDevBundleServer' task instead.")
    }

    test {
        useJUnitPlatform()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}