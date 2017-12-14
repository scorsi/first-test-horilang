package com.scorsi.horilang.ast

import com.scorsi.horilang.Token
import groovy.transform.ToString
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class Expression extends Node {

    Node leftValue
    Node operator = null
    Node rightValue = null

    @Override
    Node build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        def node = nodes.last()
        if (node == null || !(node instanceof Value || node instanceof Expression))
            throw new RuntimeException("Unexpected parameter, wanted [Expression, Value] but got $node")
        rightValue = node
        nodes.remove(nodes.lastIndexOf(node))

        if (nodes.isEmpty()) {
            leftValue = rightValue
            rightValue = null
            return this
        }
        node = nodes.last()
        if (!node instanceof Operator)
            throw new RuntimeException("Unexpected parameter, wanted [Operator] but got $node")
        operator = node
        nodes.remove(nodes.lastIndexOf(node))
        node = nodes.last()
        if (!node instanceof Value && !node instanceof Expression)
            throw new RuntimeException("Unexpected parameter, wanted [Expression, Value] but got $node")
        leftValue = node
        nodes.remove(nodes.lastIndexOf(node))
        return this
    }

}
