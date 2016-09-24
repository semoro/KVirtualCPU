package com.xcodersteam.cpue.blocks

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */

class HalfAdder() { //Stable run at 4 ticks switch duty cycle
    val xor = XORGate()
    val and = ANDGate()

    val x1 = xor.a
    val x2 = xor.b

    val s = xor.c
    val c = and.c

    init {
        xor.a.link(and.a)
        xor.b.link(and.b)
    }
}

class FullAdder() { //Stable run at 7 ticks switch duty cycle
    val adder1 = HalfAdder()
    val adder2 = HalfAdder()
    val or = ORGate()

    val x1 = adder1.x1
    val x2 = adder1.x2
    val cIn = adder2.x2

    val s = adder2.s
    val cOut = or.c

    init {
        adder2.x1.link(adder1.s)
        or.a.link(adder1.c)
        or.b.link(adder2.c)
    }
}

class Summer(val bits: Int) { //Stable run at bits*2 ticks
    val firstAdder = HalfAdder()
    val adders = Array(bits - 1, { FullAdder() })
    val x1 = ArrayBasedBus(arrayOf(firstAdder.x1, *adders.map { it.x1 }.toTypedArray()))
    val x2 = ArrayBasedBus(arrayOf(firstAdder.x2, *adders.map { it.x2 }.toTypedArray()))
    val s = ArrayBasedBus(arrayOf(firstAdder.s, *adders.map { it.s }.toTypedArray()))

    init {
        var cIn = firstAdder.c
        adders.forEach {
            it.cIn.link(cIn)
            cIn = it.cOut
        }
    }
}