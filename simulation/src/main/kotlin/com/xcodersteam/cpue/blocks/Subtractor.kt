package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation.refNode

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */

class HalfSubtractor() { //Stable run on 4 ticks
    val x = refNode()
    val y = refNode()

    val d = refNode()
    val b = refNode()

    init {
        d.link(x xor y)
        b.link(not(x) and y)
    }
}

class FullSubtractor() { //Stable run on 7 ticks
    private val sub1 = HalfSubtractor()
    private val sub2 = HalfSubtractor()

    val x1 = refNode()
    val x2 = refNode()
    val bIn = refNode()

    val d = refNode()
    val bOut = refNode()

    init {
        x1.link(sub1.x)
        x2.link(sub1.y)

        sub2.x.link(sub1.d)
        bIn.link(sub2.y)

        d.link(sub2.d)

        bOut.link(sub1.b or sub2.b)
    }
}

class Subtractor(val bits: Int) { //Stable run on ticks bits*2 + 1
    val firstSub = HalfSubtractor()
    val subtractors = Array(bits - 1, { FullSubtractor() })
    val x1 = ArrayBasedBus(arrayOf(firstSub.x, *subtractors.map { it.x1 }.toTypedArray()))
    val x2 = ArrayBasedBus(arrayOf(firstSub.y, *subtractors.map { it.x2 }.toTypedArray()))
    val s = ArrayBasedBus(arrayOf(firstSub.d, *subtractors.map { it.d }.toTypedArray()))

    init {
        var bIn = firstSub.b
        subtractors.forEach {
            it.bIn.link(bIn)
            bIn = it.bOut
        }
    }
}