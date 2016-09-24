package com.xcodersteam.cpue.blocks

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */
class TruthTable(val table: Array<out Row>) {

    fun test(test: Row.() -> Unit) {
        for (row in table) {
            test(row)
        }
    }

    data class Row(val inputs: Array<Boolean>, val outputs: Array<Boolean>)

}

fun truthTable(vararg table: Pair<Array<Int>, Array<Int>>) = TruthTable(table.map {
    TruthTable.Row(it.first.map { it != 0 }.toTypedArray(), it.second.map { it != 0 }.toTypedArray())
}.toTypedArray())