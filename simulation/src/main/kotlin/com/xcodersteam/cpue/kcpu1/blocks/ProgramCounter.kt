package com.xcodersteam.cpue.kcpu1.blocks

import com.xcodersteam.cpue.Simulation.VCC
import com.xcodersteam.cpue.Simulation.node
import com.xcodersteam.cpue.blocks.*

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
open class LinearCounter {
    val data = MemoryLine(16)
    val summer = Summer(16)
    val accCommutator = BusCommutator(16)
    val acc = MemoryLine(16)
    val accToDataCommutator = BusCommutator(16)

    val phase0 = node() //TODO Turn into array
    val phase1 = node()
    val phase2 = node()
    val phase3 = node()

    open val phases = arrayOf(phase0, phase1, phase2, phase3)

    val incDisableFlag = node()


    init {
        summer.x1.link(data.outBus)
        summer.x2.nodes[0].link(VCC)

        /*
         sequence
         phase0: reset accumulator
         phase1: copy data sum 1 to accumulator
         phase2: reset data
         phase3: copy accumulator to data
         */

        acc.r.link(phase0)

        accCommutator.busA.link(summer.s)
        accCommutator.busB.link(acc.inBus)
        accCommutator.enable.link(phase1)

        data.r.link(phase2)

        accToDataCommutator.busA.link(acc.outBus)
        accToDataCommutator.busB.link(data.inBus)
        accToDataCommutator.enable.link(phase3 and not(incDisableFlag))
    }
}

class ProgramCounter() : LinearCounter() {
    val outputs = Array(3, { RegisterConnector(1 + it) })

    val extAddress = data.outBus
    val extReadClk = node()
    val extOutputs = outputs.map { it.internalValueBus }.toTypedArray()

    val not = NotGate()
    val phase4 = node()
    override val phases = arrayOf(*super.phases, phase4)

    val mainBusConnector: MainBusConnector = MainBusConnectorImpl()

    init {
        outputs.forEach { it.link(mainBusConnector) }
        phase4.link(not.a)
        extReadClk.link(not.b)
    }
}