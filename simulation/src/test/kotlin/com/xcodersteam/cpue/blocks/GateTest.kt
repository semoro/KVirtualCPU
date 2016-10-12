package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.AbstractSimulationTest
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
        truthTable(1, 1,
                0b0 to 0b1,
                0b1 to 0b0
        ).setup {
            gate.a.link(inputs.nodes[0])
            gate.b.link(outputs.nodes[0])
        }.test(this, 3)
    }

    @Test
    fun testNORGate() {
        val gate = NORGate()
        truthTable(2, 1,
                0b00 to 0b1,
                0b01 to 0b0,
                0b10 to 0b0,
                0b11 to 0b0
        ).setup {
            gate.a.link(inputs.nodes[0])
            gate.b.link(inputs.nodes[1])
            gate.c.link(outputs.nodes[0])
        }.test(this, 10)
    }

    @Test
    fun testANDGate() {
        val gate = ANDGate()
        truthTable(2, 1,
                0b00 to 0b0,
                0b01 to 0b0,
                0b10 to 0b0,
                0b11 to 0b1
        ).setup {
            gate.a.link(inputs.nodes[0])
            gate.b.link(inputs.nodes[1])
            gate.c.link(outputs.nodes[0])
        }.test(this, 10)
    }

    @Test
    fun testMultiANDGate() {
        val gate = MultiANDGate(3)
        truthTable(3, 1,
                0b000 to 0b0,
                0b001 to 0b0,
                0b010 to 0b0,
                0b011 to 0b0,
                0b100 to 0b0,
                0b101 to 0b0,
                0b110 to 0b0,
                0b111 to 0b1
        ).setup {
            gate.input.link(inputs)
            gate.output.link(outputs.nodes[0])
        }.test(this, 10)
    }

    @Test
    fun testMultiORGate() {
        val gate = MultiORGate(3)
        truthTable(3, 1,
                0b000 to 0b0,
                0b001 to 0b1,
                0b010 to 0b1,
                0b011 to 0b1,
                0b100 to 0b1,
                0b101 to 0b1,
                0b110 to 0b1,
                0b111 to 0b1
        ).setup {
            gate.input.link(inputs)
            gate.output.link(outputs.nodes[0])
        }.test(this, 10)
    }

    @Test
    fun testORGate() {
        val gate = ORGate()
        truthTable(2, 1,
                0b00 to 0b0,
                0b01 to 0b1,
                0b10 to 0b1,
                0b11 to 0b1
        ).setup {
            gate.a.link(inputs.nodes[0])
            gate.b.link(inputs.nodes[1])
            gate.c.link(outputs.nodes[0])
        }.test(this, 10)
    }

    @Test
    fun testDiode() {
        val diode = TDiode()
        simulateNSteps(5) {
            diode.a.power = true
        }
        assertTrue(diode.b.power)

        simulateNSteps(10) {
            diode.b.power = true
        }
        assertFalse(diode.a.power)

    }

    @Test
    fun testXORGate() {
        val gate = XORGate()
        truthTable(2, 1,
                0b00 to 0b0,
                0b01 to 0b1,
                0b10 to 0b1,
                0b11 to 0b0
        ).setup {
            gate.a.link(inputs.nodes[0])
            gate.b.link(inputs.nodes[1])
            gate.c.link(outputs.nodes[0])
        }.test(this, 10)
    }

    @Test
    fun testXNORGate() {
        val gate = XNORGate()
        truthTable(2, 1,
                0b00 to 0b1,
                0b01 to 0b0,
                0b10 to 0b0,
                0b11 to 0b1
        ).setup {
            gate.a.link(inputs.nodes[0])
            gate.b.link(inputs.nodes[1])
            gate.c.link(outputs.nodes[0])
        }.test(this, 10)
    }

    @Test
    fun testRSLatch() {
        val gate = RSLatch()
        truthTable(2, 1,
                0b00 to 0b0,
                0b00 to 0b0,
                0b10 to 0b1,
                0b00 to 0b1,
                0b01 to 0b0,
                0b00 to 0b0
        ).setup {
            gate.s.link(inputs.nodes[0])
            gate.r.link(inputs.nodes[1])
            gate.q.link(outputs.nodes[0])
        }.test(this, 10)
    }
}