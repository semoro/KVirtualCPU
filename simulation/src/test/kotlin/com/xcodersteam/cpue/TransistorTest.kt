package com.xcodersteam.cpue

import com.xcodersteam.cpue.Simulation.transistor
import com.xcodersteam.cpue.blocks.truthTable
import com.xcodersteam.cpue.simulation.Transistor
import org.junit.Test

class TransistorTest : AbstractSimulationTest() {

    @Test
    fun testNChannel() {
        val t = transistor(Transistor.SiliconType.N)
        truthTable(2, 1,
                0b00 to 0b0,
                0b01 to 0b0,
                0b10 to 0b0,
                0b11 to 0b1
        ).setup {
            t.source.link(inputs.nodes[0])
            t.gate.link(inputs.nodes[1])
            t.drain.link(outputs.nodes[0])
        }.test(this, 3)
    }

    @Test
    fun testPChannel() {
        val t = transistor(Transistor.SiliconType.P)
        truthTable(2, 1,
                0b00 to 0b0,
                0b01 to 0b0,
                0b10 to 0b1,
                0b11 to 0b0
        ).setup {
            t.source.link(inputs.nodes[0])
            t.gate.link(inputs.nodes[1])
            t.drain.link(outputs.nodes[0])
        }.test(this, 3)
    }
}