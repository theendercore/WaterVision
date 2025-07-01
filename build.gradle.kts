@file:Suppress("PropertyName", "VariableNaming")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.iridium)
    alias(libs.plugins.iridium.publish)
    alias(libs.plugins.iridium.upload)
}

repositories {
    maven("https://teamvoided.org/releases")
    maven("https://maven.fzzyhmstrs.me/") { name = "FzzyMaven" }
    maven("https://maven.terraformersmc.com/") { name = "Terraformers" }
    mavenCentral()
}

modSettings {
    entrypoint("client", "com.theendercore.water_vision.WaterVision")
    mixinFile("${modId()}.mixins.json")
    dependency("fzzy_config", "*")
}

dependencies {
    modImplementation(fileTree("libs"))
    modImplementation(libs.fzzy.config)

    modImplementation(libs.modmenu)
}

loom {
    runs {
        create("TestWorld") {
            client()
            ideConfigGenerated(true)
            runDir("run")
            programArgs("--quickPlaySingleplayer", "test", "--username", "Dev")
        }
    }
}

tasks {
    val targetJavaVersion = 21
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    withType<KotlinCompile> {
        compilerOptions.jvmTarget = JvmTarget.JVM_21
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(targetJavaVersion).toString()))
        withSourcesJar()
    }
}

publishScript {
    releaseRepository("TeamVoided", "https://maven.teamvoided.org/releases")
    publication(modSettings.modId(), false)
    publishSources(true)
}

uploadConfig {
//    debugMode = true
    modrinthId = "CXryw0YT"
    curseId = "890050"

    changeLog = " - switched to fzzy config"

    // FabricApi
    modrinthDependency("P7dR8mSH", uploadConfig.REQUIRED)
    curseDependency("fabric-api", uploadConfig.REQUIRED)
    // Fabric Language Kotlin
//    modrinthDependency("Ha28R6CL", uploadConfig.REQUIRED)
//    curseDependency("fabric-language-kotlin", uploadConfig.REQUIRED)
    //Fzzy
    modrinthDependency("hYykXjDp", uploadConfig.REQUIRED)
    curseDependency("fzzy-config", uploadConfig.REQUIRED)
}
