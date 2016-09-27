package com.xcodersteam.cpue.simulation

import com.xcodersteam.cpue.Simulation.allLinks
import java.util.*

/**
 * Created by semoro on 30.07.16.
 */
open class Node {
    val links = LinkedList<Node>()

    var isPowered = false

    open fun notifyStateChange(from: Node?, state: Boolean) {
        if (isPowered != state) {
            isPowered = state
            onPowerChange(from, state)
        }
    }

    open fun onPowerChange(from: Node?, state: Boolean) {
        for (node in links) {
            if (node != from)
                node.notifyStateChange(this, state)
        }
    }


    open fun reset() {
        isPowered = false
    }

    fun link(n2: Node) {
        allLinks.add(Link(this, n2))
        this.links.add(n2)
        n2.links.add(this)
    }
}