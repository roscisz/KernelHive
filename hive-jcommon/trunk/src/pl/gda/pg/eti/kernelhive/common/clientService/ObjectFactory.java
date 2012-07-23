
package pl.gda.pg.eti.kernelhive.common.clientService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pl.gda.pg.eti.kernelhive.common.clientService package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RunGraph_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "runGraph");
    private final static QName _RunGraphResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "runGraphResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pl.gda.pg.eti.kernelhive.common.clientService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RunGraph }
     * 
     */
    public RunGraph createRunGraph() {
        return new RunGraph();
    }

    /**
     * Create an instance of {@link RunGraphResponse }
     * 
     */
    public RunGraphResponse createRunGraphResponse() {
        return new RunGraphResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunGraph }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "runGraph")
    public JAXBElement<RunGraph> createRunGraph(RunGraph value) {
        return new JAXBElement<RunGraph>(_RunGraph_QNAME, RunGraph.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunGraphResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "runGraphResponse")
    public JAXBElement<RunGraphResponse> createRunGraphResponse(RunGraphResponse value) {
        return new JAXBElement<RunGraphResponse>(_RunGraphResponse_QNAME, RunGraphResponse.class, null, value);
    }

}
