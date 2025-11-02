plugins {
    id("fabric-loom") version "1.11.4"
    id("io.github.juuxel.loom-quiltflower") version "1.10.0"
    java
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

group = "dev.fastpath"
version = "0.1.0"

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

    // Fabric API
    modImplementation(files("libs/fabric-api-0.136.0+1.21.8.jar"))

    // Dependencies
    modImplementation(files("libs/modmenu-15.0.0.jar"))
    modImplementation(files("libs/sodium-fabric-0.7.2+mc1.21.8.jar"))
}

loom {
    // Modify existing default "client" run config
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
