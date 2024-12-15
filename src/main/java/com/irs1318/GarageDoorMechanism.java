package com.irs1318;

/**
 * GarageDoorMechanism implements a state machine to control a garage door.
 * 
 * This class demonstrates several important programming concepts:
 * 1. State Machines - Using enums and switch statements to manage different states
 * 2. Hardware Abstraction - Using interfaces to separate hardware details from logic
 * 3. Safety Systems - Implementing safety features like the through-beam sensor
 */
public class GarageDoorMechanism {
    /**
     * DoorState represents all possible states of the garage door.
     * Using an enum ensures we can only be in one of these defined states:
     * - OPEN: Door is fully open and not moving
     * - CLOSING: Door is moving downward
     * - CLOSED: Door is fully closed and not moving
     * - OPENING: Door is moving upward
     */
    public enum DoorState {
        OPEN,
        CLOSING,
        CLOSED,
        OPENING
    }

    // The current state of the door - this is what we update based on inputs
    private DoorState currentState;

    /**
     * Channel numbers for various inputs and outputs.
     * These map to physical connections on the RoboRIO:
     * - OPEN_SENSOR: Limit switch that activates when door is fully open
     * - CLOSED_SENSOR: Limit switch that activates when door is fully closed
     * - THROUGH_BEAM_SENSOR: Safety sensor that detects obstacles
     * - BUTTON: User input to trigger door operation
     * - MOTOR: Output to control door movement
     */
    private final int OPEN_SENSOR_CHANNEL = 1;
    private final int CLOSED_SENSOR_CHANNEL = 2;
    private final int THROUGH_BEAM_SENSOR_CHANNEL = 0;
    private final int BUTTON_CHANNEL = 3;
    private final int MOTOR_CHANNEL = 0;

    /**
     * Constructor initializes the door in the CLOSED state.
     * This is a safe default state to start in.
     */
    public GarageDoorMechanism() {
        this.currentState = DoorState.CLOSED;
    }

    /**
     * Main update method that should be called periodically.
     * This method:
     * 1. Reads all sensor inputs
     * 2. Determines if state should change based on inputs
     * 3. Updates motor output based on new state
     *
     * @param robotRIO Interface to hardware inputs/outputs
     */
    public void update(IRoboRIO robotRIO) {
        // Read all sensor inputs first
        boolean isOpen = robotRIO.getDigital(OPEN_SENSOR_CHANNEL);
        boolean isClosed = robotRIO.getDigital(CLOSED_SENSOR_CHANNEL);
        boolean isBlocked = robotRIO.getDigital(THROUGH_BEAM_SENSOR_CHANNEL);
        boolean buttonPressed = robotRIO.getDigital(BUTTON_CHANNEL);

        // State machine implementation
        // Each case handles transitions from that state based on inputs
        switch (currentState) {
            case OPEN:
                // When open, the only transition is to CLOSING when button is pressed
                if (buttonPressed) {
                    currentState = DoorState.CLOSING;
                }
                break;

            case CLOSING:
                // While closing, we can transition to:
                // 1. OPENING if beam is broken (safety feature)
                // 2. CLOSED if we reach the closed position
                if (isBlocked) {
                    currentState = DoorState.OPENING;  // Safety feature
                }
                else if (isClosed) {
                    currentState = DoorState.CLOSED;   // Reached closed position
                }
                break;

            case CLOSED:
                // When closed, the only transition is to OPENING when button is pressed
                if (buttonPressed) {
                    currentState = DoorState.OPENING;
                }
                break;

            case OPENING:
                // When opening, the only transition is to OPEN when fully open
                if (isOpen) {
                    currentState = DoorState.OPEN;
                }
                break;
        }

        // After updating state, set motor power accordingly
        updateMotor(robotRIO);
    }

    /**
     * Sets motor power based on current state.
     * This demonstrates a simple output pattern:
     * - Positive power (1.0) to open
     * - Negative power (-1.0) to close
     * - No power (0.0) when stopped
     *
     * @param robotRIO Interface to hardware outputs
     */
    private void updateMotor(IRoboRIO robotRIO) {
        switch (currentState) {
            case OPENING:
                robotRIO.setPWM(MOTOR_CHANNEL, 1.0);   // Full power up
                break;
            case CLOSING:
                robotRIO.setPWM(MOTOR_CHANNEL, -1.0);  // Full power down
                break;
            default:  // OPEN or CLOSED
                robotRIO.setPWM(MOTOR_CHANNEL, 0.0);   // No power when stopped
                break;
        }
    }

    /**
     * Getter for current state - useful for testing and monitoring.
     * @return Current state of the door
     */
    public DoorState getCurrentState() {
        return currentState;
    }
}
