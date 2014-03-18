/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Szymon Bultrowicz
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
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
