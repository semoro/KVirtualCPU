package com.xcodersteam.cpue.simulation

import sun.plugin.dom.exception.InvalidStateException

open class RefNode : Node() {
    var ref: Node? = null

    override var isPowered: Boolean
        get() = ref!!.isPowered
        set(value) {
            throw InvalidStateException("Do not set isPowered for ref node!")
        }

    override fun notifyStateChange(from: Node?) {
        if (from != null)
            throw InvalidStateException("No ref in RefNode")
        ref!!.notifyStateChange(null)
    }

    override fun link(n2: Node) {
        if (ref == null)
            ref = n2
        else
            ref!!.link(n2)
    }
}