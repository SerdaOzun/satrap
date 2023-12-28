import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.5.11"
    id("app.cash.sqldelight") version "2.0.1"
}

group = "satrapco.satrap"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

val sqldelight_version: String by project
val precompose_version: String by project

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                api(compose.foundation)
                api(compose.animation)
                implementation("app.cash.sqldelight:sqlite-driver:$sqldelight_version")
                implementation("app.cash.sqldelight:coroutines-extensions:$sqldelight_version")
                implementation("app.cash.sqldelight:primitive-adapters:$sqldelight_version")
                api("moe.tlaster:precompose:$precompose_version")
                api("moe.tlaster:precompose-viewmodel:$precompose_version")
                implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
                implementation("org.slf4j:slf4j-api:2.0.9")
                implementation("org.slf4j:slf4j-simple:2.0.9")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-testng"))
                implementation("io.kotest:kotest-assertions-core-jvm:5.6.2")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }
    }
}

compose {
    desktop {
        application {
            mainClass = "MainKt"
            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "satrap"
                packageVersion = "1.0.0"
                description = "SSH-Management"
                vendor = "Satrap"
                copyright = "© 2023 Serda Özün. All rights reserved."
                licenseFile.set(project.file("LICENSE"))
                modules("java.sql")
                buildTypes.release {
                    proguard {
                        configurationFiles.from("proguard-rules.pro")
                        obfuscate.set(true)
                    }
                }
                windows {
                    iconFile.set(project.file("src/jvmMain/resources/satrap_logo.ico"))
                }
                macOS {
                    iconFile.set(project.file("src/jvmMain/resources/satrap_logo.icns"))
                }
                linux {
                    iconFile.set(project.file("src/jvmMain/resources/satrap_logo.png"))
                }
            }
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("satrapco.satrap")
            version = 1
        }
    }
}

tasks.withType<Test> {
    useTestNG()
}