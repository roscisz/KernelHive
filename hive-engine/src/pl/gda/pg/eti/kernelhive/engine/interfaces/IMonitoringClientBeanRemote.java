package pl.gda.pg.eti.kernelhive.engine.interfaces;

import java.util.List;
import javax.ejb.Remote;
import pl.gda.pg.eti.kernelhive.common.monitoring.service.PreviewObject;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.ClusterDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.DeviceDefinition;
import pl.gda.pg.eti.kernelhive.engine.monitoring.MonitoredEntity;
import pl.gda.pg.eti.kernelhive.engine.monitoring.dao.UnitDefinition;

@Remote
public interface IMonitoringClientBeanRemote {

	String getGraphPath(MonitoredEntity entity);

	List<UnitDefinition> getUnits();

	List<UnitDefinition> getUnitsForCluster(int clusterId);

	List<ClusterDefinition> getClusters();

	List<DeviceDefinition> getDevices(int clusterId, int unitId);

	List<DeviceDefinition> getAllDevices();

	List<PreviewObject> getPreviewData(int workflowId);
}
