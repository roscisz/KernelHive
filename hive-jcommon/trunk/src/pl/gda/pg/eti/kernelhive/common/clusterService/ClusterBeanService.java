package pl.gda.pg.eti.kernelhive.common.clusterService;

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
@WebServiceClient(name = "ClusterBeanService", targetNamespace = "http://engine.kernelhive.eti.pg.gda.pl/", wsdlLocation = "http://localhost:8080/ClusterBeanService/ClusterBean?wsdl")
public class ClusterBeanService
		extends Service {

	private final static URL CLUSTERBEANSERVICE_WSDL_LOCATION;
	private final static WebServiceException CLUSTERBEANSERVICE_EXCEPTION;
	private final static QName CLUSTERBEANSERVICE_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "ClusterBeanService");

	static {
		URL url = null;
		WebServiceException e = null;
		try {
			url = new URL("http://localhost:8080/ClusterBeanService/ClusterBean?wsdl");
		} catch (MalformedURLException ex) {
			e = new WebServiceException(ex);
		}
		CLUSTERBEANSERVICE_WSDL_LOCATION = url;
		CLUSTERBEANSERVICE_EXCEPTION = e;
	}

	public ClusterBeanService() {
		super(__getWsdlLocation(), CLUSTERBEANSERVICE_QNAME);
	}

	public ClusterBeanService(WebServiceFeature... features) {
		super(__getWsdlLocation(), CLUSTERBEANSERVICE_QNAME, features);
	}

	public ClusterBeanService(URL wsdlLocation) {
		super(wsdlLocation, CLUSTERBEANSERVICE_QNAME);
	}

	public ClusterBeanService(URL wsdlLocation, WebServiceFeature... features) {
		super(wsdlLocation, CLUSTERBEANSERVICE_QNAME, features);
	}

	public ClusterBeanService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public ClusterBeanService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
		super(wsdlLocation, serviceName, features);
	}

	/**
	 *
	 * @return returns ClusterBean
	 */
	@WebEndpoint(name = "ClusterBeanPort")
	public ClusterBean getClusterBeanPort() {
		return super.getPort(new QName("http://engine.kernelhive.eti.pg.gda.pl/", "ClusterBeanPort"), ClusterBean.class);
	}

	/**
	 *
	 * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
	 * configure on the proxy. Supported features not in *
	 * the <code>features</code> parameter will have their default values.
	 * @return returns ClusterBean
	 */
	@WebEndpoint(name = "ClusterBeanPort")
	public ClusterBean getClusterBeanPort(WebServiceFeature... features) {
		return super.getPort(new QName("http://engine.kernelhive.eti.pg.gda.pl/", "ClusterBeanPort"), ClusterBean.class, features);
	}

	private static URL __getWsdlLocation() {
		if (CLUSTERBEANSERVICE_EXCEPTION != null) {
			throw CLUSTERBEANSERVICE_EXCEPTION;
		}
		return CLUSTERBEANSERVICE_WSDL_LOCATION;
	}
}
