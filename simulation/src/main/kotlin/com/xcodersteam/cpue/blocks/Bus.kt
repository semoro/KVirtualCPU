package com.xcodersteam.cpue.blocks

import com.sun.javaws.exceptions.InvalidArgumentException
import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.Simulation.node
import com.xcodersteam.cpue.Simulation.power
import com.xcodersteam.cpue.Simulation.transistor
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
    val transistorAtoB = transistor(SiliconType.N)
    val transistorBtoA = transistor(SiliconType.N)
    val diodeAtoB = TDiode()
    val diodeBtoA = TDiode()

    val a = transistorAtoB.gate
    val b = transistorBtoA.gate

    val aToB = diodeAtoB.a
    val bToA = diodeBtoA.a

    init {
        transistorAtoB.drain.link(b)
        transistorAtoB.source.link(diodeAtoB.b)

        transistorBtoA.drain.link(a)
        transistorBtoA.source.link(diodeBtoA.b)
    }
}

class BiBusCommutator(bits: Int) {

    val commutators = Array(bits, { SingleBiCommutator() })

    val busA = ArrayBasedBus(commutators.map { it.a }.toTypedArray())
    val busB = ArrayBasedBus(commutators.map { it.b }.toTypedArray())

    val dirA = node()
    val dirB = node()

    init {
        commutators.map { it.aToB }.forEach { it.link(dirA) }
        commutators.map { it.bToA }.forEach { it.link(dirB) }
    }
}