package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.simulation.Node
import com.xcodersteam.cpue.simulation.Transistor

/**
 * Created by Semoro on 03.10.16.
 * ©XCodersTeam, 2016
 */

fun Node.diodeLink(other: Node): TDiode {
    TDiode().let {
        it.a.link(this)
        it.b.link(other)
        return it
    }
}

fun diode(node: Node): Node {
    TDiode().let {
        it.a.link(node)
        return it.b
    }
}

fun not(other: Node): Node {
    NotGate().let {
        it.a.link(other)
        return it.b
    }
}

infix fun Node.or(other: Node): Node {
    ORGate().let {
        it.a.link(this)
        it.b.link(other)
        return it.c
    }
}

infix fun Node.nor(other: Node): Node {
    NORGate().let {
        it.a.link(this)
        it.b.link(other)
        return it.c
    }
}

infix fun Node.and(other: Node): Node {
    ANDGate().let {
        it.a.link(this)
        it.b.link(other)
        return it.c
    }
}

infix fun Node.nand(other: Node): Node {
    NANDGate().let {
        it.a.link(this)
        it.b.link(other)
        return it.c
    }
}

infix fun Node.uand(other: Node): Node {
    Simulation.transistor(Transistor.SiliconType.N).let {
        it.source.link(this)
        it.gate.link(other)
        return it.drain
    }
}

infix fun Node.uxnand(other: Node): Node {
    Simulation.transistor(Transistor.SiliconType.P).let {
        it.source.link(this)
        it.gate.link(other)
        return it.drain
    }
}


infix fun Node.ruand(other: Node): Node {
    Simulation.transistor(Transistor.SiliconType.N).let {
        it.source.link(other)
        it.gate.link(this)
        return it.drain
    }
}

infix fun Node.xor(other: Node): Node {
    XORGate().let {
        it.a.link(this)
        it.b.link(other)
        return it.c
    }
}

infix fun Node.xnor(other: Node): Node {
    XNORGate().let {
        it.a.link(this)
        it.b.link(other)
        return it.c
    }
}