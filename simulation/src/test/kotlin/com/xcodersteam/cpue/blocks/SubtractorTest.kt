package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.AbstractSimulationTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */
class SubtractorTest : AbstractSimulationTest() {
    @Test
    fun testHalfSubtractor() {

        truthTable(2, 2,
                0b00 to 0b00,
                0b01 to 0b11,
                0b10 to 0b10,
                0b11 to 0b00
        ).setup {
            val subtractor = HalfSubtractor()
            subtractor.x.link(inputs.nodes[0])
            subtractor.y.link(inputs.nodes[1])
            subtractor.d.link(outputs.nodes[0])
            subtractor.b.link(outputs.nodes[1])
        }.test(this, 10)



    }

    @Test
    fun testFullSubtractor() {
        val subtractor = FullSubtractor()
        truthTable(3, 2,
                0b000 to 0b00,
                0b001 to 0b11,
                0b010 to 0b11,
                0b011 to 0b01,
                0b100 to 0b10,
                0b101 to 0b00,
                0b110 to 0b00,
                0b111 to 0b11
        ).setup {
            subtractor.x1.link(inputs.nodes[0])
            subtractor.x2.link(inputs.nodes[1])
            subtractor.bIn.link(inputs.nodes[2])

            subtractor.d.link(outputs.nodes[0])
            subtractor.bOut.link(outputs.nodes[1])
        }.test(this, 7)

    }

    @Test
    fun testSubtractor() {
        val subtractor = Subtractor(8)
        for (i in 0..128)
            for (j in 0..128) {
                simulateNSteps(17) {
                    subtractor.x1.asBits = i
                    subtractor.x2.asBits = j
                }
                assertEquals((i - j) and ((1 shl subtractor.bits) - 1), subtractor.s.asBits)
            }
    }
}