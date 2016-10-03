package com.xcodersteam.cpue.kcpu1

import com.xcodersteam.cpue.AbstractSimulationTest
import com.xcodersteam.cpue.blocks.ArrayBasedBus
import com.xcodersteam.cpue.kcpu1.blocks.KCPU
import com.xcodersteam.cpue.kcpu1.dummy.DummyROM
import com.xcodersteam.cpue.kcpu1.dummy.DummyROMChip
import junit.framework.TestCase.assertEquals
import org.junit.Test

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
class KCPUTest : AbstractSimulationTest() {

    @Test
    fun simpleTest() {
        writeVCD = true
        val rom = DummyROM(65536 * 3)
        asm {
            jmp(0x0)
        }.writeInto(rom.data)


        val chip = DummyROMChip(rom)


        val kcpu = KCPU()
        kcpu.programCounter.run {
            extReadClk.link(chip.channel1.readClk)
            extAddress.link(chip.channel1.addressBus)
            extOutputs.forEachIndexed { i, bus -> chip.channel1.outputBuses[i].link(bus) }
        }

        logicAnalyzer.module("kcpu") {
            reg(kcpu.accumulator.outBus, "accumulator")
            reg(kcpu.programCounter.data.outBus, "programCounter")
            wire(kcpu.programCounter.accToDataCommutator.enable, "incEnableFlag")

        }
        logicAnalyzer.mainBus(kcpu.mainBus)
        logicAnalyzer.module("timing") {
            bus(ArrayBasedBus(kcpu.phases), "phases")
        }

        kcpu.run {
            for (j in 1..1024) {
                simulateNSteps(3) {
                    phases[0].power = true
                }
                simulateNSteps(3) {
                    phases[1].power = true
                }
                simulateNSteps(3) {
                    phases[2].power = true
                }
                simulateNSteps(32 - 18) {
                    phases[3].power = true
                }
                simulateNSteps(3) {
                    phases[4].power = true
                }
                simulateNSteps(3) {
                    phases[5].power = true
                }
                simulateNSteps(3) {
                    phases[6].power = true
                }
                simulateNSteps(3) {
                    phases[7].power = true
                }
            }
            assertEquals(0x0, kcpu.programCounter.data.outBus.asBits)
        }

    }
}