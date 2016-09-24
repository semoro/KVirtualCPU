package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.Simulation.VCC
import com.xcodersteam.cpue.Simulation.node
import com.xcodersteam.cpue.Simulation.transistor
import com.xcodersteam.cpue.simulation.Transistor

/**
 * Created by Semoro on 23.09.16.
 * ©XCodersTeam, 2016
 */

class ANDGate {
    val t1 = transistor(Transistor.SiliconType.N)
    val t2 = transistor(Transistor.SiliconType.N)

    val a = t1.gate
    val b = t2.gate

    val c = t2.drain

    init {
        t1.source.link(Simulation.VCC)
        t2.source.link(t1.drain)
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
    val t1 = transistor(Transistor.SiliconType.N)
    val t2 = transistor(Transistor.SiliconType.N)

    val a = t1.gate
    val b = t2.gate


    val c = node()

    init {
        t1.source.link(Simulation.VCC)
        t2.source.link(Simulation.VCC)
        c.link(t1.drain)
        c.link(t2.drain)
    }
}

class NotGate {
    val t1 = transistor(Transistor.SiliconType.P)

    val a = t1.gate
    val b = t1.drain

    init {
        t1.source.link(Simulation.VCC)
    }
}

class XORGate {
    val andGate = ANDGate()
    val orGate = ORGate()
    val nandGate = NANDGate()

    val a = node()
    val b = node()
    val c = andGate.c

    init {
        orGate.a.link(a)
        orGate.b.link(b)
        nandGate.a.link(a)
        nandGate.b.link(b)
        andGate.a.link(orGate.c)
        andGate.b.link(nandGate.c)
    }
}

class XNORGate {
    val outGate = NANDGate()
    val orGate = ORGate()
    val nandGate = NANDGate()

    val a = node()
    val b = node()
    val c = outGate.c

    init {
        orGate.a.link(a)
        orGate.b.link(b)
        nandGate.a.link(a)
        nandGate.b.link(b)
        outGate.a.link(orGate.c)
        outGate.b.link(nandGate.c)
    }
}

class RSLatch {
    val resetControl = transistor(Transistor.SiliconType.P)
    val dataT = transistor(Transistor.SiliconType.N)

    val q = dataT.drain
    val s = dataT.gate
    val r = resetControl.gate

    init {
        dataT.source.link(VCC)
        resetControl.source.link(dataT.drain)
        dataT.gate.link(resetControl.drain)
    }
}
