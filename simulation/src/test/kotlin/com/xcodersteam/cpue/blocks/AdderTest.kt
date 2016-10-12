package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.AbstractSimulationTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */
class AdderTest : AbstractSimulationTest() {

    @Test
    fun testHalfAdder() {
        val adder = HalfAdder()

        truthTable(2, 2,
                0b00 to 0b00,
                0b10 to 0b01,
                0b01 to 0b01,
                0b11 to 0b10
        ).setup {
            adder.a.link(inputs.nodes[0])
            adder.b.link(inputs.nodes[1])
            adder.c.link(outputs.nodes[0])
            adder.s.link(outputs.nodes[1])
        }.test(this, 10)
    }

    @Test
    fun testFullAdder() {
        val adder = FullAdder()
        truthTable(3, 2,
                0b000 to 0b00,
                0b001 to 0b01,
                0b010 to 0b01,
                0b011 to 0b10,
                0b100 to 0b01,
                0b101 to 0b10,
                0b110 to 0b10,
                0b111 to 0b11
        ).setup {
            adder.a.link(inputs.nodes[0])
            adder.b.link(inputs.nodes[1])
            adder.cIn.link(inputs.nodes[2])
            adder.cOut.link(outputs.nodes[0])
            adder.s.link(outputs.nodes[1])
        }.test(this, 10)
    }

    @Test
    fun testSummer() {
        val summer = Summer(8)
        logicAnalyzer.module("summer") {
            bus(summer.x1, "a")
            bus(summer.x2, "b")
            bus(summer.s, "s")
        }

        for (i in 0..128)
            for (j in 0..128) {
                simulateNSteps(16) {
                    summer.x1.asBits = i
                    summer.x2.asBits = j
                }
                collector.checkThat(summer.s.asBits, equalTo((i + j) % (1 shl summer.bits)), "$i + $j")
            }
    }
}