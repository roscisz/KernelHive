
package pl.gda.pg.eti.kernelhive.common.clusterService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "ClusterBeanService", targetNamespace = "http://engine.kernelhive.eti.pg.gda.pl/", wsdlLocation = "file:/home/roy/mgr/KernelHive/KernelHive/src/hive-jcommon/trunk/cluster.wsdl")
public class ClusterBeanService
    extends Service
{

    private final static URL CLUSTERBEANSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBeanService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = pl.gda.pg.eti.kernelhive.common.clusterService.ClusterBeanService.class.getResource(".");
            url = new URL(baseUrl, "file:/home/roy/mgr/KernelHive/KernelHive/src/hive-jcommon/trunk/cluster.wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'file:/home/roy/mgr/KernelHive/KernelHive/src/hive-jcommon/trunk/cluster.wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        CLUSTERBEANSERVICE_WSDL_LOCATION = url;
    }

    public ClusterBeanService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ClusterBeanService() {
        super(CLUSTERBEANSERVICE_WSDL_LOCATION, new QName("http://engine.kernelhive.eti.pg.gda.pl/", "ClusterBeanService"));
    }

    /**
     * 
     * @return
     *     returns ClusterBean
     */
    @WebEndpoint(name = "ClusterBeanPort")
    public ClusterBean getClusterBeanPort() {
        return super.getPort(new QName("http://engine.kernelhive.eti.pg.gda.pl/", "ClusterBeanPort"), ClusterBean.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ClusterBean
     */
    @WebEndpoint(name = "ClusterBeanPort")
    public ClusterBean getClusterBeanPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://engine.kernelhive.eti.pg.gda.pl/", "ClusterBeanPort"), ClusterBean.class, features);
    }

}