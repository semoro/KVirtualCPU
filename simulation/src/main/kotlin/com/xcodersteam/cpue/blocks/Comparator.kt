package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation.node

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */
//6t
class EqualityComparator(val bits: Int) {

    val xnors = Array(bits, { XNORGate() })
    val outAnd = MultiANDGate(bits)

    val a = ArrayBasedBus(xnors.map { it.a }.toTypedArray())
    val b = ArrayBasedBus(xnors.map { it.b }.toTypedArray())

    val out = outAnd.output

    init {
        xnors.map { it.c }.forEachIndexed { i, node -> outAnd.input.nodes[i].link(node) }
    }
}


//Max timing: 8t
//Equality check: 6t
//Less check: 3t
//Greater check: 8t
class Comparator(val bits: Int) {

    val equalCmp = EqualityComparator(bits)

    val nots = Array(bits, { NotGate() })
    val andGates = Array(bits, {
        MultiANDGate(2 + (bits - it - 1))
    })

    val a = ArrayBasedBus(nots.map { it.a }.toTypedArray())
    val b = ArrayBasedBus(andGates.map { it.input.nodes[1] }.toTypedArray())

    val norGt = NORGate()

    val lesser = node()
    val equal = equalCmp.out
    val greater = norGt.c

    val orGt = ORGate()
    val greaterEqual = orGt.c

    val orLq = ORGate()
    val lesserEqual = orLq.c

    init {
        andGates.forEachIndexed { i, gate ->
            gate.input.nodes[0].link(nots[i].b)
        }
        equalCmp.a.link(a)
        equalCmp.b.link(b)
        val xS = equalCmp.xnors.map { it.c }
        andGates.forEachIndexed { i, gate ->
            for (j in 0..(bits - i - 2))
                gate.input.nodes[2 + j].link(xS[i + j + 1])
        }
        andGates.map { it.output }.forEach { it.link(lesser) }

        norGt.a.link(lesser)
        norGt.b.link(equal)

        orGt.a.link(equal)
        orGt.b.link(greater)

        orLq.a.link(equal)
        orLq.b.link(lesser)
    }
}
