package com.xcodersteam.cpue.kcpu1.blocks

import com.xcodersteam.cpue.AbstractSimulationTest
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Semoro on 02.10.16.
 * ©XCodersTeam, 2016
 */
class RegisterTest : AbstractSimulationTest() {

    lateinit var register: Register
    @Before
    fun createRegister() {
        register = Register(1)
        logicAnalyzer.module("register") {
            bus(register.addressBus, "address")
            bus(register.dataBus, "data")
            wire(register.set, "set")
            wire(register.reset, "reset")
            wire(register.read, "read")
            reg(register.memory.outBus, "internal")
        }
    }

    @Test
    fun test() {
        writeVCD = true
        simulateNSteps(2) {
            register.addressBus.asBits = 1
            register.dataBus.asBits = 0x42
            register.set.power = true
        }
        simulateNSteps(3) {}
        simulateNSteps(3) {
            register.addressBus.asBits = 1
            register.read.power = true
        }
        assertEquals(0x42, register.dataBus.asBits)

        simulateNSteps(3) {}
        simulateNSteps(3) {
            register.addressBus.asBits = 1
            register.reset.power = true
        }
        simulateNSteps(3) {
            register.addressBus.asBits = 1
            register.read.power = true
        }
        assertEquals(0x0, register.dataBus.asBits)
    }

    @Test
    fun testFullWrite() {
        writeVCD = true
        simulateNSteps(2) {
            register.addressBus.asBits = 1
            register.dataBus.asBits = 0xFF
            register.set.power = true
        }
        simulateNSteps(3) {}
        simulateNSteps(3) {
            register.addressBus.asBits = 1
            register.read.power = true
        }
        assertEquals(0xFF, register.dataBus.asBits)

        simulateNSteps(3) {}
        simulateNSteps(3) {
            register.addressBus.asBits = 1
            register.reset.power = true
        }
        simulateNSteps(3) {
            register.addressBus.asBits = 1
            register.read.power = true
        }
        assertEquals(0x0, register.dataBus.asBits)
    }
}