#!/bin/bash

# Check if the mode is passed as an argument
if [ -z "$1" ]; then
    echo "Please provide a mode: SingleProcessPlayer or NetworkPlayer"
    exit 1
fi

MODE=$1

# Debug: Print the mode
echo "Selected mode: $MODE"

# Build the project using Maven
mvn clean package

# Run the correct main method based on the provided mode
if [ "$MODE" = "SingleProcessPlayer" ]; then
    echo "Running SingleProcessMain..."
    java -cp target/playercommunication-1.0-SNAPSHOT.jar SingleProcessMain
elif [ "$MODE" = "NetworkPlayer" ]; then
    echo "Running NetworkPlayerMain..."
    java -cp target/playercommunication-1.0-SNAPSHOT.jar NetworkPlayerMain
else
    echo "Invalid mode. Please provide 'SingleProcessPlayer' or 'NetworkPlayer'."
    exit 1
fi
