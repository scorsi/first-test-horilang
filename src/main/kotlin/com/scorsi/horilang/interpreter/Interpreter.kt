package com.scorsi.horilang.interpreter

import com.scorsi.horilang.parser.ast.*
import javax.swing.plaf.nimbus.State

class Interpreter constructor(private val root: Node) {

    private var variables: MutableList<Variable> = mutableListOf()

    private fun getVariable(symbol: String) : Variable =
            variables.find { it.symbol.equals(symbol) }.let { variable ->
                when (variable) {
                    null -> throw Error("Cannot find variable named $symbol.")
                    else -> variable
                }
            }

    private fun hasVariable(symbol: String) : Boolean =
            variables.any { it.symbol.equals(symbol) }

    private fun declareVariable(node: DeclarationNode): String? =
            when {
                hasVariable(node.symbol.value) -> throw Error("Cannot redefine a variable with the same name.")
                else -> interpretNode(node.rightValue, null).let { value ->
                        variables.add(Variable(node.symbol.value, node.type.value as String, value))
                        value
                    }
            }

    private fun assignVariable(node: AssignmentNode) : String? =
            when {
                hasVariable(node.leftValue.value as String) ->
                        getVariable(node.leftValue.value).let { variable ->
                            interpretNode(node.rightValue).let { value ->
                                variable.value = value
                                value
                            }
                        }
                else -> throw Error("Undefined reference ${node.leftValue.value}")
            }

    private fun interpretBlockNode(node: BlockNode) : String? {
        var value: String? = null
        node.statements.forEach { statement ->
            value = interpretNode(statement)
        }
        return value
    }

    private fun interpretNode(node: Node?, value: String? = null): String? =
            when (node) {
                null -> value
                is BlockNode -> interpretBlockNode(node)
                is DeclarationNode -> declareVariable(node)
                is AssignmentNode -> assignVariable(node)
                is SymbolNode -> getVariable(node.value).value
                is ValueNode -> node.value
                else -> value
            }

    fun interpret(): String? = interpretNode(root)

}