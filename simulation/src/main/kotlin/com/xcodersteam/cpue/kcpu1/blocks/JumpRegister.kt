package com.xcodersteam.cpue.kcpu1.blocks

import com.xcodersteam.cpue.Simulation.VCC
import com.xcodersteam.cpue.Simulation.node
import com.xcodersteam.cpue.blocks.*
import com.xcodersteam.cpue.kcpu1.JUMP_A
import com.xcodersteam.cpue.kcpu1.JUMP_H
import com.xcodersteam.cpue.kcpu1.JUMP_L

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
class JumpRegister(val programCounter: ProgramCounter) {
    val memoryLine = MemoryLine(16)
    val registerLow = RegisterConnector(JUMP_L.i)
    val registerHigh = RegisterConnector(JUMP_H.i)

    val checkRegister = Register(JUMP_A.i)
    val shouldJumpFlag = RSLatch()
    val shouldCheckFlag = RSLatch()

    val flagPhase = node()
    val jumpPhase = node()
    val resetPhase = shouldJumpFlag.r

    val mainBusConnector = MainBusConnectorImpl()
    val busCommutator = BusCommutator(16)

    val checkResult = MultiOrGate(8)

    init {
        registerHigh.link(mainBusConnector)
        registerLow.link(mainBusConnector)
        checkRegister.link(mainBusConnector)

        registerLow.internalReset.link(memoryLine.r)
        registerLow.internalValueBus.link(memoryLine.outBus, 0)

        registerHigh.internalReset.link(shouldJumpFlag.s)
        registerHigh.internalValueBus.link(memoryLine.outBus, 0)

        busCommutator.busA.link(memoryLine.inBus)
        busCommutator.busB.link(programCounter.data.inBus)

        (VCC uand checkRegister.internalReset).link(shouldCheckFlag.s)
        (shouldJumpFlag.q and resetPhase).link(shouldCheckFlag.r)

        checkRegister.memory.outBus.link(checkResult.input)

        val jCheck = (flagPhase or jumpPhase) uand ((shouldJumpFlag.q) uand (not(shouldCheckFlag.q) or checkResult.output))
        programCounter.incDisableFlag.link(jCheck)
        busCommutator.enable.link(jCheck)
    }
}
