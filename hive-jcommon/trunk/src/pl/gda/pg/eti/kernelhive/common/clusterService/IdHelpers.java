/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.common.clusterService;

/**
 *
 * @author szymon
 */
public class IdHelpers {

	public static Long getUnitId(int unitId, int clusterId) {
		return Long.valueOf(10000 * clusterId + unitId);
	}

	public static Long getDeviceId(int unitId, int clusterId, int deviceId) {
		return Long.valueOf((1000 * clusterId + unitId) * 100 + deviceId);
	}
}
