package pl.gda.pg.eti.kernelhive.common.monitoring.service;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.2.4-b01 Generated
 * source version: 2.2
 *
 */
@WebServiceClient(name = "MonitoringClientBeanService", targetNamespace = "http://monitoring.engine.kernelhive.eti.pg.gda.pl/", wsdlLocation = "http://localhost:8080/MonitoringClientBeanService/MonitoringClientBean?wsdl")
public class MonitoringClientBeanService
		extends Service {

	private final static URL MONITORINGCLIENTBEANSERVICE_WSDL_LOCATION;
	private final static WebServiceException MONITORINGCLIENTBEANSERVICE_EXCEPTION;
	private final static QName MONITORINGCLIENTBEANSERVICE_QNAME = new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "MonitoringClientBeanService");

	static {
		URL url = null;
		WebServiceException e = null;
		try {
			url = new URL("http://localhost:8080/MonitoringClientBeanService/MonitoringClientBean?wsdl");
		} catch (MalformedURLException ex) {
			e = new WebServiceException(ex);
		}
		MONITORINGCLIENTBEANSERVICE_WSDL_LOCATION = url;
		MONITORINGCLIENTBEANSERVICE_EXCEPTION = e;
	}

	public MonitoringClientBeanService() {
		super(__getWsdlLocation(), MONITORINGCLIENTBEANSERVICE_QNAME);
	}

	public MonitoringClientBeanService(WebServiceFeature... features) {
		super(__getWsdlLocation(), MONITORINGCLIENTBEANSERVICE_QNAME, features);
	}

	public MonitoringClientBeanService(URL wsdlLocation) {
		super(wsdlLocation, MONITORINGCLIENTBEANSERVICE_QNAME);
	}

	public MonitoringClientBeanService(URL wsdlLocation, WebServiceFeature... features) {
		super(wsdlLocation, MONITORINGCLIENTBEANSERVICE_QNAME, features);
	}

	public MonitoringClientBeanService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public MonitoringClientBeanService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
		super(wsdlLocation, serviceName, features);
	}

	/**
	 *
	 * @return returns MonitoringClientBean
	 */
	@WebEndpoint(name = "MonitoringClientBeanPort")
	public MonitoringClientBean getMonitoringClientBeanPort() {
		return super.getPort(new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "MonitoringClientBeanPort"), MonitoringClientBean.class);
	}

	/**
	 *
	 * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
	 * configure on the proxy. Supported features not in *
	 * the <code>features</code> parameter will have their default values.
	 * @return returns MonitoringClientBean
	 */
	@WebEndpoint(name = "MonitoringClientBeanPort")
	public MonitoringClientBean getMonitoringClientBeanPort(WebServiceFeature... features) {
		return super.getPort(new QName("http://monitoring.engine.kernelhive.eti.pg.gda.pl/", "MonitoringClientBeanPort"), MonitoringClientBean.class, features);
	}

	private static URL __getWsdlLocation() {
		if (MONITORINGCLIENTBEANSERVICE_EXCEPTION != null) {
			throw MONITORINGCLIENTBEANSERVICE_EXCEPTION;
		}
		return MONITORINGCLIENTBEANSERVICE_WSDL_LOCATION;
	}
}
