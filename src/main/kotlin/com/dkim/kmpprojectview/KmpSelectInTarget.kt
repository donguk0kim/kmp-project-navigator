package com.dkim.kmpprojectview

import com.intellij.ide.impl.ProjectViewSelectInTarget
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileSystemItem

class KmpSelectInTarget(project: Project) : ProjectViewSelectInTarget(project) {

    override fun toString(): String = "KMP Project"

    override fun canSelect(item: PsiFileSystemItem): Boolean = true

    override fun getMinorViewId(): String = KmpProjectViewPane.ID

    override fun getWeight(): Float = 10f
}
