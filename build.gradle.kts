buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.android.tools.build:gradle:7.2.0")
        classpath("dev.icerock.moko:resources-generator:0.19.1")
        classpath("dev.icerock.moko:kswift-gradle-plugin:0.5.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
    configurations.configureEach {
        resolutionStrategy {
            force("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1-native-mt")
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}