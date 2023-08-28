import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}
val currentVersion = "1.0.2"
group = "com.xxx"
version = "1.0-SNAPSHOT"
repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}


kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.github.mifmif:generex:1.0.2")
                implementation("com.google.code.gson:gson:2.10")// https://mvnrepository.com/artifact/com.google.code.gson/gson
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "ADBTool"
            packageVersion = currentVersion
            windows {
                // a version for all Windows distributables
                packageVersion = currentVersion
                // a version only for the msi package
                msiPackageVersion = currentVersion
                // a version only for the exe package
                exePackageVersion = currentVersion
                iconFile.set(project.file("launcher/logo.ico"))
                menu = true
                shortcut = true
            }
            macOS {
                // a version for all macOS distributables
                packageVersion = currentVersion
                // a version only for the dmg package
                dmgPackageVersion = currentVersion
                // a version only for the pkg package
                pkgPackageVersion = currentVersion
                // 显示在菜单栏、“关于”菜单项、停靠栏等中的应用程序名称
                dockName = "ADBTool"
                // a build version for all macOS distributables
                packageBuildVersion = currentVersion
                // a build version only for the dmg package
                dmgPackageBuildVersion = currentVersion
                // a build version only for the pkg package
                pkgPackageBuildVersion = currentVersion
                // 设置图标
                iconFile.set(project.file("launcher/logo.icns"))
            }
        }
    }
}
