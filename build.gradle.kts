@file:Suppress("PropertyName", "VariableNaming")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.iridium)
    alias(libs.plugins.iridium.publish)
    alias(libs.plugins.iridium.upload)
}

group = property("maven_group")!!
version = property("mod_version")!!
base.archivesName.set(property("archives_base_name") as String)
description = property("description") as String

val modid: String by project
val mod_name: String by project
val modrinth_id: String? by project
val curse_id: String? by project

repositories {
    maven("https://teamvoided.org/releases")
    maven("https://maven.isxander.dev/releases") { name = "Xander Maven" }
    maven("https://maven.terraformersmc.com/") { name = "Terraformers" }
    mavenCentral()
}

modSettings {
    modId(modid)
    modName(mod_name)

//    entrypoint("main", "com.theendercore.water_vision.Template::commonInit")
    entrypoint("client", "com.theendercore.water_vision.WaterVision::clientInit")
    entrypoint("modmenu", "com.theendercore.water_vision.config.ModMenuCompat")
    dependency("yet_another_config_lib_v3", "*")
    mixinFile("$modid.mixins.json")

    accessWidener("$modid.accesswidener")
}

dependencies {
    modImplementation(fileTree("libs"))
    modImplementation(libs.yacl)
    modImplementation(libs.modmenu)

}

loom {
    runs {
        create("TestWorld") {
            client()
            ideConfigGenerated(true)
            runDir("run")
            programArgs("--quickPlaySingleplayer", "test")
        }
    }
}

tasks {
    val targetJavaVersion = 17
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = targetJavaVersion.toString()
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
    modrinthId = modrinth_id
    curseId = curse_id

    // FabricApi
    modrinthDependency("P7dR8mSH", uploadConfig.REQUIRED)
    curseDependency("fabric-api", uploadConfig.REQUIRED)
    // Fabric Language Kotlin
    modrinthDependency("Ha28R6CL", uploadConfig.REQUIRED)
    curseDependency("fabric-language-kotlin", uploadConfig.REQUIRED)
    //YACL
    modrinthDependency("1eAoo2KR", uploadConfig.REQUIRED)
    curseDependency("yacl", uploadConfig.REQUIRED)
}
