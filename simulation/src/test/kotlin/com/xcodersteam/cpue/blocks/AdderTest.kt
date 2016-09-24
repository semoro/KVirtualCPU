package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation.power
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */
class AdderTest : AbstractSimulationTest() {

    @Test
    fun testHalfAdder() {
        val adder = HalfAdder()
        simulateNSteps(10) {}
        assertFalse(adder.s.power)
        assertFalse(adder.c.power)

        simulateNSteps(4) {
            adder.x1.power = true
        }
        assertTrue(adder.s.power)
        assertFalse(adder.c.power)

        simulateNSteps(4) {
            adder.x2.power = true
        }
        assertTrue(adder.s.power)
        assertFalse(adder.c.power)

        simulateNSteps(4) {
            adder.x1.power = true
            adder.x2.power = true
        }
        assertFalse(adder.s.power)
        assertTrue(adder.c.power)
    }

    @Test
    fun testFullAdder() {
        val adder = FullAdder()
        simulateNSteps(10) {}
        assertFalse(adder.s.power)
        assertFalse(adder.cOut.power)

        simulateNSteps(7) {
            adder.x1.power = true
        }
        assertTrue(adder.s.power)
        assertFalse(adder.cOut.power)
        simulateNSteps(10) {}

        simulateNSteps(7) {
            adder.x2.power = true
        }
        assertTrue(adder.s.power)
        assertFalse(adder.cOut.power)
        simulateNSteps(10) {}

        simulateNSteps(7) {
            adder.x1.power = true
            adder.x2.power = true
        }
        assertFalse(adder.s.power)
        assertTrue(adder.cOut.power)
        simulateNSteps(10) {}

        simulateNSteps(7) {
            adder.x1.power = true
            adder.x2.power = true
            adder.cIn.power = true
        }
        assertTrue(adder.s.power)
        assertTrue(adder.cOut.power)
    }

    @Test
    fun testSummer() {
        val summer = Summer(8)
        for (i in 0..128)
            for (j in 0..128) {
                simulateNSteps(16) {
                    summer.x1.asBits = i
                    summer.x2.asBits = j
                }
                assertEquals((i + j) % (1 shl summer.bits), summer.s.asBits)
            }
    }
}