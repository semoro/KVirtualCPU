package com.xcodersteam.cpue.kcpu1.dummy

import com.xcodersteam.cpue.blocks.NodesBus

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */

class DummyRAM(addressBits: Int, dataBits: Int) {
    val addressBus = NodesBus(addressBits)
    val dataBus = NodesBus(dataBits)
}