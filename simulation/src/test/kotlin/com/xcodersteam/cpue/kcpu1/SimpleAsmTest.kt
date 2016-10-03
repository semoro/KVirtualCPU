package com.xcodersteam.cpue.kcpu1

import junit.framework.TestCase.assertEquals
import org.junit.Test

/**
 * Created by Semoro on 03.10.16.
 * ©XCodersTeam, 2016
 */

class SimpleAsmTest : AbstractAsmTest() {

    @Test
    fun testJump() {
        asm {
            val start = label()
            jmp(start)
        }.writeInto(rom.data)
        simulateCycles(10)
        assertEquals(0x0, kcpu.programCounter.data.outBus.asBits)
    }

    @Test
    fun testALU() {
        asm {
            val start = label()
            inalu(15, 10)
            nop()
            nop()
            nop()
            jmp(start)
        }.writeInto(rom.data)
        simulateCycles(5)
        assertEquals(25, kcpu.alu.sum.internalValueBus.asBits)
        assertEquals(5, kcpu.alu.sub.internalValueBus.asBits)

        assertEquals(0, kcpu.alu.eq.internalValueBus.asBits)
        assertEquals(1, kcpu.alu.gt.internalValueBus.asBits)
        assertEquals(0, kcpu.alu.lt.internalValueBus.asBits)

        assertEquals(1, kcpu.alu.gteq.internalValueBus.asBits)
        assertEquals(0, kcpu.alu.lteq.internalValueBus.asBits)
    }
}