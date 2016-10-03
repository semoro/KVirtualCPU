package com.xcodersteam.cpue.kcpu1.blocks

import com.xcodersteam.cpue.blocks.Comparator
import com.xcodersteam.cpue.blocks.Subtractor
import com.xcodersteam.cpue.blocks.Summer

/**
 * Created by Semoro on 03.10.16.
 * ©XCodersTeam, 2016
 */
class ALU(val addressGroup: IntArray) {
    val regA = Register(addressGroup[0])
    val regB = Register(addressGroup[1])

    val summer = Summer(8)
    val subtractor = Subtractor(8)
    val comparator = Comparator(8)

    val sum = RegisterConnector(addressGroup[2])
    val sub = RegisterConnector(addressGroup[3])

    val eq = RegisterConnector(addressGroup[4])
    val gt = RegisterConnector(addressGroup[5])
    val lt = RegisterConnector(addressGroup[6])
    val gteq = RegisterConnector(addressGroup[7])
    val lteq = RegisterConnector(addressGroup[8])

    val mainBusConnector: MainBusConnector = MainBusConnectorImpl()

    init {
        regA.memory.outBus.link(summer.x1)
        regB.memory.outBus.link(summer.x2)
        sum.internalValueBus.link(summer.s)

        regA.memory.outBus.link(subtractor.x1)
        regB.memory.outBus.link(subtractor.x2)
        sub.internalValueBus.link(subtractor.s)

        regA.memory.outBus.link(comparator.a)
        regB.memory.outBus.link(comparator.b)
        eq.internalValueBus.nodes[0].link(comparator.equal)
        gt.internalValueBus.nodes[0].link(comparator.greater)
        lt.internalValueBus.nodes[0].link(comparator.lesser)
        gteq.internalValueBus.nodes[0].link(comparator.greaterEqual)
        lteq.internalValueBus.nodes[0].link(comparator.lesserEqual)

        listOf(regA, regB, sum, sub, eq, gt, lt, gteq, lteq).forEach { it.link(mainBusConnector) }
    }
}