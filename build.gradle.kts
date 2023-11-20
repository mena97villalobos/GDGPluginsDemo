plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("org.jetbrains.intellij") version "1.15.0"
}

group = "com.mena97villalobos"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

// TODO 1: Cambiar la configuración de Intellij para utilizar Android Studio
intellij {
    // TODO 2: Cambiar la versión para utilizar Android Studio 2023.1.1.16
    version.set("2022.2.5")

    // Target IDE Platform https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html#intellij-extension-type
    // IC - IntelliJ IDEA Community
    // AI - Android Studio
    type.set("IC")

    plugins.set(listOf("android", "org.jetbrains.kotlin"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
        // TODO 3: Compatibilidad de versiones, en el market place solamente se
        //  mostrará el plugin para versiones de IntelliJ entre estos valores
        sinceBuild.set("222")
        untilBuild.set("232.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
