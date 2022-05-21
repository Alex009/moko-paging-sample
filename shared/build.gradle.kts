plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("dev.icerock.mobile.multiplatform-resources")
    id("dev.icerock.moko.kswift")
}

version = "1.0"

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        name = "MultiPlatformLibrary"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "MultiPlatformLibrary"

            isStatic = false

            export("dev.icerock.moko:mvvm-core:0.13.0")
            export("dev.icerock.moko:mvvm-livedata:0.13.0")
            export("dev.icerock.moko:mvvm-livedata-resources:0.13.0")
            export("dev.icerock.moko:mvvm-state:0.13.0")
            export("dev.icerock.moko:resources:0.19.1")
            export("dev.icerock.moko:units:0.8.0")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("dev.icerock.moko:mvvm-core:0.13.0")
                api("dev.icerock.moko:mvvm-livedata:0.13.0")
                api("dev.icerock.moko:mvvm-livedata-resources:0.13.0")
                api("dev.icerock.moko:mvvm-state:0.13.0")
                api("dev.icerock.moko:resources:0.19.1")
                api("dev.icerock.moko:units:0.8.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "ru.alex009.moko.paging"
}

kswift {
    install(dev.icerock.moko.kswift.plugin.feature.PlatformExtensionFunctionsFeature) {
        filter = excludeFilter(
            "PackageFunctionContext/dev.icerock.moko:mvvm-state/dev.icerock.moko.mvvm/TypeParameter(id=0)/asState/",
            "PackageFunctionContext/dev.icerock.moko:mvvm-state/dev.icerock.moko.mvvm/TypeParameter(id=0)/asState/whenNull:Class(name=kotlin/Function0)<Class(name=dev/icerock/moko/mvvm/ResourceState)<TypeParameter(id=0),TypeParameter(id=1)>>"
        )
    }
    install(dev.icerock.moko.kswift.plugin.feature.SealedToSwiftEnumFeature)
}
