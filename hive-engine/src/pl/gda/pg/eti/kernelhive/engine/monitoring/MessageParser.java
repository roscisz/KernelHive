/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.engine.monitoring;

import pl.gda.pg.eti.kernelhive.engine.monitoring.messages.UnitDefinition;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import pl.gda.pg.eti.kernelhive.engine.monitoring.messages.MonitoringMessage;
import pl.gda.pg.eti.kernelhive.engine.monitoring.messages.SequentialMessage;

/**
 *
 * @author szymon
 */
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