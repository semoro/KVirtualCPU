package com.xcodersteam.cpue.blocks

import com.sun.javaws.exceptions.InvalidArgumentException
import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.Simulation.power
import com.xcodersteam.cpue.simulation.Node

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */
abstract class AbstractBus(val bits: Int) {
    abstract val nodes: Array<Node>
    fun link(other: AbstractBus) {
        if (bits != other.bits)
            throw InvalidArgumentException(arrayOf("bits number not match"))
        nodes.forEachIndexed { i, node -> other.nodes[i].link(node) }
    }
}

class ArrayBasedBus(override val nodes: Array<Node>) : AbstractBus(nodes.size) {}

fun NodesBus(bits: Int): ArrayBasedBus {
    return ArrayBasedBus(Array(bits, { Simulation.node() }))
}

var AbstractBus.asBits: Int
    get() = nodes.map(Node::isPowered).map { if (it) 1 else 0 }.reduceIndexed { i, acc, v -> (v shl i) or acc }
    set(value) = generateSequence(value, { it shr 1 }).map { it and 1 == 1 }.take(this.bits).forEachIndexed { i, b -> nodes[i].power = b }

