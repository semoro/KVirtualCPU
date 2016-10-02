package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.AbstractSimulationTest
import com.xcodersteam.cpue.Simulation.power
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */
class SubtractorTest : AbstractSimulationTest() {
    @Test
    fun testHalfSubtractor() {

        val truthTable = truthTable(
                arrayOf(0, 0) to arrayOf(0, 0),
                arrayOf(1, 0) to arrayOf(1, 0),
                arrayOf(0, 1) to arrayOf(1, 1),
                arrayOf(1, 1) to arrayOf(0, 0)
        )

        val subtractor = HalfSubtractor()
        simulateNSteps(10) {}
        truthTable.test {
            simulateNSteps(4) {
                subtractor.x1.power = inputs[0]
                subtractor.x2.power = inputs[1]
            }
            assertEquals(outputs[0], subtractor.d.power)
            assertEquals(outputs[1], subtractor.bOut.power)
        }

    }

    @Test
    fun testFullSubtractor() {
        val truthTable = truthTable(
                arrayOf(0, 0, 0) to arrayOf(0, 0),
                arrayOf(0, 0, 1) to arrayOf(1, 0),
                arrayOf(0, 1, 0) to arrayOf(1, 1),
                arrayOf(0, 1, 1) to arrayOf(0, 0),
                arrayOf(1, 0, 0) to arrayOf(1, 1),
                arrayOf(1, 0, 1) to arrayOf(0, 0),
                arrayOf(1, 1, 0) to arrayOf(0, 1),
                arrayOf(1, 1, 1) to arrayOf(1, 1)
        )

        val subtractor = FullSubtractor()
        simulateNSteps(10) {}

        truthTable.test {
            simulateNSteps(7) {
                subtractor.x1.power = inputs[2]
                subtractor.x2.power = inputs[1]
                subtractor.bIn.power = inputs[0]
            }
            assertEquals(outputs[0], subtractor.d.power)
            assertEquals(outputs[1], subtractor.bOut.power)
        }
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