# Garage Door Mechanism

This project implements a garage door control mechanism that manages the states and operations of a garage door system.

## Overview

The garage door mechanism handles:
- Opening and closing the door via a button press
- Safety features using a through-beam sensor
- State management (Open, Closing, Closed, Opening)
- Motor control based on current state
- Limit switch inputs for fully open/closed positions

## Project Structure

```
src/
├── main/java/com/irs1318/
│   ├── GarageDoorMechanism.java  - Main control logic
│   └── IRoboRIO.java            - Interface for robot I/O
└── test/java/com/irs1318/
    ├── TestRoboRIO.java         - Test implementation of IRoboRIO
    └── GarageDoorMechanismTest.java - Unit tests
```

## Implementation Details

The garage door mechanism implements a state machine with four states:
1. OPEN - Door is fully open
2. CLOSING - Door is moving down
3. CLOSED - Door is fully closed
4. OPENING - Door is moving up

State transitions are triggered by:
- Button presses
- Limit switch activations (fully open/closed positions)
- Through-beam sensor interruptions (safety feature)

The motor control follows these rules:
- Positive power (1.0) when opening
- Negative power (-1.0) when closing
- No power (0.0) when fully open or closed

## Building and Testing

Requirements:
- Java JDK 11 or higher
- Maven

To build and run tests:
```bash
mvn clean test
```

## Integration

To integrate this code with a real robot:
1. Implement the IRoboRIO interface for your specific hardware
2. Create an instance of GarageDoorMechanism
3. Call the update() method periodically with your RoboRIO implementation

Example:
```java
IRoboRIO robotRIO = new YourRoboRIOImplementation();
GarageDoorMechanism mechanism = new GarageDoorMechanism();

// In your periodic update loop:
mechanism.update(robotRIO);
