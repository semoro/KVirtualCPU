package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.AbstractSimulationTest
import com.xcodersteam.cpue.dummy.DummyControlledBus
import org.hamcrest.CoreMatchers.equalTo

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */
class TruthTable(val inputCount: Int, val outputCount: Int, val table: Array<out Row>) {

    fun test(context: AbstractSimulationTest, i: Int) {
        var line = 0
        for ((inputState, outputState) in table) {
            line++
            context.simulateNSteps(i) {
                controlBus.stateBits = inputState
            }

            context.collector.checkThat(outputs.asBits.mirrorBits(outputCount).toBinaryString(),
                    equalTo(outputState.toBinaryString()), "On line $line, input: ${inputState.toBinaryString()}")
        }
    }

    fun Int.toBinaryString(): String {
        return String.format("%${outputCount}s", Integer.toBinaryString(this)).replace(" ", "0")
    }

    fun setup(t: TruthTable.() -> Unit): TruthTable {
        t()
        inputs.nodes.forEachIndexed { i, node -> controlBus.nodes[inputCount - i - 1].link(node) }
        return this
    }

    private val controlBus = DummyControlledBus(inputCount)

    val inputs = NodesBus(inputCount)
    val outputs = NodesBus(outputCount)

    data class Row(val inputs: Int, val outputs: Int)
}

fun Int.mirrorBits(inputCount: Int): Int {
    return Integer.reverse(this) ushr (32 - inputCount)
}


fun truthTable(ic: Int, oc: Int, vararg table: Pair<Int, Int>) = TruthTable(ic, oc, table.map { TruthTable.Row(it.first, it.second) }.toTypedArray())