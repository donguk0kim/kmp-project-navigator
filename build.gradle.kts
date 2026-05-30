import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.changelog")
    id("org.jetbrains.intellij.platform")
}

dependencies {
    intellijPlatform {
        intellijIdea("2025.3.5")
        testFramework(TestFrameworkType.Platform)
        bundledPlugin("com.intellij.java")
        plugin("org.jetbrains.android:253.33514.17")
        plugin("com.jetbrains.kmm:0.9-253.32098-IJ-37")
    }
}
