import java.util.Properties

plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.modrinth.minotaur") version "2.+"
    id("co.uzzu.dotenv.gradle") version "4.0.0"
}

group = "org.atrimilan"
version = env.PROJECT_VERSION.value

val supportedGameVersions = project.property("supportedGameVersions") as String
val changelogFile = rootProject.file("changelogs/${env.PROJECT_VERSION.value}.md")

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

// Doc: https://github.com/modrinth/minotaur
modrinth {
    token.set(env.MODRINTH_TOKEN.value)
    projectId.set(env.MODRINTH_PROJECT_ID.value)
    versionNumber.set(env.PROJECT_VERSION.value)
    uploadFile.set(tasks.jar)

    versionType.set("release")
    gameVersions.addAll(supportedGameVersions.split(","))
    loaders.addAll("paper")
    syncBodyFrom = rootProject.file("README.md").readText()

    if (changelogFile.exists()) {
        changelog.set(changelogFile.readText())
    }
}

// Uncomment if the project should support Spigot on Minecraft >=1.20.5
// paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

dependencies {
    // compileOnly("io.papermc.paper:paper-api:${project.property("paperApiVersion")}") // Included with paperweight-userdev
    paperweight.paperDevBundle(project.property("paperApiVersion") as String)
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

val serverDir = "local-server"

tasks {

    /********** Versioning **********/

    fun updateProjectVersion(major: Int, minor: Int, patch: Int) {
        val envFile = File(rootDir, ".env")
        val properties = Properties()
        properties.load(envFile.inputStream())
        properties.setProperty("PROJECT_VERSION", "$major.$minor.$patch")
        envFile.outputStream().use {
            properties.store(it, null)
        }
    }

    fun incrementVersion(majorIncrement: Int = 0, minorIncrement: Int = 0, patchIncrement: Int = 0) {
        val (major, minor, patch) = env.PROJECT_VERSION.value.split(".").map { it.toInt() }
        val newMajor = major + majorIncrement
        val newMinor = if (majorIncrement > 0) 0 else minor + minorIncrement // Reset if major, otherwise increment
        val newPatch = if (majorIncrement > 0 || minorIncrement > 0) 0 else patch + patchIncrement // Reset if major or minor, otherwise increment
        updateProjectVersion(newMajor, newMinor, newPatch)
    }

    register("incrementMajorVersion") { // X.0.0
        group = "2- versioning"
        doLast {
            incrementVersion(majorIncrement = 1)
        }
    }
    register("incrementMinorVersion") { // 0.X.0
        group = "2- versioning"
        doLast {
            incrementVersion(minorIncrement = 1)
        }
    }
    register("incrementPatchVersion") { // 0.0.X
        group = "2- versioning"
        doLast {
            incrementVersion(patchIncrement = 1)
        }
    }

    /********** Publishing **********/

    named("modrinth") {
        group = "3- publishing"
        dependsOn("modrinthSyncBody")
    }

    /********** Build plugin and run a local server **********/

    register("buildPluginAndRunServer") {
        group = "1- local server"
        description = "Build the plugin's JAR file and run a Paper test server that includes it"

        dependsOn("jar") // Build plugin JAR

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
        group = "1- local server"
        runDirectory.set(file(serverDir))
        // minecraftVersion("1.21.4") // Automatically set by paperweight-userdev

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