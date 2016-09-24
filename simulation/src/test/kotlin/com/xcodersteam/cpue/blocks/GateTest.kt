package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation.power
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
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
    fun testAndGate() {
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
    fun testOrGate() {
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
    fun testXorGate() {
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
    fun testXnorGate() {
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