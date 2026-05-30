package com.dkim.kmpprojectnavigator

import com.intellij.ide.impl.ProjectViewSelectInTarget
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiFileSystemItem

class KmpSelectInTarget(private val project: Project) : ProjectViewSelectInTarget(project) {

    override fun toString(): String = "KMP Project"

    override fun canSelect(item: PsiFileSystemItem): Boolean {
        val file = item.virtualFile ?: return false
        val basePath = project.basePath ?: return false
        val baseDir = LocalFileSystem.getInstance().findFileByPath(basePath) ?: return false
        return file == baseDir || VfsUtil.isAncestor(baseDir, file, false)
    }

    override fun getMinorViewId(): String = KmpProjectNavigatorPane.ID

    override fun getWeight(): Float = 10f
}
