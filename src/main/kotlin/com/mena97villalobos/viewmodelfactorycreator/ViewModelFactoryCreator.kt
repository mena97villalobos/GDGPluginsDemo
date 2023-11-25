package com.mena97villalobos.viewmodelfactorycreator

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleManager
import org.jetbrains.kotlin.backend.common.descriptors.allParameters
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.idea.search.usagesSearch.constructor
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.resolve.ImportPath

class ViewModelFactoryCreator: PsiElementBaseIntentionAction(), IntentionAction {

    // TODO 8: Nombre que se mostrará en las configuraciones del IDE
    override fun getFamilyName(): String = "View model factory creator"

    // TODO 9: Texto que se mostrará en el context actions del IDE
    override fun getText(): String = "Create view model factory"

    // TODO 10: Función que determina si el context action debe mostrarse
    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean =
            true

    // TODO 11: Función que se ejecuta al seleccionar el context action usaremos el PSI para crear código de kotlin
    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val clazz = element.parent as KtClass
        val ktImports = (clazz.parent as KtFile).importList
        val elementFactory = KtPsiFactory(project)
        val viewModelParams = clazz.constructor?.allParameters?.filterIsInstance<ValueParameterDescriptor>()

        // Generate View Model Factory and ViewModelProvider import directive
        val viewModelFactoryClass =
                elementFactory.createClass(getViewModelFactory(clazz.name ?: "ViewModel", viewModelParams))

        // Check if ViewModelProvider import already exists, if not add it
        if (ktImports?.children?.any { it.text.contains("ViewModelProvider") } != true) {
            val importDirective =
                    elementFactory.createImportDirective(ImportPath.fromString("androidx.lifecycle.ViewModelProvider"))
            ktImports?.add(importDirective)
        }

        // Add view model factory to code base
        clazz.body?.addAfter(viewModelFactoryClass, clazz.body?.rBrace)
        // Reformat code
        CodeStyleManager.getInstance(project).reformat(clazz.parent)
    }

    private fun generateParametersList(list: List<ValueParameterDescriptor>) =
            list.joinToString { "private val ${it.name}: ${it.type}" }

    private fun generateCallingParams(list: List<ValueParameterDescriptor>) =
            list.joinToString { it.name.toString() }

    private fun getViewModelFactory(
            className: String,
            params: List<ValueParameterDescriptor>?
    ) = """


        @Suppress("UNCHECKED_CAST")
        class ${className}Factory(${generateParametersList(params ?: emptyList())}) :
            ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(${className}::class.java))
                    return ${className}(${generateCallingParams(params ?: emptyList())}) as T
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
        """
}