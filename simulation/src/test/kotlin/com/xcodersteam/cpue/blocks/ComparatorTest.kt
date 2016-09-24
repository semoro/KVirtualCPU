package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation.power
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */

class ComparatorTest : AbstractSimulationTest() {


    @Test
    fun testPureEqualityComparator() {
        val cmp = EqualityComparator(8)
        for (i in 0..255) {
            for (j in 0..255) {
                simulateNSteps(6) {
                    cmp.a.asBits = i
                    cmp.b.asBits = j
                }
                assertEquals(i == j, cmp.out.power)
            }
        }
    }


    @Test
    fun testAllComparator() {
        val cmp = Comparator(8)
        for (i in 0..255) {
            for (j in 0..255) {
                simulateNSteps(8) {
                    cmp.a.asBits = i
                    cmp.b.asBits = j
                }
                assertEquals(i < j, cmp.lesser.power)
                assertEquals(i <= j, cmp.lesserEqual.power)
                assertEquals(i == j, cmp.equal.power)
                assertEquals(i > j, cmp.greater.power)
                assertEquals(i >= j, cmp.greaterEqual.power)
            }
        }
    }


}