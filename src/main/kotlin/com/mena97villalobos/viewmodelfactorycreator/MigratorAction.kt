package com.mena97villalobos.viewmodelfactorycreator

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.vfs.VfsUtil
import com.mena97villalobos.viewmodelfactorycreator.compiler.Compiler
import com.mena97villalobos.viewmodelfactorycreator.compiler.GradleData
import com.mena97villalobos.viewmodelfactorycreator.utils.saveFileContents
import java.io.File


// TODO 15: Añadir una clase que extienda AnAction
class MigratorAction: AnAction() {

    // TODO 16: Sobreescribir un la función action performed que ejecutará la lógica del plugin
    override fun actionPerformed(event: AnActionEvent) {
        // TODO 17: event contiene toda la información relativa del editor
        event.project?.basePath?.let { basePath ->
            val compiler = Compiler(event.project!!)

            // TODO 18: Obtener un file utilizando Vfs
            VfsUtil.findFileByIoFile(File("$basePath/app/build.gradle.kts"), true)?.let {file ->
                val ast = GradleData(file, compiler.compileGradleFile(file))
                compiler.generateVersionCatalogue()
                compiler.getNewGradleFile(ast)
                val newGradleFile = compiler.getNewGradleFile(ast)

                // TODO 19: Escribir un archivo utilizando VFS
                WriteCommandAction.runWriteCommandAction(event.project!!) {
                    VfsUtil.saveText(file, newGradleFile)
                }
            }
        }
    }
}