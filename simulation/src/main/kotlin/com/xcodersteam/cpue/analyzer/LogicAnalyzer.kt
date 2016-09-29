package com.xcodersteam.cpue.analyzer

import com.xcodersteam.cpue.blocks.AbstractBus
import com.xcodersteam.cpue.simulation.Node
import java.io.File
import java.io.Writer
import java.util.*

/**
 * Created by Dimach on 29.09.16.
 * ©XCodersTeam, 2016
 */
class LogicAnalyzer(val file: File) {
    private val allVars = LinkedList<Var>()
    val modules = LinkedList<Module>()
    val changeDump = LinkedList<String>()

    fun update(tickNumber: Int) {
        changeDump.add("#$tickNumber")
        if (tickNumber == 0) {
            allVars.forEach { it.firstTick(changeDump) }
        } else {
            allVars.forEach { it.update(changeDump) }
        }
    }

    fun save() {
        val writer = file.writer()
        writer.run {
            write("\$date ${Date()} \$end")
            write("\$version KVirtualCPU V0.0.0 \$end")
            write("\$timescale 1 ns \$end")
            write("\$enddefinitions \$end")
            changeDump.forEach(writer::write)
            flush()
            close()
        }
    }

    fun module(name: String, init: Module.() -> Unit) {
        modules.add(Module(name, init, this))
    }

    class Module(val name: String, init: Module.() -> Unit, val ana: LogicAnalyzer) {
        init {
            init()
        }

        val n = LinkedList<Var>()
        val m = LinkedList<Module>()

        fun link(node: Node, name: String) {
            val j = NodeVar(node, name, "_" + ana.allVars.size)
            n.add(j)
            ana.allVars.add(j)
        }

        fun link(bus: AbstractBus, name: String) {
            val j = BusVar(bus, name, "_" + ana.allVars.size)
            n.add(j)
            ana.allVars.add(j)
        }

        fun module(name: String, init: Module.() -> Unit) {
            m.add(Module(name, init, ana))
        }

        fun writeVCDHeader(writer: Writer) {
            writer.run {
                write("\$scope module $name \$end")
                m.forEach { it.writeVCDHeader(writer) }
                n.forEach { it.writeVCDHeader(writer) }
                write("\$upscope \$end")
            }
        }
    }

    abstract class Var(val name: String, val short: String) {
        abstract fun update(changeDump: LinkedList<String>)
        abstract fun firstTick(changeDump: LinkedList<String>)
        abstract fun writeVCDHeader(writer: Writer)
    }

    class NodeVar(val node: Node, name: String, short: String) : Var(name, short) {
        override fun writeVCDHeader(writer: Writer) {
            writer.write("\$var wire 1 $short $name \$end")
        }

        override fun firstTick(changeDump: LinkedList<String>) {
            changeDump.add("${if (node.isPowered) 1 else 0}$short")
            prev = node.isPowered
        }

        var prev: Boolean = false

        override fun update(changeDump: LinkedList<String>) {
            if (prev != node.isPowered) {
                changeDump.add("${if (node.isPowered) 1 else 0}$short")
                prev = node.isPowered
            }
        }
    }

    class BusVar(val bus: AbstractBus, name: String, short: String) : Var(name, short) {
        override fun writeVCDHeader(writer: Writer) {
            writer.write("\$var wire ${bus.bits} $short $name \$end")
        }

        override fun firstTick(changeDump: LinkedList<String>) {
            bus.nodes.forEachIndexed { i, node -> prev[i] = node.isPowered }
        }

        val prev = BooleanArray(bus.bits)

        override fun update(changeDump: LinkedList<String>) {
            var equals = true
            for (i in 0..prev.lastIndex) {
                if (prev[i] != bus.nodes[i].isPowered) {
                    equals = false
                    prev[i] = bus.nodes[i].isPowered
                }
            }
            if (!equals) {
                changeDump.add(prev.joinToString(separator = "", prefix = "b", postfix = short) { if (it) "1" else "0" })
            }
        }
    }
}