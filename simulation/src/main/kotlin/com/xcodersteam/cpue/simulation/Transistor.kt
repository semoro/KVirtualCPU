package com.xcodersteam.cpue.simulation

/**
 * Created by semoro on 30.07.16.
 */
class Transistor(val channelType: SiliconType) {

    enum class SiliconType {
        N, P
    }

    open class TransistorNode(val transistor: Transistor) : Node() {

    }

    var channelOpen = channelType == SiliconType.P

    val gate: Node = TransistorNode(this)

    val source: Node = object : TransistorNode(this) {
        override fun onPowerChange(from: Node?) {
            if (channelOpen) {
                drain.notifyStateChange(this)
            }
            super.onPowerChange(from)
        }
    }

    val drain: Node = object : TransistorNode(this) {
        override fun onPowerChange(from: Node?) {
            if (channelOpen) {
                source.notifyStateChange(this)
            }
            super.onPowerChange(from)
        }
    }

    fun update() {
        if (gate.isPowered)
            channelOpen = channelType == SiliconType.N
        else
            channelOpen = channelType == SiliconType.P
    }


    fun reset() {
        channelOpen = channelType == SiliconType.P
    }

    val nodes = arrayOf(gate, source, drain)
}