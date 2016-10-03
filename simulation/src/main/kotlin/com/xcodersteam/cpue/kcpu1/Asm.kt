package com.xcodersteam.cpue.kcpu1

/**
 * Created by Semoro on 03.10.16.
 * ©XCodersTeam, 2016
 */


class AsmContext() {

    val labels = mutableMapOf<String, Int>()
    val jumpsToSatisfy = mutableMapOf<String, MutableList<Int>>()

    val outList = mutableListOf<Byte>()

    fun toByteArray(): ByteArray {
        return outList.toByteArray()
    }

    fun writeInto(byteArray: ByteArray, offset: Int = 0) {
        println("ASM Statistics:")
        println("\tInstructions: ${outList.size / 3}")
        println("\t${outList.size} bytes used of ${byteArray.size}")
        val thisAsArray = toByteArray()
        for (i in 0..thisAsArray.size - 1)
            byteArray[i + offset] = thisAsArray[i]
    }

    fun mov(rcv: Address, src: Address) {
        outList += src.toByte()
        outList += rcv.toByte()
        outList += 0x0
    }

    fun mov(rcv: Address, data: Int) {
        outList += ROM_PC_C.toByte()
        outList += rcv.toByte()
        outList += data.toByte()
    }

    fun mov(rcv: AddressLH, data: Int) {
        mov(rcv.L, data and 0xff)
        mov(rcv.H, (data and 0xff00) shr 8)
    }

    fun jmp(target: String) {
        labels[target]?.let { jmp(it) } ?: run {
            jumpsToSatisfy.getOrPut(target, { mutableListOf() }) += outList.size
            nop()
            nop()
        }
    }

    fun jmp(target: Int) {
        mov(JUMP, target)
    }

    private fun jmp(target: Int, insertTargets: List<Int>) {
        val context = AsmContext()
        context.jmp(target)
        val gen = context.toByteArray()
        insertTargets.forEach { i ->
            gen.forEachIndexed { j, byte -> outList[i + j] = byte }
        }
    }

    fun nop() {
        outList += 0x0
        outList += 0x0
        outList += 0x0
    }

    fun inalu(srcA: Address, srcB: Address) {
        mov(ALU_A, srcA)
        mov(ALU_B, srcB)
    }

    fun inalu(a: Int, srcB: Address) {
        mov(ALU_A, a)
        mov(ALU_B, srcB)
    }

    fun inalu(srcA: Address, b: Int) = inalu(b, srcA)

    fun inalu(a: Int, b: Int) {
        mov(ALU_A, a)
        mov(ALU_B, b)
    }

    fun label(name: String) {
        labels[name] = outList.size / 3
        jumpsToSatisfy[name]?.let {
            jmp(labels[name]!!, it)
            jumpsToSatisfy.remove(name)
        }
    }
}

fun asm(init: AsmContext.() -> Unit): AsmContext {
    val context = AsmContext()
    context.init()
    return context
}