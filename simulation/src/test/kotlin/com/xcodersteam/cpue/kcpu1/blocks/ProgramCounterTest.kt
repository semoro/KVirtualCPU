package com.xcodersteam.cpue.kcpu1.blocks

import com.xcodersteam.cpue.AbstractSimulationTest
import com.xcodersteam.cpue.kcpu1.dummy.DummyROM
import com.xcodersteam.cpue.kcpu1.dummy.DummyROMChip
import com.xcodersteam.cpue.kcpu1.mainBus
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
class ProgramCounterTest : AbstractSimulationTest() {

    @Test
    fun testLinearIncrease() {
        val linearCounter = LinearCounter()
        linearCounter.run {
            logicAnalyzer.module("timing") {
                wire(phase0, "phase0")
                wire(phase1, "phase1")
                wire(phase2, "phase2")
                wire(phase3, "phase3")
            }
            logicAnalyzer.module("linearCounter") {
                reg(acc.outBus, "acc")
                reg(data.outBus, "data")
            }
        }

        linearCounter.run {
            for (i in 1..65535) {
                simulateNSteps(29) {
                    phase0.power = true
                }
                simulateNSteps(3) {
                    phase1.power = true
                }
                simulateNSteps(3) {
                    phase2.power = true
                }
                simulateNSteps(3) {
                    phase3.power = true
                }
                collector.checkThat(data.outBus.asBits, equalTo(i))
            }
        }
    }

    @Test
    fun testLinearROMRead() {
        val programCounter = ProgramCounter()
        programCounter.run {
            logicAnalyzer.module("timing") {
                wire(phase0, "phase0")
                wire(phase1, "phase1")
                wire(phase2, "phase2")
                wire(phase3, "phase3")
                wire(phase4, "phase4")
            }
            logicAnalyzer.module("programCounter") {
                reg(acc.outBus, "acc")
                reg(data.outBus, "data")
            }
            logicAnalyzer.mainBus(mainBusConnector)
        }

        val rom = DummyROM(65536 * 3)
        for (i in 0..65536 * 3 - 1)
            rom.data[i] = i.toByte()
        val chip = DummyROMChip(rom)

        programCounter.extReadClk.link(chip.channel1.readClk)
        programCounter.extAddress.link(chip.channel1.addressBus)
        programCounter.extOutputs.forEachIndexed { i, bus -> chip.channel1.outputBuses[i].link(bus) }

        programCounter.run {
            for (j in 1..65535) {
                simulateNSteps(29 - 18) {
                    phase0.power = true
                }
                simulateNSteps(3) {
                    phase1.power = true
                }
                simulateNSteps(3) {
                    phase2.power = true
                }
                simulateNSteps(3) {
                    phase3.power = true
                }
                simulateNSteps(3) {
                    phase4.power = true
                }
                programCounter.mainBusConnector.run {
                    for (i in 0..2) {
                        simulateNSteps(3) {

                        }
                        simulateNSteps(3) {
                            addressBus.asBits = 0x1 + i
                            read.power = true
                        }
                        collector.checkThat(dataBus.asBits, equalTo(rom.data[j * 3 + i].toInt() and 0xff))
                    }
                }
            }
        }
    }
}