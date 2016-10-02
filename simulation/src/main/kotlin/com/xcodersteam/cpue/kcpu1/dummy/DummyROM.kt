package com.xcodersteam.cpue.kcpu1.dummy

import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.kcpu1.blocks.MainBusConnector
import com.xcodersteam.cpue.kcpu1.blocks.Register

/**
 * Created by Semoro on 29.09.16.
 * ©XCodersTeam, 2016
 */

class DummyROM(val size: Int) {
    val data = ByteArray(size)
}


class ROMController(val address: Int, val rom: DummyROM) : MainBusConnector {


    val addressRegisterL = Register(address)
    val addressRegisterH = Register(address + 1)

    val externalConnections = Array(3, { DummyOutputRegister(address + 2 + it) })


    override val addressBus = addressRegisterL.addressBus
    override val dataBus = addressRegisterL.dataBus
    override val set = addressRegisterL.set
    override val read = addressRegisterL.read
    override val reset = addressRegisterL.reset


    var shouldUpdate = false

    init {
        addressRegisterH.link(this)
        externalConnections.forEach { it.link(this) }

        Simulation.postUpdate.add({
            if (!shouldUpdate && addressRegisterL.setControl.drain.isPowered || addressRegisterH.setControl.drain.isPowered) {
                shouldUpdate = true
            }
            if (shouldUpdate && !set.isPowered) {
                shouldUpdate = false
                val address = addressRegisterL.memory.outBus.asBits or (addressRegisterH.memory.outBus.asBits shl 8)
                for (i in 0..2) {
                    externalConnections[i].dummyControlBus.stateBits = rom.data[address * 3 + i].toInt()
                }
            }
        })
    }
}