package com.xcodersteam.cpue.simulation

/**
 * Created by Semoro on 23.09.16.
 * ©XCodersTeam, 2016
 */
class Link(val a: Node, val b: Node) {

    var isPowered: Boolean = false
    lateinit var stateListener: Link.() -> Unit

    fun update() {
        val tmpP = a.isPowered || b.isPowered
        if (tmpP != isPowered) {
            isPowered = tmpP
            stateListener()
        }
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Link -> a == other.a && b == other.b || a == other.b && b == other.a
            else -> false
        }
    }
}