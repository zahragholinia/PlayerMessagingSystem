# Player Messaging System

## Overview

This project simulates a messaging system where two players (instances of the `Player` class) can communicate with each other in a simple message-passing system. The goal is to design a system where players can send and receive messages in a loop until a specific condition is met. The system will be implemented using **pure Java** (without additional frameworks such as Spring), and both players will initially run in the **same process**. In a later phase, each player will run in its own separate Java process.

## Requirements

### 1. **Create Two Players**

* Two instances of the `Player` class must be created.
* One of the players will act as the "initiator" that starts the communication.

### 2. **Message Flow**

* The initiator sends a message to the second player.
* When a player receives a message, it appends the message counter and sends the updated message back.
* The communication continues until the initiator has sent 10 messages and received 10 messages, at which point the program will stop.

### 3. **Graceful Termination**

* The program should gracefully terminate after the initiator sends and receives 10 messages.

### 4. **Running in the Same Java Process (Initial Phase)**

* Both players should run in the same Java process during the initial phase of the project.

### 5. **Maven Project**

* This project should be a Maven project with source code only (no packaged JAR files).
* A shell script will be provided to run the project.

### 6. **Documentation**

* Every class will be thoroughly documented with clear responsibilities and design decisions.

### 7. **opposite to 4: have every player in a separate JAVA process (different PID).**


---
