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

## Example of parsing

If you type ```var a = 3; a = 3;```, you will obtain the followng output:

```
Getting: Token(type=VAR, value=var)
Peeking: Token(type=VAR, value=var) at 1
Getting: Token(type=SYMBOL, value=a)
Peeking: Token(type=SYMBOL, value=a) at 2
Getting: Token(type=ASSIGN, value==)
Peeking: Token(type=ASSIGN, value==) at 3
Getting: Token(type=INTEGER, value=3)
Peeking: Token(type=INTEGER, value=3) at 4
Getting: Token(type=EOI, value=;)
Peeking: Token(type=EOI, value=;) at 5
Purging: [Token(type=VAR, value=var), Token(type=SYMBOL, value=a), Token(type=ASSIGN, value==), Token(type=INTEGER, value=3)]
Remaining: [Token(type=EOI, value=;)]
Creating: DeclarationNode(symbol=a, type=Integer, value=3)
Peeking: Token(type=EOI, value=;) at 1
Purging: [Token(type=EOI, value=;)]
Remaining: []
Getting: Token(type=SYMBOL, value=a)
Peeking: Token(type=SYMBOL, value=a) at 1
Getting: Token(type=ASSIGN, value==)
Peeking: Token(type=ASSIGN, value==) at 2
Getting: Token(type=INTEGER, value=3)
Peeking: Token(type=INTEGER, value=3) at 3
Getting: Token(type=EOI, value=;)
Peeking: Token(type=EOI, value=;) at 4
Purging: [Token(type=SYMBOL, value=a), Token(type=ASSIGN, value==), Token(type=INTEGER, value=3)]
Remaining: [Token(type=EOI, value=;)]
Creating: AssignmentNode(leftValue=a, rightValue=3, rightValueType=Integer)
Peeking: Token(type=EOI, value=;) at 1
Purging: [Token(type=EOI, value=;)]
Remaining: []
Getting: Token(type=EOF, value=)
Returned: [DeclarationNode(symbol=a, type=Integer, value=3), AssignmentNode(leftValue=a, rightValue=3, rightValueType=Integer)]
Remaining: []
```

Only the ```Returned``` value is important here. So you get the following tree:

```[DeclarationNode(symbol=a, type=Integer, value=3), AssignmentNode(leftValue=a, rightValue=3, rightValueType=Integer)]```

Which correspond to ```var a = 3; a = 3;```.
