package com.scorsi.horilang

import com.scorsi.horilang.interpreter.Interpreter
import com.scorsi.horilang.lexer.builder.LexerBuilder
import com.scorsi.horilang.parser.Parser

class ParserRunner implements Runnable {

    String input

    ParserRunner(String input) {
        this.input = input
    }

    @Override
    void run() {
        def lexer = new LexerBuilder().defaultBuild()
        def stream = lexer.lex(this.input)
        /*
        def token = stream.next()
        while (token) {
            println(token)
            token = stream.next()
        }
        stream.reset()
        */
        def node = new Parser(stream).parse()
        println(node)
        def interpreter = new Interpreter(node)
        def ret = interpreter.interpret()
        println(ret)
    }
}
