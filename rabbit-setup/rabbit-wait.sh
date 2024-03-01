#!/bin/bash

# Function to wait for RabbitMQ to be ready
wait_for_rabbitmq() {
    echo "Waiting for RabbitMQ to be ready..."
    while ! nc -z rabbitmq 5672; do
        sleep 1
    done
    echo "RabbitMQ is ready"
}

# Call the wait_for_rabbitmq function
wait_for_rabbitmq

# Start your application
exec "$@"
