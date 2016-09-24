package com.xcodersteam.cpue

import com.xcodersteam.cpue.simulation.Link
import com.xcodersteam.cpue.simulation.Node
import com.xcodersteam.cpue.simulation.Transistor

/**
 * Created by Semoro on 23.09.16.
 * ©XCodersTeam, 2016
 */
object Simulation {

    val allNodes = mutableListOf<Node>()
    val allTransistors = mutableListOf<Transistor>()
    val allLinks = mutableSetOf<Link>()


    fun node(): Node {
        val node = Node()
        allNodes.add(node)
        return node
    }

    fun registerNode(node: Node) {
        allNodes.add(node)
    }


    fun transistor(channel: Transistor.SiliconType): Transistor {
        val t = Transistor(channel)
        t.nodes.forEach { registerNode(it) }
        allTransistors.add(t)
        return t
    }

    fun reset() {
        allNodes.forEach(Node::reset)
    }

    fun updateTransistors() {
        allTransistors.forEach { it.update() }
    }

    val VCC = node()

    fun clear() {
        allNodes.clear()
        allTransistors.clear()
        allLinks.clear()
        registerNode(VCC)
    }

    var Node.power: Boolean
        get() = this.isPowered
        set(value) = this.notifyStateChange(null, value)

    fun simulationStep(e: Simulation.() -> Unit) {
        Simulation.reset()
        VCC.notifyStateChange(null, true)
        e.invoke(this)
        Simulation.updateTransistors()
    }


}