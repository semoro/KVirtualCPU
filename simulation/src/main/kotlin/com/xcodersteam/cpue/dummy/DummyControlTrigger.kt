package com.xcodersteam.cpue.dummy

import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.simulation.Node

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
class DummyControlTrigger {

    var triggerState = false

    open class DCTNode : Node() {

    }

    val source: Node = object : DCTNode() {
        override fun onPowerChange(from: Node?, state: Boolean) {
            if (triggerState) {
                drain.notifyStateChange(this, state)
            }
            super.onPowerChange(from, state)
        }
    }

    val drain: Node = object : DCTNode() {
        override fun onPowerChange(from: Node?, state: Boolean) {
            if (triggerState) {
                source.notifyStateChange(this, state)
            }
            super.onPowerChange(from, state)
        }
    }

    val nodes = arrayOf(source, drain)

}

fun dct(): DummyControlTrigger {
    val dct = DummyControlTrigger()
    dct.nodes.forEach { Simulation.registerNode(it) }
    return dct
}