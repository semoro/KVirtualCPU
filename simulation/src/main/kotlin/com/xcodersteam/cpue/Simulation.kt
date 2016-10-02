package com.xcodersteam.cpue


import com.xcodersteam.cpue.Simulation.UpdateListener
import com.xcodersteam.cpue.simulation.Link
import com.xcodersteam.cpue.simulation.Node
import com.xcodersteam.cpue.simulation.Transistor

/**
 * Created by Semoro on 23.09.16.
 * ©XCodersTeam, 2016
 */
object Simulation {

    typealias UpdateListener = () -> Unit

    val allNodes = mutableListOf<Node>()
    val allTransistors = mutableListOf<Transistor>()
    val allLinks = mutableSetOf<Link>()
    val preUpdate = mutableListOf<UpdateListener>()
    val postUpdate = mutableListOf<UpdateListener>()


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

    fun postUpdate() {
        postUpdate.forEach { it.invoke() }
    }

    fun preUpdate() {
        preUpdate.forEach { it.invoke() }
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
        Simulation.preUpdate()
        Simulation.updateTransistors()
        Simulation.postUpdate()
    }


}