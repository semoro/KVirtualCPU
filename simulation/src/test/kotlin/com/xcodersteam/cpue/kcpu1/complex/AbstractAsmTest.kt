package com.xcodersteam.cpue.kcpu1.complex

import com.xcodersteam.cpue.AbstractSimulationTest
import com.xcodersteam.cpue.blocks.ArrayBasedBus
import com.xcodersteam.cpue.kcpu1.blocks.KCPU
import com.xcodersteam.cpue.kcpu1.dummy.DummyROM
import com.xcodersteam.cpue.kcpu1.dummy.DummyROMChip
import com.xcodersteam.cpue.kcpu1.mainBus
import org.junit.Before

/**
 * Created by Semoro on 03.10.16.
 * ©XCodersTeam, 2016
 */
abstract class AbstractAsmTest : AbstractSimulationTest() {


    lateinit var kcpu: KCPU
    lateinit var rom: DummyROM
    @Before
    fun createKCPU() {
        rom = DummyROM(65536 * 3)
        val chip = DummyROMChip(rom)
        kcpu = KCPU()
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
    }

    fun simulateCycles(n: Int) {
        for (i in 1..n)
            simulateCycle()
    }

    fun simulateCycle() {
        kcpu.run {
            simulateNSteps(3) {
                phases[0].power = true
            }
            simulateNSteps(4) {
                phases[1].power = true
            }
            simulateNSteps(4) {
                phases[2].power = true
            }
            simulateNSteps(32 - 20) {
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
    }
}