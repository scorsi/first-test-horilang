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
module "test"               // The name of this module, must respect folders hierarchy

// The imports keyword must be after module keyword and before everything else
import "testA"              // We import A to be used in this file

import (                    // You can import multiple modules
    "testB"                 
    "testC"
)

typealias A = testA.A       // You can create aliases

// Private function which edit "left" value because it is a var not a val
func testFunc1 (var left, val right) : var Integer {
    return left += right
}

// If there is only one statement you can use the "="
func testFunc2 (var left, val right) : var Integer =
    return left += right
    
// The return value of the last statement is automatically returned
func testFunc3 (var left, val right) : val Integer =
    left += right

// This function is only accessible in this file
func test1 () : var Integer = 1

// This function is only accessible in this module
local func test2 () : var Integer = 2

// This function is accessible everywhere
global func test3 () : var Integer = 3

// You can choose between var and val for the returned value
global func test4 () : val Integer = 4

// You can omite "()" if there isn't any parameters
global func test5 : var Integer = 5

global type B : A                    // B heriting from A (alias of testA.A)
{
    // var and val have to be assigned !
    val name { export get } : String        // Inited in the constructor
    var counter : Integer                   // Inited in the init func
    
    constructor (name : String) : A()       // The constructor which call the parent constructor
    
    init = counter = 0                      // The init function which is called automaticaly after the constructor
    
    destructor {                            // Called automatically when no longer used
        print("Bye bye")
    }
    
    /**
     * You can type the following if you don't like the multi "=":
     *
    init {
        counter = 0
    }
     */
    
    // Private equivalent access to this type
    func addToCounter (val toAdd : Integer) : val Integer =
        counter + toAdd
        
    // An alias is created for counter attribute, see bellow for an example
    // The returned value is const, so you cannot edit it
    global func getCounter : val Integer =
        counter
   
    // An alias is created for the assignment of counter attribute
    global func setCounter (val newCounter : Integer) : val Integer =
        counter = newCounter
        
    // In all cases this func return counter attribute
    global func loopCounter (val nbTurn : Integer) : val Integer =
        if (nbTurn > 0) =
            for (var i : Integer = 0 to nbTurn) =
                counter = addToCounter(1)
        else {                              // We can mix "=" body and "{}" body
            counter
        }
    
    // We override operator "++"
    // Operators access are the same as their type
    operator ++ (val toAdd : Integer) : val Integer =
        counter += toAdd
}

// No "func" keyword because main is the keyword
// main must return Integer
main(val args : String[]) {
    var b : B = B("test")
    b.addToCounter(3)                       // Cannot do that because addToCounter is not exported
    print(b.loopCounter(5))                 // Should display 5
    b++; print(b.counter)                   // Should display 6
                                            // Note the uses of "b.counter" which will call "b.getCounter()"
    // If you let this like that, the returned value will be 1 because print returned true
    0                                       // which return 0
}
```