package com.xcodersteam.cpue.blocks

import com.xcodersteam.cpue.Simulation
import org.junit.Before

/**
 * Created by Semoro on 24.09.16.
 * ©XCodersTeam, 2016
 */
abstract class AbstractSimulationTest {
    @Before
    fun clearSimulation() {
        Simulation.clear()
    }

    fun simulateNSteps(steps: Int, e: Simulation.() -> Unit) {
        for (i in 1..steps) {
            Simulation.simulationStep(e)
        }
    }
}

