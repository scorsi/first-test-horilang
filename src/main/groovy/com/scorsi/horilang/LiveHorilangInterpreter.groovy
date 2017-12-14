package com.scorsi.horilang

class LiveHorilangInterpreter implements Runnable {

    String input

    LiveHorilangInterpreter(String input) {
        this.input = input
    }

    @Override
    void run() {
        def parser = Builder.build(input)
        def node = parser.parse()
        println("Returned: " + node)
    }

}
