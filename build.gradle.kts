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
    maven("https://maven.fzzyhmstrs.me/") { name = "FzzyMaven"; content { includeGroup("me.fzzyhmstrs") } }
    maven("https://maven.terraformersmc.com/") { // ModMenu, EMI
        name = "Terraformers"
        content {
            includeGroup("com.terraformersmc")
            includeGroup("dev.emi")
        }
    }
    maven("https://api.modrinth.com/maven") { content { includeGroup("maven.modrinth") } }
    mavenLocal()
    mavenCentral()
    maven("https://teamvoided.org/releases") { content { includeGroup("org.teamvoided") } }
    maven("https://teamvoided.org/snapshots") { content { includeGroup("org.teamvoided") } }
}

dependencies {
    implementation(fileTree("libs"))
    minecraft(libs.minecraft)
    // Dependencies
    implementation(libs.fabric.loader)
    implementation(libs.fabric.api)
    implementation(libs.fzzy.config)
    // Compatibility
    // Runtime
    implementation(libs.modmenu)
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
            runDirectory = File("run")
            programArguments.addAll("--username", username)
            uuid?.let { programArguments.addAll("--uuid", it) }
        }

        create("randomClient") {
            client()
            runDirectory = File("run")
        }

        create("TestWorld") {
            client()
            runDirectory = File("run")
            programArguments.addAll("--quickPlaySingleplayer", "test", "--username", username)
            uuid?.let { programArguments.addAll("--uuid", it) }
        }

        forEach {
            it.generateRunConfig = true
            it.preferGradleTask = true
            it.jvmArguments.addAll(
                // If enabled this you can hotswap basally anything
                // Requires a JetBrains runtime!
//                "-XX:+AllowEnhancedClassRedefinition",
                // If enabled this you can hotswap mixins
                // Requires you to add MIXIN_PATH to your .env file
                // Here is how to find the path: https://docs.fabricmc.net/develop/getting-started/intellij-idea/launching-the-game#1-locate-the-mixin-library-jar
//                "-javaagent:${System.getProperty("MIXIN_PATH")}"
            )
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
            iridium.appendLibsVersionProps(expandProps, projectDir.resolve("libs.versions.toml"))
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

    changelog = projectDir.resolve("changelog.md").readText()

    version += libs.versions.minecraft.get()
    versionName = "${iridium.modName()} ${iridium.modVersion}"
    jarTask = tasks.jar.get()

    dependency("P7dR8mSH", "fabric-api")
    dependency("hYykXjDp", "fzzy-config")
}
