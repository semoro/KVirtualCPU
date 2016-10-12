package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.simulation.Node
import com.xcodersteam.cpue.simulation.Transistor

/**
 * Created by Semoro on 03.10.16.
 * ©XCodersTeam, 2016
 */

fun Node.diodeLink(other: Node): TDiode {
    val diode = TDiode()
    diode.a.link(this)
    diode.b.link(other)
    return diode
}

fun not(other: Node): Node {
    val notGate = NotGate()
    notGate.a.link(other)
    return notGate.b
}

infix fun Node.or(other: Node): Node {
    val orGate = ORGate()
    orGate.a.link(this)
    orGate.b.link(other)
    return orGate.c
}

infix fun Node.and(other: Node): Node {
    val andGate = ANDGate()
    andGate.a.link(this)
    andGate.b.link(other)
    return andGate.c
}

infix fun Node.nand(other: Node): Node {
    val nandGate = NANDGate()
    nandGate.a.link(this)
    nandGate.b.link(other)
    return nandGate.c
}

infix fun Node.uand(other: Node): Node {
    val t = Simulation.transistor(Transistor.SiliconType.N)
    t.source.link(this)
    t.gate.link(other)
    return t.drain
}

infix fun Node.uxnand(other: Node): Node {
    val t = Simulation.transistor(Transistor.SiliconType.P)
    t.source.link(this)
    t.gate.link(other)
    return t.drain
}


infix fun Node.ruand(other: Node): Node {
    val t = Simulation.transistor(Transistor.SiliconType.N)
    t.source.link(other)
    t.gate.link(this)
    return t.drain
}

infix fun Node.xor(other: Node): Node {
    val gate = XORGate()
    gate.a.link(this)
    gate.b.link(other)
    return gate.c
}

infix fun Node.xnor(other: Node): Node {
    val gate = XNORGate()
    gate.a.link(this)
    gate.b.link(other)
    return gate.c
}