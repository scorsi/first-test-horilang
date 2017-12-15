package horilang.nodes

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import horilang.lexer.Token
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
@EqualsAndHashCode
class ImportDeclaration extends Node{

    String symbol

    ImportDeclaration(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        super(tokens, nodes)
        symbol = tokens[1]
    }

}
