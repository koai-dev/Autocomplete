plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}
group = "com.koai-dev"
version = "1.0.0"

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        multiDexEnabled = true
        aarMetadata {
            minCompileSdk = 29
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    api("androidx.recyclerview:recyclerview:1.1.0")
}


afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.koai"
                artifactId = "autocomplete"
                version = "1.0.0"

                afterEvaluate {
                    from(components["release"])
                }
            }
        }

    }
}

tasks.register("localBuild") {
    dependsOn("assembleRelease")
}

tasks.register("createReleaseTag") {
    doLast {
        val tagName = "v1.0.0"
        try {
            exec {
                commandLine("git", "tag", "-a", tagName, "-m", "Release tag $tagName")
            }

            exec {
                commandLine("git", "push", "origin", tagName)
            }
        } catch (e: Exception) {
            println(e.toString())
        }
    }
}