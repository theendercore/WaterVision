@file:Suppress("PropertyName", "VariableNaming")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.iridium)
    alias(libs.plugins.iridium.publish)
    alias(libs.plugins.iridium.upload)
    alias(libs.plugins.fabric.loom)
}

repositories {
    maven("https://maven.fabricmc.net/")
    maven("https://teamvoided.org/releases") { content { includeGroup("org.teamvoided") } }
    maven("https://teamvoided.org/snapshots") { content { includeGroup("org.teamvoided") } }
    maven("https://maven.fzzyhmstrs.me/") { name = "FzzyMaven"; content { includeGroup("me.fzzyhmstrs") } }
    maven("https://maven.terraformersmc.com/") {
        name = "Terraformers"
        content {
            includeGroup("com.terraformersmc")
            includeGroup("dev.emi")
        }
    }
    maven("https://api.modrinth.com/maven") { content { includeGroup("maven.modrinth") } }
    mavenLocal()
    mavenCentral()
}

dependencies {
    modImplementation(fileTree("libs"))
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    // Dependencies
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.fzzy.config)
    // Compatibility
    // Runtime
    modImplementation(libs.modmenu)
}

val username = "vDev"
val uuid = iridium.fetchUUID(username) // Dev & vDev will always be null

loom {
    mods {
        register(iridium.modId) {
            sourceSet(sourceSets.main.get())
        }
    }
    runs {
        named("client") {
            programArgs("--username", username)
            uuid?.let { programArgs("--uuid", it) }
        }

        create("randomClient") {
            client()
            runDir("run")
            ideConfigGenerated(true)
        }

        create("TestWorld") {
            client()
            runDir("run")
            ideConfigGenerated(true)
            programArgs("--quickPlaySingleplayer", "test", "--username", username)
            uuid?.let { programArgs("--uuid", it) }
        }
    }
}

tasks {
    val javaVersion = libs.versions.java.get()
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(javaVersion.toInt())
    }

    withType<KotlinCompile>().all {
        compilerOptions.jvmTarget = JvmTarget.fromTarget(javaVersion)
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(JavaVersion.toVersion(javaVersion).toString()))
        withSourcesJar()
    }

    sourceSets.forEach { set ->
        named<ProcessResources>(set.processResourcesTaskName) {
            var expandProps = iridium.props.toMutableMap()
            iridium.appendLibsVersionProps(expandProps, File("libs.versions.toml"))
            filesMatching(
                listOf("pack.mcmeta", "fabric.mod.json", "META-INF/mods.toml", "META-INF/neoforge.mods.toml")
            ) {
                expand(expandProps)
            }
            inputs.properties(expandProps)
        }
    }
}

publishScript {
    releaseRepository("TeamVoided", "https://maven.teamvoided.org/releases")
    publication(iridium.modId, isSnapshot = false)
    publishSources = true
}

uploadScript {
    debugMode = false

    modrinthId = "CXryw0YT"
    curseId = "890050"

    changelog = File("changelog.md").readText()

    version += libs.versions.minecraft.get()
    versionName = "${iridium.modName()} ${iridium.modVersion}"
    jarTask = tasks.remapJar.get()

    dependency("P7dR8mSH", "fabric-api")
    dependency("hYykXjDp", "fzzy-config")
}
