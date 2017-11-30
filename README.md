# Horilang
Horilang is my own programming langage, done for fun

For the moment, the langage is not usable.

## How to compile

For windows:
```bash
gradlew.bat package
java -jar bin/horilang-0.1-alpha.jar
```

For linux:
```bash
./gradlew package
java -jar bin/horilang-0.1-alpha.jar
```

## How to try the Lexer

Actually the Lexer is fully fonctionnal. You got all the (actual) available tokens of the Horilang langage.

Here's an example in Groovy (the same is possible in Java):

```groovy
import com.scorsi.horilang.lexer.Token
import com.scorsi.horilang.lexer.builder.LexerBuilder

def lexer = new LexerBuilder().defaultBuild()
def stream = lexer.lex("var a = 1; if (a == true) { print(\"success\") }")
Token token = stream.next()
while (token) {
  println(token)
  token = stream.next()
}
```

## How to try the Parser

Here's an example in Groovy (the same is possible in Java).

It will just display the actual parsed AST of the given input.

```groovy
import com.scorsi.horilang.lexer.Token
import com.scorsi.horilang.lexer.builder.LexerBuilder
import com.scorsi.horilang.parser.Parser

def lexer = new LexerBuilder().defaultBuild()
def stream = lexer.lex("var a = 1; if (a == true) { print(\"success\") }")
def parser = new Parser(stream)
def node = parser.parse()
println(node)
```
