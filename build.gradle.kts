import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("app.cash.sqldelight") version "2.0.0-rc01"
}

group = "satrapin.satrap"
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
            }
        }
        val jvmTest by getting
    }
}

compose {
    kotlinCompilerPlugin.set(dependencies.compiler.forKotlin("1.8.20"))
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.8.21")
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
            packageName.set("satrapin.satrap")
            version = 1
        }
    }
}