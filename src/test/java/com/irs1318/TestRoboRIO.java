package com.irs1318;

import java.util.HashMap;
import java.util.Map;

/**
 * TestRoboRIO provides a test implementation of IRoboRIO.
 * 
 * This class demonstrates several important testing concepts:
 * 1. Test Doubles - Creating a fake version of hardware for testing
 * 2. State Tracking - Recording what happens during tests
 * 3. Controllable Inputs - Allowing tests to set up specific scenarios
 * 
 * Using this test implementation, we can:
 * - Verify our logic without real hardware
 * - Test edge cases and error conditions safely
 * - Automate our tests for continuous integration
 */
public class TestRoboRIO implements IRoboRIO {
    // Maps to store the state of our simulated hardware
    private Map<Integer, Boolean> digitalInputs;  // Stores digital input values
    private Map<Integer, Double> pwmOutputs;      // Stores motor output values

    /**
     * Constructor initializes empty maps for our simulated I/O.
     * All inputs default to false (LOW) and outputs to 0.0 (stopped).
     */
    public TestRoboRIO() {
        digitalInputs = new HashMap<>();
        pwmOutputs = new HashMap<>();
    }

    /**
     * Implements getDigital from IRoboRIO.
     * Returns the simulated value for the specified channel.
     * If no value has been set, returns false (LOW) as a safe default.
     */
    @Override
    public boolean getDigital(int channel) {
        return digitalInputs.getOrDefault(channel, false);
    }

    /**
     * Implements setPWM from IRoboRIO.
     * Stores the output value for later verification in tests.
     */
    @Override
    public void setPWM(int channel, double value) {
        pwmOutputs.put(channel, value);
    }

    /**
     * Test helper method to simulate a digital input.
     * This lets us set up specific test scenarios, like:
     * - Simulating a pressed limit switch
     * - Simulating a blocked beam sensor
     * - Simulating a button press
     */
    public void setDigitalInput(int channel, boolean value) {
        digitalInputs.put(channel, value);
    }

    /**
     * Test helper method to check what power was sent to a motor.
     * This lets us verify our logic is controlling outputs correctly:
     * - Check motor direction (positive/negative)
     * - Verify motor stops (zero)
     * - Ensure proper power levels
     */
    public double getPWMOutput(int channel) {
        return pwmOutputs.getOrDefault(channel, 0.0);
    }
}
