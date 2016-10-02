package com.xcodersteam.cpue.kcpu1.dummy

import com.xcodersteam.cpue.AbstractSimulationTest
import com.xcodersteam.cpue.kcpu1.blocks.MainBusConnector
import com.xcodersteam.cpue.kcpu1.blocks.ROMController
import com.xcodersteam.cpue.kcpu1.mainBus
import com.xcodersteam.cpue.kcpu1.reg
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
class ROMTest : AbstractSimulationTest() {

    fun setupLogicAnalyzer(controller: ROMController) {
        logicAnalyzer.mainBus(controller)
        logicAnalyzer.module("romController") {
            reg(controller.addressRegisterL, "addressL")
            reg(controller.addressRegisterH, "addressH")
            reg(controller.outputs[0], "ext0x3")
            reg(controller.outputs[1], "ext0x4")
            reg(controller.outputs[2], "ext0x5")
        }
    }

    @Test
    fun testL() {
        val rom = DummyROM(256 * 3)
        for (i in 0..256 * 3 - 1)
            rom.data[i] = i.toByte()
        val chip = DummyROMChip(rom)

        val controller = ROMController(1)
        controller.extReadClk.link(chip.channel1.readClk)
        controller.extAddress.link(chip.channel1.addressBus)
        controller.extOutputs.forEachIndexed { i, bus -> chip.channel1.outputBuses[i].link(bus) }
        setupLogicAnalyzer(controller)
        (controller as MainBusConnector).run {
            for (j in 0..255) {
                simulateNSteps(3) {
                    addressBus.asBits = 0x1
                    reset.power = true
                }
                simulateNSteps(2) {
                    dataBus.asBits = j
                    addressBus.asBits = 0x1
                    set.power = true
                }

                for (i in 0..2) {
                    simulateNSteps(3) {
                    }

                    simulateNSteps(3) {
                        addressBus.asBits = 0x3 + i
                        read.power = true
                    }
                    collector.checkThat(dataBus.asBits, equalTo(rom.data[j * 3 + i].toInt() and 0xff))
                }
            }
        }
    }

    @Test
    fun testHL() {

        val rom = DummyROM(65536 * 3)
        for (i in 0..65536 * 3 - 1)
            rom.data[i] = i.toByte()
        val chip = DummyROMChip(rom)

        val controller = ROMController(1)
        controller.extReadClk.link(chip.channel1.readClk)
        controller.extAddress.link(chip.channel1.addressBus)
        controller.extOutputs.forEachIndexed { i, bus -> chip.channel1.outputBuses[i].link(bus) }
        setupLogicAnalyzer(controller)
        (controller as MainBusConnector).run {
            for (j in 0..65535) {
                simulateNSteps(3) {
                    addressBus.asBits = 0x1
                    reset.power = true
                }
                simulateNSteps(2) {
                    dataBus.asBits = j and 0xff
                    addressBus.asBits = 0x1
                    set.power = true
                }
                simulateNSteps(3) {
                    addressBus.asBits = 0x2
                    reset.power = true
                }
                simulateNSteps(2) {
                    dataBus.asBits = (j and 0xff00) shr 8
                    addressBus.asBits = 0x2
                    set.power = true
                }

                for (i in 0..2) {
                    simulateNSteps(3) {
                    }

                    simulateNSteps(3) {
                        addressBus.asBits = 0x3 + i
                        read.power = true
                    }

                    collector.checkThat(dataBus.asBits, equalTo(rom.data[j * 3 + i].toInt() and 0xff))
                }
            }
        }
    }

}