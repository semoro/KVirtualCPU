package com.xcodersteam.cpue.kcpu1.dummy

import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.Simulation.node
import com.xcodersteam.cpue.blocks.NodesBus
import com.xcodersteam.cpue.dummy.DummyControlledBus

/**
 * Created by Semoro on 29.09.16.
 * ©XCodersTeam, 2016
 */

class DummyROM(val size: Int) {
    val data = ByteArray(size)
}


class DummyROMChip(val rom: DummyROM) {

    class Channel(val chip: DummyROMChip) {
        val addressBus = NodesBus(16)
        val readClk = node()
        val outputBuses = Array(3, { DummyControlledBus(8) })
        var shouldUpdate = false
        fun update() {
            if (!shouldUpdate && readClk.isPowered)
                shouldUpdate = true
            if (shouldUpdate && !readClk.isPowered) {
                val address = addressBus.asBits
                for (i in 0..2) {
                    outputBuses[i].stateBits = chip.rom.data[address * 3 + i].toInt()
                }
            }
        }
    }

    val channel1 = Channel(this)
    val channel2 = Channel(this)

    init {
        Simulation.postUpdate.add(channel1::update)
        Simulation.postUpdate.add(channel2::update)
    }
}

