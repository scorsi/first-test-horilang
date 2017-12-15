package horilang.nodes

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import horilang.lexer.Token
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
@EqualsAndHashCode
class ExportDeclaration extends Node {

    Node statement
    String symbol

    ExportDeclaration(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        super(tokens, nodes)

        if (nodes.isEmpty()) {
            symbol = tokens[1].value
        } else {
            statement = nodes.last()
            nodes.remove(nodes.last())
        }
    }

}
