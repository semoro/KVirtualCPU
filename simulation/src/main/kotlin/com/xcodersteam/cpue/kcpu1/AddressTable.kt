package com.xcodersteam.cpue.kcpu1

/**
 * Created by Semoro on 03.10.16.
 * ©XCodersTeam, 2016
 */

data class Address(val i: Int) {
    fun toByte(): Byte = i.toByte()
}

data class AddressLH(val L: Address, val H: Address) {

}

val ADDR_ZERO = Address(0x0)

val ROM_PC_A = Address(0x1)
val ROM_PC_B = Address(0x2)
val ROM_PC_C = Address(0x3)

val JUMP_L = Address(0x4)
val JUMP_H = Address(0x5)
val JUMP_A = Address(0xF)
val JUMP = AddressLH(JUMP_L, JUMP_H)

val ALU_A = Address(0x6)
val ALU_B = Address(0x7)
val ALU_SUM = Address(0x8)
val ALU_SUB = Address(0x9)
val ALU_EQ = Address(0xA)
val ALU_GT = Address(0xB)
val ALU_LT = Address(0xC)
val ALU_GTEQ = Address(0xD)
val ALU_LTEQ = Address(0xE)
val ALU_GROUP = arrayOf(ALU_A, ALU_B, ALU_SUM, ALU_SUB, ALU_EQ, ALU_GT, ALU_LT, ALU_GTEQ, ALU_LTEQ).map { it.i }.toIntArray()
