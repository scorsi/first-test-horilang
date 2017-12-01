import com.scorsi.horilang.lexer.Lexer;
import com.scorsi.horilang.lexer.TokenType;
import com.scorsi.horilang.lexer.builder.LexerBuilder;
import com.scorsi.horilang.parser.Parser;
import com.scorsi.horilang.parser.ast.*;
import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    Lexer lexer;

    @Before
    public void init() {
        lexer = new LexerBuilder().defaultBuild();
    }

    @Test
    public void testEmptyInput() {
        assert new Parser(lexer.lex("")).parse() == null;
    }

    @Test
    public void testDeclaration() {
        Parser parser = new Parser(lexer.lex("var a : Integer"));
        Node node = parser.parse();
        assert node != null;
        assert node instanceof BlockNode;
        assert ((BlockNode) node).getStatements().size() == 1;
        assert ((BlockNode) node).getStatements().get(0) instanceof DeclarationNode;
        DeclarationNode declarationNode = (DeclarationNode)((BlockNode) node).getStatements().get(0);
        assert declarationNode.getSymbol().getValue().getType() == TokenType.SYMBOL && declarationNode.getSymbol().getValue().getValue().equals("a");
        assert declarationNode.getType().getType() == TokenType.TYPE && declarationNode.getType().getValue().equals("Integer");
        assert declarationNode.getRightValue() == null;
    }

    @Test
    public void testDeclarationWithAssignment() {
        Parser parser = new Parser(lexer.lex("var testString : String = \"test test\""));
        Node node = parser.parse();
        assert node != null;
        assert node instanceof BlockNode;
        assert ((BlockNode) node).getStatements().size() == 1;
        assert ((BlockNode) node).getStatements().get(0) instanceof DeclarationNode;
        DeclarationNode declarationNode = (DeclarationNode)((BlockNode) node).getStatements().get(0);
        assert declarationNode.getSymbol().getValue().getType() == TokenType.SYMBOL && declarationNode.getSymbol().getValue().getValue().equals("testString");
        assert declarationNode.getType().getType() == TokenType.TYPE && declarationNode.getType().getValue().equals("String");
        assert ((ValueNode)declarationNode.getRightValue()).getValue().getType() == TokenType.STRING && ((ValueNode)declarationNode.getRightValue()).getValue().getValue().equals("\"test test\"");
    }

    @Test
    public void testAssignment() {
        Parser parser = new Parser(lexer.lex("abcAZ9 = \"test\""));
        Node node = parser.parse();
        assert node != null;
        assert node instanceof BlockNode;
        assert ((BlockNode) node).getStatements().size() == 1;
        assert ((BlockNode) node).getStatements().get(0) instanceof AssignmentNode;
        AssignmentNode assignmentNode = (AssignmentNode)((BlockNode) node).getStatements().get(0);
        assert assignmentNode.getLeftValue().getType() == TokenType.SYMBOL && assignmentNode.getLeftValue().getValue().equals("abcAZ9");
        //assert assignmentNode.getRightValue().getType() == TokenType.STRING && assignmentNode.getRightValue().getValue().equals("\"test\"");
    }

    @Test
    public void testEmptyIf() {
        Parser parser = new Parser(lexer.lex("if (a == 0) {}"));
        Node node = parser.parse();
        assert node != null;
        assert node instanceof BlockNode;
        assert ((BlockNode) node).getStatements().size() == 1;
        assert ((BlockNode) node).getStatements().get(0) instanceof ConditionalBranchNode;
        ConditionalBranchNode conditionalBranchNode = (ConditionalBranchNode)((BlockNode) node).getStatements().get(0);
        assert conditionalBranchNode.getThenBlock() == null;
        assert conditionalBranchNode.getElseBlock() == null;
    }

    @Test
    public void testBasicIf() {
        Parser parser = new Parser(lexer.lex("if (a == 0) { b = 1; }"));
        Node node = parser.parse();
        assert node != null;
        assert node instanceof BlockNode;
        assert ((BlockNode) node).getStatements().size() == 1;
        assert ((BlockNode) node).getStatements().get(0) instanceof ConditionalBranchNode;
        ConditionalBranchNode conditionalBranchNode = (ConditionalBranchNode)((BlockNode) node).getStatements().get(0);
        assert conditionalBranchNode.getCondition().getLeft().getType() == TokenType.SYMBOL && conditionalBranchNode.getCondition().getLeft().getValue().equals("a");
        assert conditionalBranchNode.getCondition().getRight().getType() == TokenType.NUMBER && conditionalBranchNode.getCondition().getRight().getValue().equals("0");
        assert conditionalBranchNode.getCondition().getOperator().getType() == TokenType.EQUAL;
        BlockNode thenBlock = conditionalBranchNode.getThenBlock();
        assert thenBlock.getStatements().size() == 1;
        assert thenBlock.getStatements().get(0) instanceof AssignmentNode;
        AssignmentNode assignmentNode = (AssignmentNode)thenBlock.getStatements().get(0);
        assert assignmentNode.getLeftValue().getValue().equals("b");
        assert ((ValueNode)assignmentNode.getRightValue()).getValue().getType() == TokenType.NUMBER;
        assert ((ValueNode)assignmentNode.getRightValue()).getValue().getValue().equals("1");
        assert conditionalBranchNode.getElseBlock() == null;
    }

    @Test
    public void testBasicIfWithElse() {
        Parser parser = new Parser(lexer.lex("if (a == 0) { b = 1; } else { c = 2; }"));
        Node node = parser.parse();
        assert node != null;
        assert node instanceof BlockNode;
        assert ((BlockNode) node).getStatements().size() == 1;
        assert ((BlockNode) node).getStatements().get(0) instanceof ConditionalBranchNode;
        ConditionalBranchNode conditionalBranchNode = (ConditionalBranchNode)((BlockNode) node).getStatements().get(0);
        assert conditionalBranchNode.getCondition().getLeft().getType() == TokenType.SYMBOL && conditionalBranchNode.getCondition().getLeft().getValue().equals("a");
        assert conditionalBranchNode.getCondition().getRight().getType() == TokenType.NUMBER && conditionalBranchNode.getCondition().getRight().getValue().equals("0");
        assert conditionalBranchNode.getCondition().getOperator().getType() == TokenType.EQUAL;
        BlockNode thenBlock = conditionalBranchNode.getThenBlock();
        assert thenBlock.getStatements().size() == 1;
        assert thenBlock.getStatements().get(0) instanceof AssignmentNode;
        AssignmentNode assignmentNode = (AssignmentNode)thenBlock.getStatements().get(0);
        assert assignmentNode.getLeftValue().getValue().equals("b");
        assert ((ValueNode)assignmentNode.getRightValue()).getValue().getType() == TokenType.NUMBER;
        assert ((ValueNode)assignmentNode.getRightValue()).getValue().getValue().equals("1");
        assert conditionalBranchNode.getElseBlock() != null;
        BlockNode elseBlock = conditionalBranchNode.getElseBlock();
        assert elseBlock.getStatements().size() == 1;
        assert thenBlock.getStatements().get(0) instanceof AssignmentNode;
        assignmentNode = (AssignmentNode)elseBlock.getStatements().get(0);
        assert assignmentNode.getLeftValue().getValue().equals("c");
        assert ((ValueNode)assignmentNode.getRightValue()).getValue().getType() == TokenType.NUMBER;
        assert ((ValueNode)assignmentNode.getRightValue()).getValue().getValue().equals("2");
    }

}
