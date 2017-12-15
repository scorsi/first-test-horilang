# Horilang
The Horilang Language

## What is it ?

Horilang has initially made for fun.

Horilang is a special language.
 - Variables are _statically_ typed
 - Types are full _object_
 - Still have _function_ outside object (because sometime object is too heavy for a light thing)
 
Horilang works with namespace corresponding to folders (equivalent to Java's package).

Everything is not accessible until you ``export`` it. Something exported is accessible everywhere, something not exported is accessible in the same namespace. 


## Why use it ?

If you want to use a fresh new full object language where you can decide of the visibility of everything.

## Example of code

```horilang
// Each files must start with module keyword
module test                 // The name of this module, must respect folders hierarchy

// The import keywords must be after module keyword and before everything else

import test.A               // We import A to be used in this file

import {                    // You can import multiple modules
    test.C
    test.D
}

// You can create import alias (equivalent to the line below + standard import)
import horilang.Integer as Integer
import horilang.String as String

// You can create typealias
typealias test.A as A

// This function can edit "left" value because it is a var not a val
func testFunc1 (var left : String, val right : String) : var Integer {
    return left += right
}

// If there is only one statement you can use the "->"
func testFunc2 (var left : String, val right : String) : var Integer ->
    return left += right
    
// The return value of the last statement is automatically returned
func testFunc3 (var left : String, val right : String) : val Integer ->
    left += right

// This function is only accessible in this file
func test1 () : var Integer -> 1

// This function is accessible everywhere
export func test2 () : var Integer -> 2

// You can choose between var and val for the returned value
export func test3 () : val Integer -> 3

// You can omite "()" if there isn't any parameters
export func test4 : var Integer -> 4

export type B extends A implements test.C   // B heriting from A (alias of test.A) and implement test.C
{
    /**
     * Note: in a type, first should be attributes, then constructor, then init, then destructor, then methods 
     */

    // Every var and val have to be assigned even in a type
    val name : String                       // Initialized in the constructor
    var counter : Integer                   // Initialized in the init func
    
    constructor (name : String) : A()       // The constructor have to call the parent constructor
    
    init -> counter = 0                     // The init function which is called automaticaly after the constructor
    
    /**
     * You can type the following if you don't like the multi "=":
     *
     * init {
     *     counter = 0
     * }
     */
    
    destructor {                            // Called automatically when no longer used
        print("Bye bye")
    }
    
    // Accessible only in this type
    func addToCounter (val toAdd : Integer) : val Integer ->
        counter + toAdd
    
    // This is a function getter
    // The returned value is const, so you cannot edit it
    export getter counter : val Integer ->
        counter
   
    // This is a function setter which take only one argument
    export setter counter (val newCounter : Integer) : val Integer ->
        counter = newCounter
    
    // You can do some polymorphism
    export setter counter (val newCounter : test.A) : val Integer ->
        counter = newCounter.counter
    
    // In all cases this func return counter attribute
    export func loopCounter (val nbTurn : Integer) : val Integer ->
        if (nbTurn > 0) ->
            for (var i : Integer = 0 to nbTurn) ->
                counter = addToCounter(1)
        else {                              // We can mix "->" body and "{}" body
            counter
        }
    
    // We override operator "++"
    export operator ++ (val toAdd : Integer) : val Integer ->
        counter += toAdd
}

// "main" function must respect this signature
func main (val args : String[]) : val Integer {
    var b : B = create B ("test")
    b.addToCounter(3)                       // Cannot do that because addToCounter is not exported
    print(b.loopCounter(5))                 // Should display 5
    b++; print(b.counter)                   // Should display 6
    
    /**
     * If you let this like that, the returned value will be 1
     * because print returned true Boolean
     */
    return 0                                // We decide to return 0 instead of 1
}
```