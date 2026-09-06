# Player Messaging System

A Java-based messaging system demonstrating **concurrency, thread-safe communication, TCP networking, and inter-process messaging**.

The project implements the same messaging abstraction in two different execution models:

* **Single-process communication** using threads and a `BlockingQueue`
* **Inter-process communication** using TCP sockets

The goal is to demonstrate how the same domain model can support different communication mechanisms while keeping the player abstraction consistent.

## Architecture

The system is built around an abstract `Player` component that defines the messaging lifecycle.

Two implementations provide different communication mechanisms:

* `SingleProcessPlayer` — communicates through an in-memory `BlockingQueue`
* `NetworkPlayer` — communicates through TCP sockets

Both implementations support sending, receiving, message counting, and graceful shutdown.
## Architecture

The system supports two communication models using the same `Player` abstraction:

```text
                    ┌─────────────────┐
                    │     Player      │
                    │   (Abstract)    │
                    └────────┬────────┘
                             │
                ┌────────────┴────────────┐
                │                         │
       ┌────────▼────────┐      ┌────────▼────────┐
       │ SingleProcess   │      │ NetworkPlayer   │
       │     Player      │      │                 │
       └────────┬────────┘      └────────┬────────┘
                │                         │
       ┌────────▼────────┐      ┌────────▼────────┐
       │ BlockingQueue   │      │   TCP Socket    │
       │  (in-memory)    │      │ (port 8085)     │
       └────────┬────────┘      └────────┬────────┘
                │                         │
          ┌─────▼─────┐            ┌─────▼─────┐
          │  Player A │            │  Player A │
          │     ↕     │            │     ↕     │
          │  Player B │            │  Player B │
          └───────────┘            └───────────┘
```

### Communication Models

**Single Process**

Two player instances run as separate threads and exchange messages through a thread-safe `BlockingQueue`.

**Network**

Two player instances communicate through TCP sockets, allowing them to run in separate Java processes.

## Key Concepts

This project demonstrates practical Java concepts including:

* Multithreading and concurrency
* Producer-consumer communication
* Thread-safe data structures with `BlockingQueue`
* Synchronization and atomic variables
* TCP socket communication
* Inter-process communication (IPC)
* Object serialization
* Graceful resource and thread shutdown
* Abstraction and separation of communication mechanisms

## Project Structure

```text
src/main/java/
├── Message.java
├── MessageType.java
├── Player.java
├── SingleProcessPlayer.java
├── SingleProcessMain.java
├── NetworkPlayer.java
└── NetworkPlayerMain.java
```

## Running the Project

### Single Process

Runs two players as separate threads communicating through an in-memory queue.

```bash
./run.sh SingleProcessPlayer
```

### Network Mode

Runs players using TCP socket communication.

```bash
./run.sh NetworkPlayer
```

The network implementation uses `localhost:8085` for communication.

## Why This Project

This project started as an exploration of different approaches to communication between Java components.

It demonstrates how the communication mechanism can change — from an in-memory queue to TCP sockets — while the core `Player` abstraction remains consistent.

## Technologies

* Java
* Maven
* TCP/IP
* Java Concurrency
* Sockets
* Inter-Process Communication
