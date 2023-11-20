package com.mena97villalobos.viewmodelfactorycreator.compiler

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.implementations.declarations.DependenciesBlockDeclaration
import com.mena97villalobos.viewmodelfactorycreator.compiler.visitors.VersionCatalogGenerator
import com.mena97villalobos.viewmodelfactorycreator.compiler.syntax.Parser
import com.mena97villalobos.viewmodelfactorycreator.compiler.syntax.Scanner
import com.mena97villalobos.viewmodelfactorycreator.compiler.syntax.SourceFile
import com.mena97villalobos.viewmodelfactorycreator.compiler.utils.ErrorReporter
import com.mena97villalobos.viewmodelfactorycreator.compiler.visitors.GradleDependenciesGenerator
import com.mena97villalobos.viewmodelfactorycreator.compiler.visitors.GradleFileGenerator
import com.mena97villalobos.viewmodelfactorycreator.utils.saveFileContents
import java.io.File
import java.io.IOException

class Compiler(private val project: Project) {
    private val versionCatalog = VersionCatalogGenerator()
    private val errorReporter = ErrorReporter()
    private val fileGenerator = GradleFileGenerator()
    private val gradleGenerator by lazy {
        GradleDependenciesGenerator(versionCatalog.versionReference)
    }

    fun compileGradleFiles(files: List<VirtualFile>) {
        val asts = mutableListOf<GradleData>()
        files.forEach {
            asts.add(GradleData(it, compileGradleFile(it)))
        }
        generateVersionCatalogue()
        asts.forEach {
            val newGradleFile = getNewGradleFile(it)
            it.sourceFile.saveFileContents(project, newGradleFile)
        }
    }

    fun compileGradleFile(sourceFile: VirtualFile): DependenciesBlockDeclaration {
        val scanner = Scanner(SourceFile(sourceFile))
        val parser = Parser(scanner, errorReporter)
        return parser.parseGradleFile().apply {
            visit(versionCatalog, Unit)
        }
    }

    fun reportErrors() {
        if (errorReporter.containsErrors()) {
            errorReporter.showAllErrors()
        } else {
            Messages.showInfoMessage(
                "All files are now updated",
                "Compilation Successful"
            )
        }
    }

    fun generateVersionCatalogue() {
        // Must visit all AST before calling this function
        project.basePath?.let { basePath ->
            val baseFolder = VfsUtil.findFileByIoFile(File("$basePath/gradle"), true)
            baseFolder?.let {
                try {
                    WriteCommandAction.runWriteCommandAction(project) {
                        val libsNewFile = it.createChildData(null, "libs.versions.toml")
                        VfsUtil.saveText(libsNewFile, versionCatalog.generateVersionCatalogue())
                    }
                } catch (exception: IOException) {
                    errorReporter.reportError("Something went wrong creating catalog file: $exception")
                }
            }
        }
    }

    fun getNewGradleFile(data: GradleData): String {
        val newAst = data.ast.visit(gradleGenerator, Unit) as DependenciesBlockDeclaration

        val text = StringBuilder(newAst.restOfFile)
        newAst.visit(fileGenerator, text)
        return text.toString()
    }
}

data class GradleData(
    val sourceFile: VirtualFile,
    val ast: DependenciesBlockDeclaration
)