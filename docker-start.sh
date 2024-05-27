#!/bin/bash

# Function to attach to the Docker container
attach_to_container() {
    local container_id=$1
    echo "Attaching to UI container..."
    # Check if the script is running on a Windows environment
    if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
        winpty docker attach "$container_id"
    else
        docker attach "$container_id"
    fi
}

# Step 1: Check if the beads_ui container is already running
uiContainerId=$(docker-compose ps -q ui)
if [ -n "$uiContainerId" ] && [ "$(docker inspect --format '{{.State.Status}}' "$uiContainerId")" == "running" ]; then
    echo "UI container is already running."
    docker-compose restart ui
else
    # Step 2: Build the Docker images if containers are not running
    echo "Building Docker images..."
    sbt controller/docker:publishLocal
    sbt ui/docker:publishLocal

    # Step 3: Start the Docker Compose stack
    echo "Starting Docker Compose services..."
    docker-compose up -d

    # Step 4: Wait for the beads_ui container to be running
    uiContainerId=$(docker-compose ps -q ui)
    start_time=$(date +%s)
    max_wait_time=300

    while [ -z "$uiContainerId" ] || [ "$(docker inspect --format '{{.State.Status}}' "$uiContainerId")" != "running" ]; do
        current_time=$(date +%s)
        elapsed_time=$((current_time - start_time))

        if [ "$elapsed_time" -ge "$max_wait_time" ]; then
            echo "Error: beads_ui container did not start within 5 minutes."
            exit 1
        fi

        echo "Waiting for beads_ui container to be running..."
        sleep 1
        uiContainerId=$(docker-compose ps -q ui)
    done
fi

# Step 5: Attach to the beads_ui container
if [ -z "$uiContainerId" ]; then
    echo "Error: beads_ui container is not running."
    exit 1
fi

attach_to_container "$uiContainerId"
