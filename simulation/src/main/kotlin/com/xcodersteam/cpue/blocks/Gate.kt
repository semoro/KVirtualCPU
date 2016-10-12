package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.Simulation.VCC
import com.xcodersteam.cpue.Simulation.refNode
import com.xcodersteam.cpue.Simulation.transistor
import com.xcodersteam.cpue.simulation.Transistor.SiliconType

/**
 * Created by Semoro on 23.09.16.
 * ©XCodersTeam, 2016
 */

class ANDGate {

    val a = refNode()
    val b = refNode()
    val c = refNode()

    init {
        c.link((VCC uand a) uand b)
    }
}

class MultiANDGate(val bits: Int) {

    val input = ArrayBasedBus(Array(bits) { refNode() })
    val output = refNode()

    init {
        var prevNode = VCC
        input.nodes.forEach {
            prevNode = (prevNode uand it)
        }
        output.link(prevNode)
    }
}


class NANDGate {
    val andGate = ANDGate()
    val notGate = NotGate()

    val a = andGate.a
    val b = andGate.b

    val c = notGate.b

    init {
        andGate.c.link(notGate.a)
    }
}

class ORGate {
    val a = refNode()
    val b = refNode()

    val c = refNode()

    init {
        c.link(VCC uand a)
        c.link(VCC uand b)
    }
}

class NORGate {
    val a = refNode()
    val b = refNode()

    val c = refNode()

    init {
        c.link(not(a or b))
    }
}


class MultiORGate(val bits: Int) {
    val input = ArrayBasedBus(Array(bits, { refNode() }))
    val output = refNode()

    init {
        input.nodes.forEach {
            output.link(VCC uand it)
        }
    }
}

class NotGate {
    private val t1 = transistor(SiliconType.P)
    val a = t1.gate

    val b = t1.drain

    init {
        t1.source.link(Simulation.VCC)
    }
}

class XORGate {


    val a = refNode()
    val b = refNode()

    val c = refNode()

    init {
        c.link((a or b) and (a nand b))
    }
}

class XNORGate {

    val a = refNode()
    val b = refNode()

    val c = refNode()

    init {
        c.link((a or b) nand (a nand b))
    }
}

class RSLatch {

    val q = refNode()
    val s = refNode()
    val r = refNode()

    init {
        q.link((VCC uxnand r) uand s)
        q.link(s)
    }
}

class TDiode {

    val a = refNode()
    val b = refNode()

    init {
        b.link(VCC uand a)
    }
}