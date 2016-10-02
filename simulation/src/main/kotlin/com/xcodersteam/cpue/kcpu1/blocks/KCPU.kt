package com.xcodersteam.cpue.kcpu1.blocks

import com.xcodersteam.cpue.Simulation.node
import com.xcodersteam.cpue.blocks.*
import com.xcodersteam.cpue.simulation.Node

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */

interface MainBusConnector {
    val addressBus: AbstractBus
    val dataBus: AbstractBus
    val set: Node
    val read: Node
    val reset: Node

    fun link(other: MainBusConnector) {
        other.addressBus.link(this.addressBus)
        other.dataBus.link(this.dataBus)
        other.set.link(this.set)
        other.read.link(this.read)
        other.reset.link(this.reset)
    }
}

class MainBusConnectorImpl : MainBusConnector {
    override val addressBus: AbstractBus = NodesBus(8)
    override val dataBus: AbstractBus = NodesBus(8)
    override val set: Node = node()
    override val read: Node = node()
    override val reset: Node = node()
}

class KCPU {
    val programCounter = ProgramCounter()
    val accumulator = MemoryLine(8)

    val phases = Array(8, { node() })
    val mainBus = MainBusConnectorImpl()

    val argACommutator = BusCommutator(8)
    val argBCommutator = BusCommutator(8)
    val accumulatorCommutator = BusCommutator(8)

    val jumpRegister = JumpRegister(programCounter)

    init {
        programCounter.mainBusConnector.link(mainBus)
        jumpRegister.mainBusConnector.link(mainBus)

        /*
        note:
         * - means dereference address(like a pointer)
        sequence:
        phase0: read by programCounter from ROM
        phase1: set address to *0x1, preform read, data saved to accumulator
        phase2: set address to *0x2, preform reset
        phase3: set address to *0x2, preform set, data from accumulator saved to *0x2
        programCounter:
            phase4: reset programCounter.accumulator
            phase5: copy data sum 1 to programCounter.accumulator
            phase6: reset data
            phase7: copy accumulator to data if !incDisableFlag
        phase4: reset accumulator
        jumpRegister:
            phase0: reset shouldJumpFlag
            phase6: set programCounter.incDisableFlag
            phase7: copy jumpRegister memory to programCounter.data
         */

        accumulatorCommutator.busA.link(mainBus.dataBus)
        accumulatorCommutator.busB.link(accumulator.outBus)

        argACommutator.busA.link(programCounter.outputs[0].internalValueBus)
        argACommutator.busB.link(mainBus.addressBus)

        argBCommutator.busA.link(programCounter.outputs[1].internalValueBus)
        argBCommutator.busB.link(mainBus.addressBus)

        phases[0].link(programCounter.phase4)

        phases[1].diodeLink(argACommutator.enable)
        phases[1].diodeLink(mainBus.read)
        phases[1].diodeLink(accumulatorCommutator.enable)

        phases[2].diodeLink(argBCommutator.enable)
        phases[2].diodeLink(mainBus.reset)

        phases[3].diodeLink(argBCommutator.enable)
        phases[3].diodeLink(mainBus.set)
        phases[3].diodeLink(accumulatorCommutator.enable)

        for (i in 4..7)
            phases[i].link(programCounter.phases[i - 4])

        phases[4].link(accumulator.r)

        phases[0].link(jumpRegister.resetPhase)
        phases[6].link(jumpRegister.flagPhase)
        phases[7].link(jumpRegister.jumpPhase)
    }
}