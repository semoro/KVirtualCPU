package com.xcodersteam.cpue.pmram

import com.xcodersteam.cpue.blocks.NodesBus
import com.xcodersteam.cpue.dummy.DummyControlledBus
import com.xcodersteam.cpue.dummy.diNode

class DummyPMRAM(rows: Int, colums: Int) : PMRAMConnector {

    override val CAS = diNode()
    override val OE = diNode()
    override val RAS = diNode()
    override val WE = diNode()
    override val DQ = DummyControlledBus(8)
    override val A = NodesBus(12)

    private val data = Array(rows, { ByteArray(colums) })

    private var rowAddress = 0
    private var columnAddress = 0

    init {
        RAS.stateListener = {
            edgeFall {
                rowAddress = A.asBits
            }
        }
        CAS.stateListener = {
            edgeFall {
                columnAddress = A.asBits
                if (!WE.isPowered) {
                    data[rowAddress][columnAddress] = DQ.asBits.toByte()
                }
            }
            edgeFront {
                DQ.stateBits = 0
            }
        }
        OE.stateListener = {
            edgeFall {
                DQ.stateBits = data[rowAddress][columnAddress].toInt()
            }
        }
    }
}