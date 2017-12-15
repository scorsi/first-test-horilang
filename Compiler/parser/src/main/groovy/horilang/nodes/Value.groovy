package horilang.nodes

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import horilang.lexer.Token
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
@EqualsAndHashCode
class Value extends Node {

    String value
    String type

    Value(@NotNull List<Token> tokens, @NotNull List<horilang.nodes.Node> nodes) {
        super(tokens, nodes)
        build(tokens, nodes)
    }

    private build(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        value = tokens[0].value
        type = tokens[0].type.toString().toLowerCase().capitalize()
    }

}
