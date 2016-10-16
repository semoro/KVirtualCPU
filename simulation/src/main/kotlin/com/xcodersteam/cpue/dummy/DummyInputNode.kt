package com.xcodersteam.cpue.dummy

import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.simulation.Node

class DummyInputNode internal constructor() : Node() {
    typealias StateListener = DummyInputNode.() -> Unit
    var prevState = false
    var stateListener: StateListener? = null

    inline fun edgeFront(stateListener: StateListener) {
        if (!prevState && isPowered)
            stateListener()
    }

    inline fun edgeFall(stateListener: StateListener) {
        if (prevState && !isPowered)
            stateListener()
    }

}

fun diNode(): DummyInputNode {
    return DummyInputNode().apply {
        Simulation.registerNode(this)
        Simulation.postUpdate += {
            stateListener?.invoke(this)
            if (prevState != isPowered) {
                prevState = isPowered
            }
        }
    }
}