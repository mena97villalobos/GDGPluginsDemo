package com.mena97villalobos.viewmodelfactorycreator.compiler.ast.implementations.declarations

import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.Visitor
import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.base.Terminal
import com.mena97villalobos.viewmodelfactorycreator.compiler.ast.base.BaseVariable
import com.mena97villalobos.viewmodelfactorycreator.compiler.syntax.SourcePosition

data class VariableDeclaration(
        val identifier: Terminal,
        val value: Terminal,
        val position: SourcePosition
): BaseVariable(position) {
    override fun visit(v: Visitor, o: Any): Any = v.visitVariableDeclaration(this, o)
}