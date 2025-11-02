plugins {
    id("fabric-loom") version "1.11.4"
    id("io.github.juuxel.loom-quiltflower") version "1.10.0"
    id("com.modrinth.minotaur") version "2.8.7"
    java
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

group = "dev.fastpath"
version = "1.0.0"

repositories {
    maven("https://maven.fabricmc.net/")
    mavenCentral()
}

dependencies {
    // Minecraft
    minecraft("com.mojang:minecraft:1.21.8")

    // Official Mojang mappings (recommended for 1.21+)
    mappings(loom.officialMojangMappings())

    // Fabric Loader
    modImplementation("net.fabricmc:fabric-loader:0.16.14")

    // Fabric API (local jar for dev, but Modrinth will declare dependency)
    modImplementation(files("libs/fabric-api-0.136.0+1.21.8.jar"))

    // Optional dependencies
    modImplementation(files("libs/modmenu-15.0.0.jar"))
    modImplementation(files("libs/sodium-fabric-0.7.2+mc1.21.8.jar"))
}

loom {
    runs {
        named("client") {
            client()
            name("Fastpath Client")
            vmArgs(
                "-Xms2G",
                "-Xmx4G",
                "-XX:+UnlockExperimentalVMOptions"
            )
        }
    }
}

// Ensures Java 21 bytecode
tasks.withType<JavaCompile> {
    options.release.set(21)
}

// Build JAR metadata
tasks.jar {
    from("LICENSE") {
        rename { "LICENSE_${project.name}" }
    }
}

// ----------------------
// Modrinth Publishing
// ----------------------
modrinth {
    token.set(System.getenv("mrp_Xja2UliI8N1sDHB9dQjFOX977rFWJ6iYI8x2uffrgCGjhwIDtAsUTNY01U0U"))
    projectId.set("fastpath") // your Modrinth project slug/ID
    versionNumber.set(project.version.toString())
    versionName.set("Fastpath ${project.version}")
    changelog.set("Initial release with compile throttling, flag-based rebuild detection, and HUD stats.")
    uploadFile.set(tasks.remapJar)
    gameVersions.set(listOf("1.21.8"))
    loaders.set(listOf("fabric"))

    // Dependencies (Fabric API required, Mod Menu optional)
    dependencies {
        required.project("P7dR8mSH") // Fabric API
        optional.project("mOgUt4GM") // Mod Menu
    }
}