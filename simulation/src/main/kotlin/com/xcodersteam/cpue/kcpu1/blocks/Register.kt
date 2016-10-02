package com.xcodersteam.cpue.kcpu1.blocks

import com.xcodersteam.cpue.Simulation.transistor
import com.xcodersteam.cpue.blocks.AddressMatcher
import com.xcodersteam.cpue.blocks.BiBusCommutator
import com.xcodersteam.cpue.blocks.MemoryLine
import com.xcodersteam.cpue.simulation.Transistor

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */

open class RegisterConnector(val address: Int) : MainBusConnector {

    val addressMatcher = AddressMatcher(8, address)
    val commutator = BiBusCommutator(8)

    val setControl = transistor(Transistor.SiliconType.N)
    val readControl = transistor(Transistor.SiliconType.N)
    val resetControl = transistor(Transistor.SiliconType.N)

    override val addressBus = addressMatcher.bus
    override val dataBus = commutator.busA
    override val set = setControl.gate
    override val read = readControl.gate
    override val reset = resetControl.gate

    var internalReset = resetControl.drain
    var internalValueBus = commutator.busB

    init {
        setControl.source.link(addressMatcher.out)
        readControl.source.link(addressMatcher.out)
        resetControl.source.link(addressMatcher.out)

        commutator.dirA.link(setControl.drain)
        commutator.dirB.link(readControl.drain)
    }
}

class Register(address: Int) : RegisterConnector(address) {
    val memory = MemoryLine(8)

    init {
        memory.r.link(internalReset)
        memory.outBus.link(internalValueBus)
    }
}