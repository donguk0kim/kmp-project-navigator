package com.dkim.kmpprojectnavigator.nodes

internal object KmpSourceSetContentSort {
    const val MANIFESTS_FOLDER = "manifests"

    private val RESOURCE_FOLDERS = setOf("res", "resources", "composeResources")

    fun isResourceFolder(name: String): Boolean = name in RESOURCE_FOLDERS

    fun sortKey(name: String): String {
        val group = when {
            name == MANIFESTS_FOLDER -> 0
            isResourceFolder(name) -> 2
            else -> 1
        }

        return "$group:$name"
    }
}
