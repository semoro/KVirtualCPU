package com.xcodersteam.cpue.kcpu1

/**
 * Created by Semoro on 03.10.16.
 * ©XCodersTeam, 2016
 */


class AsmContext() {

    val outList = mutableListOf<Byte>()

    fun toByteArray(): ByteArray {
        return outList.toByteArray()
    }

    fun writeInto(byteArray: ByteArray, offset: Int = 0) {
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

    fun jmp(target: Int) {
        mov(JUMP, target)
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

    fun label(): Int {
        return outList.size / 3
    }
}

fun asm(init: AsmContext.() -> Unit): AsmContext {
    val context = AsmContext()
    context.init()
    return context
}