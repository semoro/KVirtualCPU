package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation.refNode

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */

class HalfAdder() { //Stable run at 4 ticks switch duty cycle

    val a = refNode()
    val b = refNode()

    val s = refNode()
    val c = refNode()

    init {
        s.link(a xor b)
        c.link(a and b)
    }
}

class FullAdder() { //Stable run at 7 ticks switch duty cycle
    private val adder1 = HalfAdder()
    private val adder2 = HalfAdder()

    val a = refNode()
    val b = refNode()
    val cIn = refNode()

    val s = refNode()
    val cOut = refNode()

    init {
        a.link(adder1.a)
        b.link(adder1.b)

        adder2.a.link(adder1.s)
        cIn.link(adder2.b)

        s.link(adder2.s)
        cOut.link(adder1.c or adder2.c)
    }
}

class Summer(val bits: Int) { //Stable run at bits*2 ticks
    val firstAdder = HalfAdder()
    val adders = Array(bits - 1, { FullAdder() })
    val x1 = ArrayBasedBus(arrayOf(firstAdder.a, *adders.map { it.a }.toTypedArray()))
    val x2 = ArrayBasedBus(arrayOf(firstAdder.b, *adders.map { it.b }.toTypedArray()))
    val s = ArrayBasedBus(arrayOf(firstAdder.s, *adders.map { it.s }.toTypedArray()))

    init {
        var cIn = firstAdder.c
        adders.forEach {
            it.cIn.link(cIn)
            cIn = it.cOut
        }
    }
}