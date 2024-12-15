package com.irs1318;

/**
 * IRoboRIO defines the interface for interacting with robot hardware.
 * 
 * This interface demonstrates several important programming concepts:
 * 1. Hardware Abstraction - Separating hardware details from control logic
 * 2. Interface Design - Defining a contract that implementations must follow
 * 3. Dependency Inversion - High-level modules depend on abstractions
 * 
 * By using an interface, we can:
 * - Test our logic without real hardware (using a test implementation)
 * - Support different hardware without changing control logic
 * - Make our code more modular and maintainable
 */
public interface IRoboRIO {
    /**
     * Gets the value of a digital input channel.
     * 
     * Digital inputs are boolean sensors that can be either:
     * - true (HIGH/1/on)
     * - false (LOW/0/off)
     * 
     * Examples of digital inputs include:
     * - Limit switches (pressed/not pressed)
     * - Beam break sensors (blocked/not blocked)
     * - Push buttons (pressed/not pressed)
     * 
     * @param channel The channel number to read from
     * @return true if the input is HIGH, false if LOW
     */
    boolean getDigital(int channel);

    /**
     * Sets the PWM (Pulse Width Modulation) output value for a channel.
     * 
     * PWM is used to control motor speed:
     * - 1.0 means full power forward
     * - 0.0 means no power (stopped)
     * - -1.0 means full power reverse
     * 
     * Values between these numbers give proportional power:
     * - 0.5 would be half power forward
     * - -0.5 would be half power reverse
     * 
     * @param channel The channel number to write to
     * @param value The power value (-1.0 to 1.0)
     */
    void setPWM(int channel, double value);
}
