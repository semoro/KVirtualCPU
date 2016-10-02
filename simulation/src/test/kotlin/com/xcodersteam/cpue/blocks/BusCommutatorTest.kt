package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.AbstractSimulationTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
class BusCommutatorTest : AbstractSimulationTest() {

    @Test
    fun testBusCommutator() {
        val commutator = BusCommutator(8)
        for (i in 0..255) {
            simulateNSteps(3) {
                commutator.busA.asBits = i
            }
            collector.checkThat(commutator.busB.asBits, equalTo(0))
        }
        for (i in 0..255) {
            simulateNSteps(3) {
                commutator.busB.asBits = i
            }
            collector.checkThat(commutator.busA.asBits, equalTo(0))
        }
        for (i in 0..255) {
            simulateNSteps(3) {
                commutator.busA.asBits = i
                commutator.enable.power = true
            }
            collector.checkThat(commutator.busB.asBits, equalTo(i))
        }
        for (i in 0..255) {
            simulateNSteps(3) {
                commutator.busB.asBits = i
                commutator.enable.power = true
            }
            collector.checkThat(commutator.busA.asBits, equalTo(i))
        }
    }

    @Test
    fun testBiBusCommutator() {
        val commutator = BiBusCommutator(8)
        for (i in 0..255) {
            simulateNSteps(5) {
                commutator.busA.asBits = i
            }
            collector.checkThat(commutator.busB.asBits, equalTo(0))
        }
        for (i in 0..255) {
            simulateNSteps(5) {
                commutator.busB.asBits = i
            }
            collector.checkThat(commutator.busA.asBits, equalTo(0))
        }
        for (i in 0..255) {
            simulateNSteps(5) {
                commutator.busA.asBits = i
                commutator.dirA.power = true
            }
            collector.checkThat(commutator.busB.asBits, equalTo(i))
        }
        for (i in 0..255) {
            simulateNSteps(5) {
                commutator.busB.asBits = i
                commutator.dirB.power = true
            }
            collector.checkThat(commutator.busA.asBits, equalTo(i))
        }
    }
}