package com.scorsi.horilang.ast

import com.scorsi.horilang.Token

abstract class Node {

    abstract fun build(tokens: List<Token>): Node

}