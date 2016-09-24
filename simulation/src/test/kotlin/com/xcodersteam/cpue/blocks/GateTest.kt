package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation.power
import junit.framework.Assert.*
import org.junit.Test

/**
 * Created by Semoro on 23.09.16.
 * ©XCodersTeam, 2016
 */

class GateTest : AbstractSimulationTest() {

    @Test
    fun testNotGate() {
        val gate = NotGate()
        simulateNSteps(10) {
            gate.a.power = true
        }
        assertFalse(gate.b.power)
        simulateNSteps(10) {
            gate.a.power = false
        }
        assertTrue(gate.b.power)
    }

    @Test
    fun testNORGate() {
        val gate = NORGate()
        val t = truthTable(
                arrayOf(0, 0) to arrayOf(1),
                arrayOf(0, 1) to arrayOf(0),
                arrayOf(1, 0) to arrayOf(0),
                arrayOf(1, 1) to arrayOf(0)
        )
        t.test {
            simulateNSteps(3) {
                gate.a.power = inputs[0]
                gate.b.power = inputs[1]
            }
            assertEquals(outputs[0], gate.c.power)
        }
    }

    @Test
    fun testANDGate() {
        val gate = ANDGate()
        simulateNSteps(10) {
            gate.a.power = true
        }
        assertFalse(gate.c.power)
        simulateNSteps(10) {
            gate.a.power = true
            gate.b.power = true
        }
        assertTrue(gate.c.power)
        simulateNSteps(10) {
            gate.b.power = true
        }
        assertFalse(gate.c.power)
    }

    @Test
    fun testORGate() {
        val gate = ORGate()
        simulateNSteps(10) {
            gate.a.power = true
        }
        assertTrue(gate.c.power)
        simulateNSteps(10) {
            gate.a.power = true
            gate.b.power = true
        }
        assertTrue(gate.c.power)
        simulateNSteps(10) {
            gate.b.power = true
        }
        assertTrue(gate.c.power)
        simulateNSteps(10) {}
        assertFalse(gate.c.power)
    }

    @Test
    fun testXORGate() {
        val gate = XORGate()
        simulateNSteps(10) {
            gate.a.power = true
        }
        assertTrue(gate.c.power)
        simulateNSteps(10) {
            gate.a.power = true
            gate.b.power = true
        }
        assertFalse(gate.c.power)
        simulateNSteps(10) {
            gate.b.power = true
        }
        assertTrue(gate.c.power)
        simulateNSteps(10) {}
        assertFalse(gate.c.power)
    }

    @Test
    fun testXNORGate() {
        val gate = XNORGate()
        simulateNSteps(10) {
            gate.a.power = true
        }
        assertFalse(gate.c.power)
        simulateNSteps(10) {
            gate.a.power = true
            gate.b.power = true
        }
        assertTrue(gate.c.power)
        simulateNSteps(10) {
            gate.b.power = true
        }
        assertFalse(gate.c.power)
        simulateNSteps(10) {}
        assertTrue(gate.c.power)
    }

    @Test
    fun testRSLatch() {
        val gate = RSLatch()
        simulateNSteps(10) {}
        assertFalse(gate.q.power)
        simulateNSteps(10) {
            gate.s.power = true
        }
        assertTrue(gate.q.power)
        simulateNSteps(10) {}
        assertTrue(gate.q.power)
        simulateNSteps(10) {
            gate.r.power = true
        }
        assertFalse(gate.q.power)
    }
}