package com.xcodersteam.cpue.kcpu1

import com.xcodersteam.cpue.analyzer.LogicAnalyzer
import com.xcodersteam.cpue.kcpu1.blocks.MainBusConnector
import com.xcodersteam.cpue.kcpu1.blocks.RegisterConnector

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */

fun LogicAnalyzer.Module.reg(r: RegisterConnector, name: String) {
    module(name) {
        wire(r.commutator.dirA, "wEn")
        wire(r.commutator.dirB, "rEn")
        wire(r.internalReset, "resetI")
        reg(r.internalValueBus, "valueI")
    }
}


fun LogicAnalyzer.mainBus(cn: MainBusConnector) {
    module("mainBus") {
        bus(cn.addressBus, "address")
        bus(cn.dataBus, "data")
        wire(cn.set, "set")
        wire(cn.read, "read")
        wire(cn.reset, "reset")
    }
}