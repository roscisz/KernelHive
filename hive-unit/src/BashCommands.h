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
