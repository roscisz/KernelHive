#!/usr/bin/env sh

# The DataProcessor executable path:
EXECUTABLE="../build/hive-worker/DataProcessor"

# The host address:
HOST="localhost"

# Port definitions:
CLUSTER_PORT="1234"
DATA_PORT="5678"
KERNEL_PORT="9012"

# Data identifiers:
DATA_ID="123"
KERNEL_ID="456"

# The device to use for execution:
DEVICE_ID="\"GeForce 9400M G\""

# Kernel parameters:
NUM_DIMENSIONS="1"
OFFSET="0"
GLOBAL_SIZE="4096"
LOCAL_SIZE="64"

eval $EXECUTABLE $HOST $CLUSTER_PORT $HOST $DATA_PORT $DATA_ID $HOST $KERNEL_PORT $KERNEL_ID $DEVICE_ID $NUM_DIMENSIONS $OFFSET $GLOBAL_SIZE $LOCAL_SIZE

