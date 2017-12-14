package horilang.nodes

import groovy.transform.ToString
import horilang.lexer.Token
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
class Value extends Node {

    String value
    String type
    Node sign = null

    @Override
    Node build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        if (!nodes.isEmpty()) {
            sign = nodes.last()
            nodes.remove(nodes.lastIndexOf(sign))
        }
        println(nodes)
        value = tokens[0].value
        type = tokens[0].type.toString().toLowerCase().capitalize()
        return this
    }

}
