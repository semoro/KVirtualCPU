package com.xcodersteam.cpue.blocks

import com.sun.javaws.exceptions.InvalidArgumentException
import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.Simulation.power
import com.xcodersteam.cpue.Simulation.refNode
import com.xcodersteam.cpue.simulation.Node
import com.xcodersteam.cpue.simulation.Transistor.SiliconType

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

    fun link(other: AbstractBus, offset: Int) {
        if ((bits + offset) > other.bits)
            throw InvalidArgumentException(arrayOf("connector out of bounds"))
        nodes.forEachIndexed { i, node -> other.nodes[i + offset].link(node) }
    }

    var asBits: Int
        get() = nodes.map(Node::isPowered).map { if (it) 1 else 0 }.reduceIndexed { i, acc, v -> (v shl i) or acc }
        set(value) = generateSequence(value, { it shr 1 }).map { it and 1 == 1 }.take(this.bits).forEachIndexed { i, b -> nodes[i].power = b }

    var asLongBits: Long
        get() = nodes.map(Node::isPowered).map { if (it) 1L else 0L }.reduceIndexed { i, acc, v -> (v shl i) or acc }
        set(value) = generateSequence(value, { it shr 1 }).map { it and 1L == 1L }.take(this.bits).forEachIndexed { i, b -> nodes[i].power = b }
}

class ArrayBasedBus(override val nodes: Array<Node>) : AbstractBus(nodes.size) {}

fun NodesBus(bits: Int): ArrayBasedBus {
    return ArrayBasedBus(Array(bits, { Simulation.node() }))
}

fun RefNodesBus(bits: Int): ArrayBasedBus {
    return ArrayBasedBus(Array(bits, { Simulation.refNode() }))
}

class BusCommutator(bits: Int) {

    val transistors = Array(bits, { i ->
        Simulation.transistor(SiliconType.N)
    })

    val enable = Simulation.node()
    val busA = ArrayBasedBus(transistors.map { it.source }.toTypedArray())
    val busB = ArrayBasedBus(transistors.map { it.drain }.toTypedArray())

    init {
        transistors.forEach { it.gate.link(enable) }
    }
}

class SingleBiCommutator() {

    val a = refNode()
    val b = refNode()

    val aToB = refNode()
    val bToA = refNode()

    init {
        b.link(diode(aToB) uand a)
        a.link(diode(bToA) uand b)
    }
}

class BiBusCommutator(bits: Int) {

    private val commutators = Array(bits, { SingleBiCommutator() })

    val busA = ArrayBasedBus(commutators.map { it.a }.toTypedArray())
    val busB = ArrayBasedBus(commutators.map { it.b }.toTypedArray())

    val dirA = refNode()
    val dirB = refNode()

    init {
        commutators.map { it.aToB }.forEach { it.link(dirA) }
        commutators.map { it.bToA }.forEach { it.link(dirB) }
    }
}