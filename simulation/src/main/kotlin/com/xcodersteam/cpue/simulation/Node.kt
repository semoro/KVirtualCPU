package com.xcodersteam.cpue.simulation

import com.xcodersteam.cpue.Simulation
import java.util.*

open class Node {
    var links = LinkedList<Node>()

    var isPowered = false


    open fun notifyStateChange(from: Node?) {
        if (!isPowered) {
            isPowered = true
            onPowerChange(from)
        }
    }

    open fun onPowerChange(from: Node?) {
        for (link in links) {
            if (link !== from)
                link.notifyStateChange(this)
        }
    }

    open fun reset() {
        isPowered = false
    }

    fun link(n2: Node) {
        Simulation.allLinks.add(Link(this, n2))
        this.links.add(n2)
        n2.links.add(this)
    }

}