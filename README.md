# KMP Project Navigator

KMP Project Navigator adds a dedicated IntelliJ project view for Kotlin Multiplatform projects.
It groups Gradle modules, source sets, manifests, iOS app files, and Gradle scripts into a layout tailored for KMP development.

## Features

- Adds a `KMP Project` pane to the Project tool window.
- Shows Gradle/KMP modules as top-level nodes.
- Groups each module by source set, such as `main`, `commonMain`, `androidMain`, and `iosMain`.
- Shows all folders inside each source set, including custom folders such as `cpp` or `graphql`.
- Collects project and module Gradle files under `Gradle Scripts`.
- Detects iOS app directories that contain `.xcodeproj` or `.xcworkspace` files.

## Compatibility

- IntelliJ IDEA `2026.1.2+`.
- Uses the Java runtime bundled with supported IntelliJ versions.
- Intended for conventional Gradle Kotlin Multiplatform project layouts.

## Development

Run verification:

```bash
./gradlew check verifyPluginStructure
```

Run the plugin in a sandbox IDE:

```bash
./gradlew runIde
```

Build the plugin ZIP:

```bash
./gradlew buildPlugin
```

## Notes

The view is intentionally lightweight.
It uses IntelliJ module/content-root information and project folders rather than parsing the full Gradle/Kotlin model.

## License

KMP Project Navigator is released under the MIT License.
