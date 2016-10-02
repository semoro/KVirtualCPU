package com.xcodersteam.cpue.kcpu1.blocks

import com.xcodersteam.cpue.Simulation.node
import com.xcodersteam.cpue.blocks.*

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
class JumpRegister(val programCounter: ProgramCounter) {
    val memoryLine = MemoryLine(16)
    val registerLow = RegisterConnector(0x4)
    val registerHigh = RegisterConnector(0x5)
    val shouldJumpFlag = RSLatch()

    val flagPhase = node()
    val jumpPhase = node()
    val resetPhase = shouldJumpFlag.r

    val mainBusConnector = MainBusConnectorImpl()
    val busCommutator = BusCommutator(16)

    init {
        registerHigh.link(mainBusConnector)
        registerLow.link(mainBusConnector)

        registerLow.internalReset.link(memoryLine.r)
        registerLow.internalValueBus.link(memoryLine.outBus, 0)

        registerHigh.internalReset.link(shouldJumpFlag.s)
        registerHigh.internalValueBus.link(memoryLine.outBus, 0)

        busCommutator.busA.link(memoryLine.inBus)
        busCommutator.busB.link(programCounter.data.inBus)

        val jCheck = (flagPhase or jumpPhase) uand (shouldJumpFlag.q)
        programCounter.incDisableFlag.link(jCheck)
        busCommutator.enable.link(jCheck)
    }
}
