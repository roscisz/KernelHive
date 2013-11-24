/*
 * BashCommands.h
 *
 *  Created on: 12-07-2013
 *      Author: szymon
 */

#ifndef BASHCOMMANDS_H_
#define BASHCOMMANDS_H_

namespace KernelHive {

class BashCommands {
public:
	static const char* TOTAL_MEMORY_STATS_COMMAND;
	static const char* FREE_MEMORY_STATS_COMMAND;
	static const char* CPU_CORES_COMMAND;
	static const char* CPU_SPEED_COMMAND;
	static const char* NVIDIA_ID_COMMAND;
	static const char* NVIDIA_NAME_COMMAND;
	static const char* NVIDIA_GPU_COMMAND;
	static const char* NVIDIA_MEMORY_USED_COMMAND;
	static const char* NVIDIA_FAN_COMMAND;
};

}

#endif /* BASHCOMMANDS_H_ */
