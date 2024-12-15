package com.irs1318;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * GarageDoorMechanismTest verifies the behavior of our garage door control system.
 * 
 * This class demonstrates several important testing concepts:
 * 1. Unit Testing - Testing individual components in isolation
 * 2. Test Setup - Preparing a known state before each test
 * 3. Test Scenarios - Verifying specific behaviors and edge cases
 * 4. Assertions - Checking that code behaves as expected
 */
public class GarageDoorMechanismTest {
    private GarageDoorMechanism mechanism;
    private TestRoboRIO robotRIO;

    /**
     * The setUp method runs before each test.
     * This ensures each test starts with a fresh, known state:
     * - New mechanism instance
     * - New test RoboRIO instance
     * - No lingering state from previous tests
     */
    @Before
    public void setUp() {
        mechanism = new GarageDoorMechanism();
        robotRIO = new TestRoboRIO();
    }

    /**
     * Verifies that the door starts in the closed state.
     * This is a basic state test - checking initial conditions.
     */
    @Test
    public void testInitialState() {
        assertEquals("Door should start in closed state", 
            GarageDoorMechanism.DoorState.CLOSED, 
            mechanism.getCurrentState());
    }

    /**
     * Tests the door's response to a button press when closed.
     * This test verifies:
     * 1. State changes to OPENING
     * 2. Motor runs in the opening direction
     */
    @Test
    public void testOpeningFromClosed() {
        // Simulate button press
        robotRIO.setDigitalInput(3, true);
        mechanism.update(robotRIO);

        // Verify state change
        assertEquals("Door should be in opening state", 
            GarageDoorMechanism.DoorState.OPENING, 
            mechanism.getCurrentState());
        
        // Verify motor control
        assertEquals("Motor should be running forward", 
            1.0, robotRIO.getPWMOutput(0), 0.001);
    }

    /**
     * Tests that the door stops when it reaches the fully open position.
     * This test verifies:
     * 1. Door transitions to OPEN state
     * 2. Motor stops running
     */
    @Test
    public void testStopsWhenFullyOpen() {
        // Get door moving up
        robotRIO.setDigitalInput(3, true);
        mechanism.update(robotRIO);

        // Simulate open limit switch
        robotRIO.setDigitalInput(1, true);
        mechanism.update(robotRIO);

        // Verify final state
        assertEquals("Door should be in open state", 
            GarageDoorMechanism.DoorState.OPEN, 
            mechanism.getCurrentState());
        
        // Verify motor stopped
        assertEquals("Motor should be stopped", 
            0.0, robotRIO.getPWMOutput(0), 0.001);
    }

    /**
     * Tests the door's response to a button press when open.
     * This test verifies:
     * 1. State changes to CLOSING
     * 2. Motor runs in the closing direction
     */
    @Test
    public void testClosingFromOpen() {
        // Get to open state first
        robotRIO.setDigitalInput(3, true);
        mechanism.update(robotRIO);
        robotRIO.setDigitalInput(1, true);
        mechanism.update(robotRIO);

        // Press button to close
        robotRIO.setDigitalInput(3, true);
        mechanism.update(robotRIO);

        // Verify closing state
        assertEquals("Door should be in closing state", 
            GarageDoorMechanism.DoorState.CLOSING, 
            mechanism.getCurrentState());
        
        // Verify motor direction
        assertEquals("Motor should be running reverse", 
            -1.0, robotRIO.getPWMOutput(0), 0.001);
    }

    /**
     * Tests the safety feature when an obstacle is detected.
     * This test verifies:
     * 1. Door reverses when beam is broken
     * 2. Motor changes direction appropriately
     */
    @Test
    public void testBeamBreakWhileClosing() {
        // Get to closing state
        robotRIO.setDigitalInput(3, true);
        mechanism.update(robotRIO);
        robotRIO.setDigitalInput(1, true);
        mechanism.update(robotRIO);
        robotRIO.setDigitalInput(3, true);
        mechanism.update(robotRIO);

        // Simulate beam break
        robotRIO.setDigitalInput(0, true);
        mechanism.update(robotRIO);

        // Verify safety response
        assertEquals("Door should reverse to opening when beam broken", 
            GarageDoorMechanism.DoorState.OPENING, 
            mechanism.getCurrentState());
        
        // Verify motor reversed
        assertEquals("Motor should be running forward", 
            1.0, robotRIO.getPWMOutput(0), 0.001);
    }
}
