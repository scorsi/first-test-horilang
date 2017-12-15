package horilang.nodes

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import horilang.lexer.Token
import org.jetbrains.annotations.NotNull

@ToString(includeNames = true, includePackage = false, ignoreNulls = true)
@EqualsAndHashCode
class ModuleDeclaration extends Node {

    String name

    ModuleDeclaration(@NotNull List<Token> tokens, @NotNull List<Node> nodes) {
        super(tokens, nodes)

        name = tokens[1].value
    }

}
