package com.mena97villalobos.viewmodelfactorycreator.compiler.ast.implementations.declarations

import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.Visitor
import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.base.BaseDependencyImplementation
import com.mena97villalobos.viewmodelfactorycreator.compiler.syntax.SourcePosition

data class UnrelatedFileContent(val spelling: String, val position: SourcePosition) : BaseDependencyImplementation(position) {
    override fun visit(v: Visitor, o: Any): Any = v.visitUnrelatedFileContent(this, o)

}