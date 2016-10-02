package com.xcodersteam.cpue.kcpu1.dummy

import com.xcodersteam.cpue.AbstractSimulationTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
class ROMChipTest : AbstractSimulationTest() {


    @Test
    fun test8Bit() {
        val rom = DummyROM(256 * 3)
        for (i in 0..256 * 3 - 1)
            rom.data[i] = i.toByte()
        val chip = DummyROMChip(rom)
        chip.channel1.run {
            for (j in 0..255) {
                simulateNSteps(1) {
                    addressBus.asBits = j
                    readClk.power = true
                }
                simulateNSteps(2) {
                    addressBus.asBits = j
                }
                for (i in 0..2) {
                    collector.checkThat(outputBuses[i].asBits.toByte(), equalTo(rom.data[i + j * 3]))
                }
            }
        }
    }

    @Test
    fun test16Bit() {
        val rom = DummyROM(65536 * 3)
        for (i in 0..65536 * 3 - 1)
            rom.data[i] = i.toByte()
        val chip = DummyROMChip(rom)
        chip.channel1.run {
            for (j in 0..65535) {
                simulateNSteps(1) {
                    addressBus.asBits = j
                    readClk.power = true
                }
                simulateNSteps(2) {
                    addressBus.asBits = j
                }
                for (i in 0..2) {
                    collector.checkThat(outputBuses[i].asBits.toByte(), equalTo(rom.data[i + j * 3]))
                }
            }
        }
    }
}