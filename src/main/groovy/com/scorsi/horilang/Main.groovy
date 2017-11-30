package com.scorsi.horilang

import com.scorsi.horilang.lexer.Token
import com.scorsi.horilang.lexer.builder.LexerBuilder
import com.scorsi.horilang.parser.Parser

class Main {

    static void main(String[] args) {
        def lexer = new LexerBuilder().defaultBuild()

        def input = new Scanner(System.in)
        while (input.hasNext()) {
            def stream = lexer.lex(input.nextLine())

            Token token = stream.next()
            while (token) {
                println(token)
                token = stream.next()
            }

            stream.reset()
            def parser = new Parser(stream)
            def node = parser.parse()
            println(node)
        }
    }

}
