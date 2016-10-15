package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation
import com.xcodersteam.cpue.Simulation.refNode
import com.xcodersteam.cpue.Simulation.transistor
import com.xcodersteam.cpue.simulation.Transistor.SiliconType

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */
class MemoryLine(val bits: Int) {
    val latches = Array(bits, { RSLatch() })

    val r = refNode()

    val inNodes = latches.map { it.s }.toTypedArray()
    val inBus = ArrayBasedBus(inNodes)

    val outNodes = latches.map { it.q }.toTypedArray()
    val outBus = ArrayBasedBus(outNodes)

    init {
        latches.forEach { it.r.link(r) }
    }
}

class AddressMatcher(val bits: Int, val address: Int) {

    val transistors = Array(bits, { i ->
        val type = if ((address shr i) and 1 == 1) SiliconType.N else SiliconType.P
        transistor(type)
    })

    val out = transistors.last().drain
    val bus = ArrayBasedBus(transistors.map { it.gate }.toTypedArray())

    init {
        var linkTo = Simulation.VCC
        transistors.forEach {
            it.source.link(linkTo)
            linkTo = it.drain
        }
    }
}

class MemoryBlock(val addressBits: Int, val dataBits: Int, startAddress: Int, size: Int) {

    val s = refNode()
    val r = refNode()

    val addressBus = RefNodesBus(addressBits)
    val dataBus = RefNodesBus(dataBits)


    class MemoryRow(addressBus: AbstractBus, dataBus: AbstractBus, address: Int) {
        val addressMatcher = AddressMatcher(addressBus.bits, address)
        val memoryLine = MemoryLine(dataBus.bits)
        val resetControl = transistor(SiliconType.N)
        val accessControl = transistor(SiliconType.N)
        val readCommutator = BusCommutator(dataBus.bits)
        val r = resetControl.gate
        val s = accessControl.gate


        init {
            addressMatcher.bus.link(addressBus)
            resetControl.source.link(addressMatcher.out)
            accessControl.source.link(addressMatcher.out)
            memoryLine.inBus.link(readCommutator.busA)
            readCommutator.busB.link(dataBus)
            accessControl.drain.link(readCommutator.enable)
            resetControl.drain.link(memoryLine.r)
        }
    }

    fun createMemoryRow(address: Int): MemoryRow {
        val memoryRow = MemoryRow(addressBus, dataBus, address)
        memoryRow.r.link(r)
        memoryRow.s.link(s)
        return memoryRow
    }

    val memoryRows = Array(size, {
        createMemoryRow(startAddress + it)
    })
}