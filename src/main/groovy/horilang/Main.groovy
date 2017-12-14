package horilang

class Main {

    static void main(String[] args) {
        def input = new Scanner(System.in)
        while (input.hasNext()) {
            new Thread(new LiveHorilangInterpreter(input.nextLine())).start()
        }
    }

}
