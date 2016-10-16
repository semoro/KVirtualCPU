package com.xcodersteam.cpue.pmram

import com.xcodersteam.cpue.AbstractSimulationTest
import com.xcodersteam.cpue.dummy.DummyControlledBus
import com.xcodersteam.cpue.dummy.DummyOutputNode
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

class PMRAMTest : AbstractSimulationTest() {

    class TestConnector : PMRAMConnector {
        override val CAS = DummyOutputNode()
        override val RAS = DummyOutputNode()
        override val WE = DummyOutputNode()
        override val OE = DummyOutputNode()
        override val DQ = DummyControlledBus(8)
        override val A = DummyControlledBus(12)
    }

    @Test
    fun testRW() {
        val ram = DummyPMRAM(1024, 1024)
        val connector = TestConnector()
        connector.link(ram)
        connector.apply {
            RAS.state = true
            CAS.state = true
            simulateNSteps(2)

            fun write(row: Int, col: Int, data: Int) {
                A.stateBits = row
                simulateNSteps(1)
                RAS.state = false
                simulateNSteps(2)
                DQ.stateBits = data
                WE.state = false
                A.stateBits = col
                simulateNSteps(1)
                CAS.state = false
                simulateNSteps(2)
                WE.state = true
                CAS.state = true
                RAS.state = true
                DQ.stateBits = 0
                simulateNSteps(2)
            }

            fun read(row: Int, col: Int): Int {
                A.stateBits = row
                simulateNSteps(1)
                RAS.state = false
                simulateNSteps(2)
                WE.state = true
                A.stateBits = col
                simulateNSteps(1)
                CAS.state = false
                simulateNSteps(2)
                OE.state = false
                simulateNSteps(2)
                val out = DQ.asBits
                OE.state = true
                CAS.state = true
                RAS.state = true
                simulateNSteps(2)
                return out
            }


            for (i in 0..255) {
                for (j in 0..255) {
                    write(i, j, (i + j) % 256)
                }
            }


            for (i in 0..255) {
                for (j in 0..255) {
                    collector.checkThat(read(i, j), equalTo((i + j) % 256))
                }
            }
        }

    }
}