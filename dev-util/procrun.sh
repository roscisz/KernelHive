#!/usr/bin/env sh

# The DataProcessor executable path:
EXECUTABLE="../build/hive-worker/DataProcessor"

# The host address:
HOST="localhost"

# Port definitions:
CLUSTER_PORT="31338"
DATA_PORT="31340"
KERNEL_PORT="9012"

# Data identifiers:
DATA_ID="123"
KERNEL_ID="456"

# The device to use for execution:
DEVICE_ID="GeForce9400MG"

# Kernel parameters:
NUM_DIMENSIONS="1"
OFFSET="0"
GLOBAL_SIZE="4096"
LOCAL_SIZE="64"

#DataProcessor 10 hive-cluster2 31338 31340 NVIDIACorporationGeForce9400MG 3 0 0 0 512 1 1 64 1 1 2048 hive-cluster2 31339 1 1 hive-cluster2 31339 1 1 hive-cluster2 31339

echo $EXECUTABLE $HOST $CLUSTER_PORT $HOST $DATA_PORT $DATA_ID $HOST $KERNEL_PORT $KERNEL_ID $DEVICE_ID $NUM_DIMENSIONS $OFFSET $GLOBAL_SIZE $LOCAL_SIZ
eval $EXECUTABLE $HOST $CLUSTER_PORT $HOST $DATA_PORT $DATA_ID $HOST $KERNEL_PORT $KERNEL_ID $DEVICE_ID $NUM_DIMENSIONS $OFFSET $GLOBAL_SIZE $LOCAL_SIZE

