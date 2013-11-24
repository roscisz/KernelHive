/*
 * BashCommands.cpp
 *
 *  Created on: 12-07-2013
 *      Author: szymon
 */

#include "BashCommands.h"

namespace KernelHive {

const char* BashCommands::TOTAL_MEMORY_STATS_COMMAND = "grep MemTotal /proc/meminfo | perl -n -e'/(\\d+)/ && print $1'";
const char* BashCommands::FREE_MEMORY_STATS_COMMAND = "head -n 4 /proc/meminfo | tail -n 3 | awk '{s+=$2}END{print s}'";
const char* BashCommands::CPU_CORES_COMMAND = "grep 'cpu cores' /proc/cpuinfo | head -n 1 | awk '{ print $4 }'";
const char* BashCommands::CPU_SPEED_COMMAND = "grep 'cpu MHz' /proc/cpuinfo | head -n 1 | awk '{ print $4 }'";
const char* BashCommands::NVIDIA_ID_COMMAND = "nvidia-smi -L | grep '%s' | awk '{print $2}' | tr -d [:punct:]";
const char* BashCommands::NVIDIA_NAME_COMMAND = "nvidia-smi -i %d -q | grep 'product Name' | awk '{ print $3 }'";
const char* BashCommands::NVIDIA_GPU_COMMAND = "nvidia-smi -i %d -q -d utilization | grep Gpu | awk '{ print $3 }'";
const char* BashCommands::NVIDIA_MEMORY_USED_COMMAND = "nvidia-smi -i %d -q -d Memory | grep Used | awk '{ print $3 }'";
const char* BashCommands::NVIDIA_FAN_COMMAND = "nvidia-smi -i %d -q | grep 'Fan Speed' | awk '{ print $4 }'";

}
