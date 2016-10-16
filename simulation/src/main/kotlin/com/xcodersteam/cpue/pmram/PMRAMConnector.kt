package com.xcodersteam.cpue.pmram

import com.xcodersteam.cpue.blocks.AbstractBus
import com.xcodersteam.cpue.simulation.Node


interface PMRAMConnector {
    val CAS: Node
    val RAS: Node
    val WE: Node
    val OE: Node
    val DQ: AbstractBus
    val A: AbstractBus

    fun link(other: PMRAMConnector) {
        CAS.link(other.CAS)
        RAS.link(other.RAS)
        WE.link(other.WE)
        OE.link(other.OE)
        DQ.link(other.DQ)
        A.link(other.A)
    }
}