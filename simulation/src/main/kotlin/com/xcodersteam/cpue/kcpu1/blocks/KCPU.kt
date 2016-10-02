package com.xcodersteam.cpue.kcpu1.blocks

import com.xcodersteam.cpue.Simulation.node
import com.xcodersteam.cpue.blocks.AbstractBus
import com.xcodersteam.cpue.blocks.NodesBus
import com.xcodersteam.cpue.simulation.Node

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */

interface MainBusConnector {
    val addressBus: AbstractBus
    val dataBus: AbstractBus
    val set: Node
    val read: Node
    val reset: Node

    fun link(other: MainBusConnector) {
        other.addressBus.link(this.addressBus)
        other.dataBus.link(this.dataBus)
        other.set.link(this.set)
        other.read.link(this.read)
        other.reset.link(this.reset)
    }
}

class MainBusConnectorImpl : MainBusConnector {
    override val addressBus: AbstractBus = NodesBus(8)
    override val dataBus: AbstractBus = NodesBus(8)
    override val set: Node = node()
    override val read: Node = node()
    override val reset: Node = node()
}