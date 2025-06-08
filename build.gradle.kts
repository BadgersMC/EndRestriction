plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
}

fun getProperty(key: String): String {
    return project.property(key) as String
}

group = "net.lumalyte"
version = getProperty("projectVersion")
base.archivesName.set("end-restriction")

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "placeholderapi"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    paperweight.paperDevBundle(getProperty("paperApiVersion"))
    compileOnly("me.clip:placeholderapi:2.11.5")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}