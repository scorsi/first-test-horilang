import com.scorsi.horilang.lexer.Lexer;
import com.scorsi.horilang.lexer.LexerStream;
import com.scorsi.horilang.lexer.Token;
import com.scorsi.horilang.lexer.TokenType;
import com.scorsi.horilang.lexer.builder.LexerBuilder;
import org.junit.Before;
import org.junit.Test;

public class LexerTest {

    Lexer lexer;

    @Before
    public void init() {
        lexer = new LexerBuilder().defaultBuild();
    }

    private void expectedToken(Token expectedToken, Token givenToken, Boolean testValue) {
        assert expectedToken.getType().equals(givenToken.getType());
        assert !testValue || expectedToken.getValue().equals(givenToken.getValue());
    }

    @Test
    public void testBasicVarDeclarationWithAssignment() {
        LexerStream stream = lexer.lex("var a : Integer = 1;");

        expectedToken(new Token(TokenType.VAR, null), stream.next(), false);
        expectedToken(new Token(TokenType.SYMBOL, "a"), stream.next(), true);
        expectedToken(new Token(TokenType.DDOT, null), stream.next(), false);
        expectedToken(new Token(TokenType.TYPE, "Integer"), stream.next(), true);
        expectedToken(new Token(TokenType.ASSIGN, null), stream.next(), false);
        expectedToken(new Token(TokenType.NUMBER, "1"), stream.next(), true);
        expectedToken(new Token(TokenType.SEMICOLON, null), stream.next(), false);
        assert stream.next() == null;
    }

    @Test
    public void testBasicIfWithoutBody() {
        LexerStream stream = lexer.lex("if (a == 1) { }");

        expectedToken(new Token(TokenType.IF, null), stream.next(), false);
        expectedToken(new Token(TokenType.LPAREN, null), stream.next(), false);
        expectedToken(new Token(TokenType.SYMBOL, "a"), stream.next(), true);
        expectedToken(new Token(TokenType.EQUAL, null), stream.next(), false);
        expectedToken(new Token(TokenType.NUMBER, "1"), stream.next(), true);
        expectedToken(new Token(TokenType.RPAREN, null), stream.next(), false);
        expectedToken(new Token(TokenType.LBRACE, null), stream.next(), false);
        expectedToken(new Token(TokenType.RBRACE, null), stream.next(), false);
        assert stream.next() == null;
    }

    @Test
    public void testIfElseWithoutBody() {
        LexerStream stream = lexer.lex("if (a == 3) { } else { }");

        expectedToken(new Token(TokenType.IF, null), stream.next(), false);
        expectedToken(new Token(TokenType.LPAREN, null), stream.next(), false);
        expectedToken(new Token(TokenType.SYMBOL, "a"), stream.next(), true);
        expectedToken(new Token(TokenType.EQUAL, null), stream.next(), false);
        expectedToken(new Token(TokenType.NUMBER, "3"), stream.next(), true);
        expectedToken(new Token(TokenType.RPAREN, null), stream.next(), false);
        expectedToken(new Token(TokenType.LBRACE, null), stream.next(), false);
        expectedToken(new Token(TokenType.RBRACE, null), stream.next(), false);
        expectedToken(new Token(TokenType.ELSE, null), stream.next(), false);
        expectedToken(new Token(TokenType.LBRACE, null), stream.next(), false);
        expectedToken(new Token(TokenType.RBRACE, null), stream.next(), false);
        assert stream.next() == null;
    }

    @Test
    public void testBasicFunctionCall() {
        LexerStream stream = lexer.lex("print(\"test\")");

        expectedToken(new Token(TokenType.SYMBOL, "print"), stream.next(), true);
        expectedToken(new Token(TokenType.LPAREN, null), stream.next(), false);
        expectedToken(new Token(TokenType.STRING, "\"test\""), stream.next(), true);
        expectedToken(new Token(TokenType.RPAREN, null), stream.next(), false);
        assert stream.next() == null;
    }

}
