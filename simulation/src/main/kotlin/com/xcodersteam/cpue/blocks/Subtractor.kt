package com.xcodersteam.cpue.blocks

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */

class HalfSubtractor() { //Stable run on 4 ticks
    val xor = XORGate()
    val not = NotGate()
    val and = ANDGate()

    val x1 = xor.a
    val x2 = xor.b

    val d = xor.c
    val bOut = and.c

    init {
        xor.a.link(not.a)
        xor.b.link(and.b)
        and.a.link(not.b)
    }
}

class FullSubtractor() { //Stable run on 7 ticks
    val sub1 = HalfSubtractor()
    val sub2 = HalfSubtractor()
    val or = ORGate()

    val x1 = sub1.x1
    val x2 = sub1.x2
    val bIn = sub2.x2

    val d = sub2.d
    val bOut = or.c

    init {
        sub2.x1.link(sub1.d)
        or.a.link(sub1.bOut)
        or.b.link(sub2.bOut)
    }
}

class Subtractor(val bits: Int) { //Stable run on ticks bits*2 + 1
    val firstSub = HalfSubtractor()
    val subtractors = Array(bits - 1, { FullSubtractor() })
    val x1 = ArrayBasedBus(arrayOf(firstSub.x1, *subtractors.map { it.x1 }.toTypedArray()))
    val x2 = ArrayBasedBus(arrayOf(firstSub.x2, *subtractors.map { it.x2 }.toTypedArray()))
    val s = ArrayBasedBus(arrayOf(firstSub.d, *subtractors.map { it.d }.toTypedArray()))

    init {
        var bIn = firstSub.bOut
        subtractors.forEach {
            it.bIn.link(bIn)
            bIn = it.bOut
        }
    }
}