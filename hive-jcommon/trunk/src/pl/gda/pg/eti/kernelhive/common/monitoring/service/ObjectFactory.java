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
package pl.gda.pg.eti.kernelhive.common.monitoring.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * pl.gda.pg.eti.kernelhive.common.monitoring.service package.
 * <p>An ObjectFactory allows you to programatically construct new instances of
 * the Java representation for XML content. The Java representation of XML
 * content can consist of schema derived interfaces and classes representing the
 * binding of schema type definitions, element declarations and model groups.
 * Factory methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _GetClustersResponse_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getClustersResponse");
	private final static QName _GetUnitsResponse_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getUnitsResponse");
	private final static QName _GetUnits_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getUnits");
	private final static QName _GetGraphPath_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getGraphPath");
	private final static QName _GetDevicesResponse_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getDevicesResponse");
	private final static QName _GetAllDevices_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getAllDevices");
	private final static QName _GetUnitsForCluster_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getUnitsForCluster");
	private final static QName _GetUnitsForClusterResponse_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getUnitsForClusterResponse");
	private final static QName _GetGraphPathResponse_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getGraphPathResponse");
	private final static QName _GetClusters_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getClusters");
	private final static QName _GetPreviewDataResponse_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getPreviewDataResponse");
	private final static QName _GetAllDevicesResponse_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getAllDevicesResponse");
	private final static QName _GetDevices_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getDevices");
	private final static QName _GetPreviewData_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "getPreviewData");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * pl.gda.pg.eti.kernelhive.common.monitoring.service
	 *
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link GetUnitsResponse }
	 *
	 */
	public GetUnitsResponse createGetUnitsResponse() {
		return new GetUnitsResponse();
	}

	/**
	 * Create an instance of {@link GetUnits }
	 *
	 */
	public GetUnits createGetUnits() {
		return new GetUnits();
	}

	/**
	 * Create an instance of {@link GetClustersResponse }
	 *
	 */
	public GetClustersResponse createGetClustersResponse() {
		return new GetClustersResponse();
	}

	/**
	 * Create an instance of {@link GetAllDevices }
	 *
	 */
	public GetAllDevices createGetAllDevices() {
		return new GetAllDevices();
	}

	/**
	 * Create an instance of {@link GetUnitsForCluster }
	 *
	 */
	public GetUnitsForCluster createGetUnitsForCluster() {
		return new GetUnitsForCluster();
	}

	/**
	 * Create an instance of {@link GetDevicesResponse }
	 *
	 */
	public GetDevicesResponse createGetDevicesResponse() {
		return new GetDevicesResponse();
	}

	/**
	 * Create an instance of {@link GetGraphPath }
	 *
	 */
	public GetGraphPath createGetGraphPath() {
		return new GetGraphPath();
	}

	/**
	 * Create an instance of {@link GetUnitsForClusterResponse }
	 *
	 */
	public GetUnitsForClusterResponse createGetUnitsForClusterResponse() {
		return new GetUnitsForClusterResponse();
	}

	/**
	 * Create an instance of {@link GetGraphPathResponse }
	 *
	 */
	public GetGraphPathResponse createGetGraphPathResponse() {
		return new GetGraphPathResponse();
	}

	/**
	 * Create an instance of {@link GetPreviewData }
	 *
	 */
	public GetPreviewData createGetPreviewData() {
		return new GetPreviewData();
	}

	/**
	 * Create an instance of {@link GetDevices }
	 *
	 */
	public GetDevices createGetDevices() {
		return new GetDevices();
	}

	/**
	 * Create an instance of {@link GetAllDevicesResponse }
	 *
	 */
	public GetAllDevicesResponse createGetAllDevicesResponse() {
		return new GetAllDevicesResponse();
	}

	/**
	 * Create an instance of {@link GetPreviewDataResponse }
	 *
	 */
	public GetPreviewDataResponse createGetPreviewDataResponse() {
		return new GetPreviewDataResponse();
	}

	/**
	 * Create an instance of {@link GetClusters }
	 *
	 */
	public GetClusters createGetClusters() {
		return new GetClusters();
	}

	/**
	 * Create an instance of {@link ClusterDefinition }
	 *
	 */
	public ClusterDefinition createClusterDefinition() {
		return new ClusterDefinition();
	}

	/**
	 * Create an instance of {@link PreviewObject }
	 *
	 */
	public PreviewObject createPreviewObject() {
		return new PreviewObject();
	}

	/**
	 * Create an instance of {@link MonitoredEntity }
	 *
	 */
	public MonitoredEntity createMonitoredEntity() {
		return new MonitoredEntity();
	}

	/**
	 * Create an instance of {@link UnitDefinition }
	 *
	 */
	public UnitDefinition createUnitDefinition() {
		return new UnitDefinition();
	}

	/**
	 * Create an instance of {@link DeviceDefinition }
	 *
	 */
	public DeviceDefinition createDeviceDefinition() {
		return new DeviceDefinition();
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetClustersResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getClustersResponse")
	public JAXBElement<GetClustersResponse> createGetClustersResponse(GetClustersResponse value) {
		return new JAXBElement<GetClustersResponse>(_GetClustersResponse_QNAME, GetClustersResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetUnitsResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getUnitsResponse")
	public JAXBElement<GetUnitsResponse> createGetUnitsResponse(GetUnitsResponse value) {
		return new JAXBElement<GetUnitsResponse>(_GetUnitsResponse_QNAME, GetUnitsResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetUnits }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getUnits")
	public JAXBElement<GetUnits> createGetUnits(GetUnits value) {
		return new JAXBElement<GetUnits>(_GetUnits_QNAME, GetUnits.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetGraphPath }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getGraphPath")
	public JAXBElement<GetGraphPath> createGetGraphPath(GetGraphPath value) {
		return new JAXBElement<GetGraphPath>(_GetGraphPath_QNAME, GetGraphPath.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetDevicesResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getDevicesResponse")
	public JAXBElement<GetDevicesResponse> createGetDevicesResponse(GetDevicesResponse value) {
		return new JAXBElement<GetDevicesResponse>(_GetDevicesResponse_QNAME, GetDevicesResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetAllDevices }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getAllDevices")
	public JAXBElement<GetAllDevices> createGetAllDevices(GetAllDevices value) {
		return new JAXBElement<GetAllDevices>(_GetAllDevices_QNAME, GetAllDevices.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetUnitsForCluster }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getUnitsForCluster")
	public JAXBElement<GetUnitsForCluster> createGetUnitsForCluster(GetUnitsForCluster value) {
		return new JAXBElement<GetUnitsForCluster>(_GetUnitsForCluster_QNAME, GetUnitsForCluster.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetUnitsForClusterResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getUnitsForClusterResponse")
	public JAXBElement<GetUnitsForClusterResponse> createGetUnitsForClusterResponse(GetUnitsForClusterResponse value) {
		return new JAXBElement<GetUnitsForClusterResponse>(_GetUnitsForClusterResponse_QNAME, GetUnitsForClusterResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetGraphPathResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getGraphPathResponse")
	public JAXBElement<GetGraphPathResponse> createGetGraphPathResponse(GetGraphPathResponse value) {
		return new JAXBElement<GetGraphPathResponse>(_GetGraphPathResponse_QNAME, GetGraphPathResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetClusters }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getClusters")
	public JAXBElement<GetClusters> createGetClusters(GetClusters value) {
		return new JAXBElement<GetClusters>(_GetClusters_QNAME, GetClusters.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetPreviewDataResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getPreviewDataResponse")
	public JAXBElement<GetPreviewDataResponse> createGetPreviewDataResponse(GetPreviewDataResponse value) {
		return new JAXBElement<GetPreviewDataResponse>(_GetPreviewDataResponse_QNAME, GetPreviewDataResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetAllDevicesResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getAllDevicesResponse")
	public JAXBElement<GetAllDevicesResponse> createGetAllDevicesResponse(GetAllDevicesResponse value) {
		return new JAXBElement<GetAllDevicesResponse>(_GetAllDevicesResponse_QNAME, GetAllDevicesResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetDevices }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getDevices")
	public JAXBElement<GetDevices> createGetDevices(GetDevices value) {
		return new JAXBElement<GetDevices>(_GetDevices_QNAME, GetDevices.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetPreviewData }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", name = "getPreviewData")
	public JAXBElement<GetPreviewData> createGetPreviewData(GetPreviewData value) {
		return new JAXBElement<GetPreviewData>(_GetPreviewData_QNAME, GetPreviewData.class, null, value);
	}
}
