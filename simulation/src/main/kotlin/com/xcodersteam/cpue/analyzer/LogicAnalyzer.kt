package com.xcodersteam.cpue.analyzer

import com.xcodersteam.cpue.blocks.AbstractBus
import com.xcodersteam.cpue.simulation.Node
import java.io.File
import java.io.PrintWriter
import java.util.*

/**
 * Created by Dimach on 29.09.16.
 * ©XCodersTeam, 2016
 */
class LogicAnalyzer() {
    private val allVars = LinkedList<Var>()
    val modules = LinkedList<Module>()
    val changeDump = LinkedList<String>()

    fun update(tickNumber: Int) {
        if (tickNumber > 0 && changeDump.last.startsWith("#"))
            changeDump.removeLast()
        changeDump.add("#$tickNumber")
        if (tickNumber == 0) {
            allVars.forEach { it.firstTick(changeDump) }
        } else {
            allVars.forEach { it.update(changeDump) }
        }
    }

    fun save(file: File) {
        if (modules.isNotEmpty()) {
            val writer = file.printWriter()
            writer.run {
                println("\$date ${Date()} \$end")
                println("\$version KVirtualCPU V0.0.0 \$end")
                println("\$timescale 1 ns \$end")
                modules.forEach { it.writeVCDHeader(writer) }
                println("\$enddefinitions \$end")
                changeDump.forEach(writer::println)
                flush()
                close()
            }
        }
    }

    fun module(name: String, init: Module.() -> Unit) {
        modules.add(Module(name, init, this))
    }

    class Module(val name: String, init: Module.() -> Unit, val ana: LogicAnalyzer) {


        val n = LinkedList<Var>()
        val m = LinkedList<Module>()

        fun wire(node: Node, name: String) {
            val j = NodeVar(node, name, "_" + ana.allVars.size)
            n.add(j)
            ana.allVars.add(j)
        }

        fun bus(bus: AbstractBus, name: String) {
            val j = BusVar(bus, name, "_" + ana.allVars.size)
            n.add(j)
            ana.allVars.add(j)
        }

        fun reg(bus: AbstractBus, name: String) {
            val j = RegVar(bus, name, "_" + ana.allVars.size)
            n.add(j)
            ana.allVars.add(j)
        }

        fun module(name: String, init: Module.() -> Unit) {
            m.add(Module(name, init, ana))
        }

        fun error(name: String): ErrorVar {
            val v = ErrorVar(name, "_" + ana.allVars.size)
            n.add(v)
            ana.allVars.add(v)
            return v
        }

        fun writeVCDHeader(writer: PrintWriter) {
            writer.run {
                println("\$scope module $name \$end")
                m.forEach { it.writeVCDHeader(writer) }
                n.forEach { it.writeVCDHeader(writer) }
                println("\$upscope \$end")
            }
        }

        init {
            init()
        }
    }

    abstract class Var(val name: String, val short: String) {
        abstract fun update(changeDump: LinkedList<String>)
        abstract fun firstTick(changeDump: LinkedList<String>)
        abstract fun writeVCDHeader(writer: PrintWriter)
    }

    class NodeVar(val node: Node, name: String, short: String) : Var(name, short) {
        override fun writeVCDHeader(writer: PrintWriter) {
            writer.println("\$var wire 1 $short $name \$end")
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

    class RegVar(bus: AbstractBus, name: String, short: String) : BusVar(bus, name, short) {
        override fun writeVCDHeader(writer: PrintWriter) {
            writer.println("\$var reg ${bus.bits} $short $name \$end")
        }
    }

    class ErrorVar(name: String, short: String) : Var(name, short) {
        var errorOnTick = false
        var errorOnPrevTick = false
        override fun update(changeDump: LinkedList<String>) {
            if (errorOnPrevTick) {
                changeDump.add("0$short")
                errorOnPrevTick = false
            }
            if (errorOnTick) {
                changeDump.add("1$short")
                errorOnTick = false
                errorOnPrevTick = true
            }
        }

        override fun firstTick(changeDump: LinkedList<String>) {
            changeDump.add("0$short")
        }

        override fun writeVCDHeader(writer: PrintWriter) {
            writer.println("\$var wire 1 $short $name \$end")
        }
    }

    open class BusVar(val bus: AbstractBus, name: String, short: String) : Var(name, short) {

        val nodes = bus.nodes

        open override fun writeVCDHeader(writer: PrintWriter) {
            writer.println("\$var wire ${bus.bits} $short $name \$end")
        }

        override fun firstTick(changeDump: LinkedList<String>) {
            nodes.forEachIndexed { i, node -> prev[i] = node.isPowered }
            changeDump.add(prev.joinToString(separator = "", prefix = "b", postfix = " " + short) { if (it) "1" else "0" })
        }

        val prev = BooleanArray(bus.bits)

        override fun update(changeDump: LinkedList<String>) {
            var equals = true
            for (i in 0..prev.lastIndex) {
                if (prev[i] != nodes[i].isPowered) {
                    equals = false
                    prev[i] = nodes[i].isPowered
                }
            }
            if (!equals) {
                changeDump.add(prev.joinToString(separator = "", prefix = "b", postfix = " " + short) { if (it) "1" else "0" })
            }
        }
    }
}