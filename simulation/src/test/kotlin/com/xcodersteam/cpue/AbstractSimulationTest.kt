package com.xcodersteam.cpue

import com.xcodersteam.cpue.analyzer.LogicAnalyzer
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.File

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */
abstract class AbstractSimulationTest {

    @Rule
    @JvmField
    var collector = SpecialErrorCollector()

    var tick = 0
    open var writeVCD: Boolean = false
    open var writeVCDonFail: Boolean = true

    lateinit var logicAnalyzer: LogicAnalyzer

    @Rule
    @JvmField
    var testWatcher = object : TestWatcher() {

        fun save(description: Description) {
            val vcdDir = File("vcd")
            if (!vcdDir.exists())
                vcdDir.mkdir()
            logicAnalyzer.save(File(vcdDir, "${description.testClass.simpleName}_${description.methodName}.vcd"))
        }

        var VCD = false
        var VCDonFail = true

        override fun starting(description: Description?) {
            super.starting(description)
            VCD = writeVCD
            VCDonFail = writeVCDonFail
        }

        override fun finished(description: Description?) {
            super.finished(description)
            writeVCD = VCD
            writeVCDonFail = VCDonFail
            println("Statistics:")
            println("\ttransistors:${Simulation.allTransistors.size}")
            println("\tnodes:${Simulation.allNodes.size}")
            println("\tlinks:${Simulation.allLinks.size}")
            println("\tticks:$tick")
        }

        override fun succeeded(description: Description) {
            super.succeeded(description)
            if (writeVCD)
                save(description)
        }

        override fun failed(e: Throwable?, description: Description) {
            super.failed(e, description)
            if (writeVCD || writeVCDonFail)
                save(description)
        }
    }

    @Before
    fun clearSimulation() {
        tick = 0
        Simulation.clear()
        logicAnalyzer = LogicAnalyzer()
    }


    fun simulateNSteps(steps: Int, e: Simulation.() -> Unit) {
        for (i in 1..steps) {
            Simulation.simulationStep(e)
            logicAnalyzer.update(tick++)
        }
    }
}

