import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.changelog")
    id("org.jetbrains.intellij.platform")
}

dependencies {
    intellijPlatform {
        intellijIdea("2026.1.2")
        testFramework(TestFrameworkType.Platform)
        bundledPlugin("com.intellij.java")
    }
}

intellijPlatform {
    pluginConfiguration {
        changeNotes.set(
            provider {
                changelog.renderItem(
                    changelog.get(project.version.toString()),
                    org.jetbrains.changelog.Changelog.OutputType.HTML,
                )
            }
        )

        ideaVersion {
            sinceBuild.set("261.24374.151")
        }
    }
}
