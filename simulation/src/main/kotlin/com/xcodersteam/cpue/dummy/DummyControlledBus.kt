package com.xcodersteam.cpue.dummy

import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.blocks.AbstractBus
import com.xcodersteam.cpue.simulation.Node

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */

class DummyControlledBus(bits: Int) : AbstractBus(bits) {

    val triggers = Array(bits, { dct() })

    override val nodes: Array<Node> = triggers.map { it.drain }.toTypedArray()

    init {
        triggers.map { it.source }.forEach { it.link(Simulation.VCC) }
    }

    var stateBits: Int
        get() = triggers.map { it.triggerState }.map { if (it) 1 else 0 }.reduceIndexed { i, acc, v -> (v shl i) or acc }
        set(value) = generateSequence(value, { it shr 1 }).map { it and 1 == 1 }.take(this.bits).forEachIndexed { i, b -> triggers[i].triggerState = b }
}