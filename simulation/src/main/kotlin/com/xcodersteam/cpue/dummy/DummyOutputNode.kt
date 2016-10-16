package com.xcodersteam.cpue.dummy

import com.xcodersteam.cpue.Simulation.VCC
import com.xcodersteam.cpue.simulation.RefNode

class DummyOutputNode : RefNode() {

    val trigger = dct()
    var state: Boolean
        get() = trigger.triggerState
        set(value) {
            trigger.triggerState = value
        }

    init {
        trigger.source.link(VCC)
        trigger.drain.link(this)
    }
}
