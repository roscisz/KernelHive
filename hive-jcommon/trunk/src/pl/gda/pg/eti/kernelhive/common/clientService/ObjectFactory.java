
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

    private final static QName _BrowseWorkflows_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseWorkflows");
    private final static QName _DeleteWorkflowResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "deleteWorkflowResponse");
    private final static QName _GetResults_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "getResults");
    private final static QName _BrowseWorkflowsResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseWorkflowsResponse");
    private final static QName _GetResultsResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "getResultsResponse");
    private final static QName _DeleteWorkflow_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "deleteWorkflow");
    private final static QName _BrowseInfrastructureResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseInfrastructureResponse");
    private final static QName _RunWorkflowResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "runWorkflowResponse");
    private final static QName _BrowseInfrastructure_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseInfrastructure");
    private final static QName _RunWorkflow_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "runWorkflow");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pl.gda.pg.eti.kernelhive.common.clientService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RunWorkflow }
     * 
     */
    public RunWorkflow createRunWorkflow() {
        return new RunWorkflow();
    }

    /**
     * Create an instance of {@link RunWorkflowResponse }
     * 
     */
    public RunWorkflowResponse createRunWorkflowResponse() {
        return new RunWorkflowResponse();
    }

    /**
     * Create an instance of {@link BrowseWorkflows }
     * 
     */
    public BrowseWorkflows createBrowseWorkflows() {
        return new BrowseWorkflows();
    }

    /**
     * Create an instance of {@link ClusterInfo }
     * 
     */
    public ClusterInfo createClusterInfo() {
        return new ClusterInfo();
    }

    /**
     * Create an instance of {@link WorkflowInfo }
     * 
     */
    public WorkflowInfo createWorkflowInfo() {
        return new WorkflowInfo();
    }

    /**
     * Create an instance of {@link DeleteWorkflow }
     * 
     */
    public DeleteWorkflow createDeleteWorkflow() {
        return new DeleteWorkflow();
    }

    /**
     * Create an instance of {@link GetResultsResponse }
     * 
     */
    public GetResultsResponse createGetResultsResponse() {
        return new GetResultsResponse();
    }

    /**
     * Create an instance of {@link BrowseWorkflowsResponse }
     * 
     */
    public BrowseWorkflowsResponse createBrowseWorkflowsResponse() {
        return new BrowseWorkflowsResponse();
    }

    /**
     * Create an instance of {@link BrowseInfrastructureResponse }
     * 
     */
    public BrowseInfrastructureResponse createBrowseInfrastructureResponse() {
        return new BrowseInfrastructureResponse();
    }

    /**
     * Create an instance of {@link DeleteWorkflowResponse }
     * 
     */
    public DeleteWorkflowResponse createDeleteWorkflowResponse() {
        return new DeleteWorkflowResponse();
    }

    /**
     * Create an instance of {@link GetResults }
     * 
     */
    public GetResults createGetResults() {
        return new GetResults();
    }

    /**
     * Create an instance of {@link BrowseInfrastructure }
     * 
     */
    public BrowseInfrastructure createBrowseInfrastructure() {
        return new BrowseInfrastructure();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BrowseWorkflows }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "browseWorkflows")
    public JAXBElement<BrowseWorkflows> createBrowseWorkflows(BrowseWorkflows value) {
        return new JAXBElement<BrowseWorkflows>(_BrowseWorkflows_QNAME, BrowseWorkflows.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteWorkflowResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "deleteWorkflowResponse")
    public JAXBElement<DeleteWorkflowResponse> createDeleteWorkflowResponse(DeleteWorkflowResponse value) {
        return new JAXBElement<DeleteWorkflowResponse>(_DeleteWorkflowResponse_QNAME, DeleteWorkflowResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResults }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "getResults")
    public JAXBElement<GetResults> createGetResults(GetResults value) {
        return new JAXBElement<GetResults>(_GetResults_QNAME, GetResults.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BrowseWorkflowsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "browseWorkflowsResponse")
    public JAXBElement<BrowseWorkflowsResponse> createBrowseWorkflowsResponse(BrowseWorkflowsResponse value) {
        return new JAXBElement<BrowseWorkflowsResponse>(_BrowseWorkflowsResponse_QNAME, BrowseWorkflowsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResultsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "getResultsResponse")
    public JAXBElement<GetResultsResponse> createGetResultsResponse(GetResultsResponse value) {
        return new JAXBElement<GetResultsResponse>(_GetResultsResponse_QNAME, GetResultsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteWorkflow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "deleteWorkflow")
    public JAXBElement<DeleteWorkflow> createDeleteWorkflow(DeleteWorkflow value) {
        return new JAXBElement<DeleteWorkflow>(_DeleteWorkflow_QNAME, DeleteWorkflow.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BrowseInfrastructureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "browseInfrastructureResponse")
    public JAXBElement<BrowseInfrastructureResponse> createBrowseInfrastructureResponse(BrowseInfrastructureResponse value) {
        return new JAXBElement<BrowseInfrastructureResponse>(_BrowseInfrastructureResponse_QNAME, BrowseInfrastructureResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunWorkflowResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "runWorkflowResponse")
    public JAXBElement<RunWorkflowResponse> createRunWorkflowResponse(RunWorkflowResponse value) {
        return new JAXBElement<RunWorkflowResponse>(_RunWorkflowResponse_QNAME, RunWorkflowResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BrowseInfrastructure }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "browseInfrastructure")
    public JAXBElement<BrowseInfrastructure> createBrowseInfrastructure(BrowseInfrastructure value) {
        return new JAXBElement<BrowseInfrastructure>(_BrowseInfrastructure_QNAME, BrowseInfrastructure.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunWorkflow }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "runWorkflow")
    public JAXBElement<RunWorkflow> createRunWorkflow(RunWorkflow value) {
        return new JAXBElement<RunWorkflow>(_RunWorkflow_QNAME, RunWorkflow.class, null, value);
    }

}
