package com.xcodersteam.cpue.kcpu1.blocks

import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.blocks.MemoryLine
import com.xcodersteam.cpue.blocks.TDiode

class ROMController(val address: Int) : MainBusConnector {


    val addressRegisterL = RegisterConnector(address)
    val addressRegisterH = RegisterConnector(address + 1)

    val outputs = Array(3, { RegisterConnector(address + 2 + it) })


    override val addressBus = addressRegisterL.addressBus
    override val dataBus = addressRegisterL.dataBus
    override val set = addressRegisterL.set
    override val read = addressRegisterL.read
    override val reset = addressRegisterL.reset


    val addressMemory = MemoryLine(16)
    val extReadClk = Simulation.node()
    val extAddress = addressMemory.outBus
    val extOutputs = outputs.map { it.internalValueBus }.toTypedArray()

    init {
        addressRegisterH.link(this)
        outputs.forEach { it.link(this) }

        addressRegisterL.internalValueBus.link(addressMemory.inBus, 0)
        addressRegisterH.internalValueBus.link(addressMemory.inBus, 8)
        addressRegisterL.internalReset.link(addressMemory.r)

        val d1 = TDiode()
        val d2 = TDiode()
        addressRegisterL.setControl.drain.link(d1.a)
        addressRegisterH.setControl.drain.link(d2.a)
        extReadClk.link(d1.b)
        extReadClk.link(d2.b)

    }
}