package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation.refNode

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


//Max timing: 9t
//Equality check: 6t
//Lesser check: 3t
//Lesser equal check: 7t
//Greater check: 8t
//Greater equal check: 9t
class Comparator(val bits: Int) {

    val equalCmp = EqualityComparator(bits)

    val nots = Array(bits, { NotGate() })
    val andGates = Array(bits, {
        MultiANDGate(2 + (bits - it - 1))
    })

    val a = ArrayBasedBus(nots.map { it.a }.toTypedArray())
    val b = ArrayBasedBus(andGates.map { it.input.nodes[1] }.toTypedArray())

    val lesser = refNode()
    val equal = equalCmp.out
    val greater = refNode()

    val greaterEqual = refNode()
    val lesserEqual = refNode()

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


        lesserEqual.link(lesser or equal)
        greater.link(not(lesserEqual))
        greaterEqual.link(greater or equal)
    }
}
