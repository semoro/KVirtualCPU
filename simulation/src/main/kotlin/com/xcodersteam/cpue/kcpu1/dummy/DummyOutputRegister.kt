package com.xcodersteam.cpue.kcpu1.dummy

import com.xcodersteam.cpue.dummy.DummyControlledBus
import com.xcodersteam.cpue.kcpu1.blocks.RegisterConnector

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
class DummyOutputRegister(address: Int) : RegisterConnector(address) {
    val dummyControlBus = DummyControlledBus(8)

    init {
        internalValueBus.link(dummyControlBus)
    }
}