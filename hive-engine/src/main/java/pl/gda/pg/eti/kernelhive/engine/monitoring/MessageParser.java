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
package pl.gda.pg.eti.kernelhive.engine.monitoring;

import pl.gda.pg.eti.kernelhive.engine.monitoring.messages.UnitDefinition;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import pl.gda.pg.eti.kernelhive.engine.monitoring.messages.MonitoringMessage;
import pl.gda.pg.eti.kernelhive.engine.monitoring.messages.SequentialMessage;

public class MessageParser {

	private static final Logger logger = Logger.getLogger(MessageParser.class.getName());

	public MonitoringMessage parseMessage(String text) {
		return parseMessage(text.getBytes());
	}

	public MonitoringMessage parseMessage(byte[] bytes) {
		MessageExtractor extractor = new MessageExtractor(bytes);
		int clusterId = extractor.extractInt(IntegerSize.SHORT_INT);
		int messageType = extractor.extractInt(IntegerSize.BYTE);
		int unitId = extractor.extractInt(IntegerSize.SHORT_INT);

		switch (MonitoringMessageType.fromValue(messageType)) {
			case INITIAL:
				return parseInitialMessage(extractor, clusterId, unitId);
			case SEQUENTIAL:
				return parseSequentialMessage(extractor, clusterId, unitId);
			default:
				logger.severe("Wrong message type");
				return null;
		}
	}

	public UnitDefinition parseInitialMessage(MessageExtractor extractor,
			int clusterId, int unitId) {
		//logger.info("Parsing initial message");
        /*String tmp = "Full message: ";
		 for(int i = 0; i < bytes.length && i < 1024; i++) {
		 tmp += String.format("%02X", bytes[i]);
		 }
		 logger.severe(tmp);*/

		int cpuCores = extractor.extractInt(IntegerSize.SHORT_INT);
		int memorySize = extractor.extractInt(IntegerSize.INT);
		int gpuDevicesCount = extractor.extractInt(IntegerSize.SHORT_INT);

		logger.info(String.format("Parsed initial message from %s/%s: %s cores, %sB memory, %s device(s)",
				clusterId, unitId, cpuCores, memorySize, gpuDevicesCount));

		UnitDefinition initialMessage = new UnitDefinition();
		initialMessage.setUnitId(unitId);
		initialMessage.setClusterId(clusterId);
		initialMessage.setCpuCount(cpuCores);
		initialMessage.setMemorySize(memorySize);
		initialMessage.setGpuDevicesCount(gpuDevicesCount);

		return initialMessage;
	}

	public SequentialMessage parseSequentialMessage(MessageExtractor extractor,
			int clusterId, int unitId) {
		//logger.info("Parsing sequential message");
        /*String tmp = "Full message: ";
		 for(int i = 0; i < bytes.length && i < 1024; i++) {
		 tmp += String.format("%02X", bytes[i]);
		 }
		 logger.severe(tmp);*/

		int clockSpeed = extractor.extractInt(IntegerSize.SHORT_INT);
		int coresCount = extractor.extractInt(IntegerSize.SHORT_INT);

		List<Integer> cpuUsages = new ArrayList<>(coresCount);
		for (int i = 0; i < coresCount; i++) {
			int cpuUsage = extractor.extractInt(IntegerSize.SHORT_INT);
			cpuUsages.add(cpuUsage);
		}

		int memoryUsed = extractor.extractInt(IntegerSize.INT);
		int gpuDevicesCount = extractor.extractInt(IntegerSize.SHORT_INT);

		/*logger.info(String.format("Parsed sequential message: %s clock speed, %sB usages, %s memoryused, %d devices",
		 clockSpeed, cpuUsages.toString(), memoryUsed, gpuDevicesCount));*/

		SequentialMessage message = new SequentialMessage();
		message.setClusterId(clusterId);
		message.setUnitId(unitId);
		message.setClockSpeed(clockSpeed);
		message.setCpuCores(coresCount);
		message.setCpuUsage(cpuUsages);
		message.setMemoryUsed(memoryUsed);

		for (int i = 0; i < gpuDevicesCount; i++) {
			DeviceCyclicData gpu = new DeviceCyclicData();
			gpu.setClusterId(clusterId);
			gpu.setUntiId(unitId);
			gpu.setDeviceId(extractor.extractInt(IntegerSize.SHORT_INT));
			gpu.setMemoryUsed(extractor.extractInt(IntegerSize.INT));
			gpu.setGpuUsage(extractor.extractInt(IntegerSize.SHORT_INT));
			gpu.setFanSpeed(extractor.extractInt(IntegerSize.SHORT_INT));
			message.getGpuDevices().add(gpu);
		}

		return message;
	}
}
