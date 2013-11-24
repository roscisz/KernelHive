package pl.gda.pg.eti.kernelhive.common.clientService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * pl.gda.pg.eti.kernelhive.common.clientService package.
 * <p>An ObjectFactory allows you to programatically construct new instances of
 * the Java representation for XML content. The Java representation of XML
 * content can consist of schema derived interfaces and classes representing the
 * binding of schema type definitions, element declarations and model groups.
 * Factory methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _GetWorkflowResultsResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "getWorkflowResultsResponse");
	private final static QName _SubmitWorkflow_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "submitWorkflow");
	private final static QName _GetWorkflowProgress_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "getWorkflowProgress");
	private final static QName _GetWorkflowResults_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "getWorkflowResults");
	private final static QName _TerminateWorkflowResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "terminateWorkflowResponse");
	private final static QName _BrowseWorkflows_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseWorkflows");
	private final static QName _TerminateWorkflow_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "terminateWorkflow");
	private final static QName _BrowseWorkflowsResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseWorkflowsResponse");
	private final static QName _BrowseInfrastructureResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseInfrastructureResponse");
	private final static QName _BrowseInfrastructure_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseInfrastructure");
	private final static QName _SubmitWorkflowResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "submitWorkflowResponse");
	private final static QName _GetWorkflowProgressResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "getWorkflowProgressResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * pl.gda.pg.eti.kernelhive.common.clientService
	 *
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link SubmitWorkflowResponse }
	 *
	 */
	public SubmitWorkflowResponse createSubmitWorkflowResponse() {
		return new SubmitWorkflowResponse();
	}

	/**
	 * Create an instance of {@link BrowseInfrastructure }
	 *
	 */
	public BrowseInfrastructure createBrowseInfrastructure() {
		return new BrowseInfrastructure();
	}

	/**
	 * Create an instance of {@link GetWorkflowProgressResponse }
	 *
	 */
	public GetWorkflowProgressResponse createGetWorkflowProgressResponse() {
		return new GetWorkflowProgressResponse();
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
	 * Create an instance of {@link TerminateWorkflow }
	 *
	 */
	public TerminateWorkflow createTerminateWorkflow() {
		return new TerminateWorkflow();
	}

	/**
	 * Create an instance of {@link GetWorkflowProgress }
	 *
	 */
	public GetWorkflowProgress createGetWorkflowProgress() {
		return new GetWorkflowProgress();
	}

	/**
	 * Create an instance of {@link SubmitWorkflow }
	 *
	 */
	public SubmitWorkflow createSubmitWorkflow() {
		return new SubmitWorkflow();
	}

	/**
	 * Create an instance of {@link GetWorkflowResultsResponse }
	 *
	 */
	public GetWorkflowResultsResponse createGetWorkflowResultsResponse() {
		return new GetWorkflowResultsResponse();
	}

	/**
	 * Create an instance of {@link BrowseWorkflows }
	 *
	 */
	public BrowseWorkflows createBrowseWorkflows() {
		return new BrowseWorkflows();
	}

	/**
	 * Create an instance of {@link TerminateWorkflowResponse }
	 *
	 */
	public TerminateWorkflowResponse createTerminateWorkflowResponse() {
		return new TerminateWorkflowResponse();
	}

	/**
	 * Create an instance of {@link GetWorkflowResults }
	 *
	 */
	public GetWorkflowResults createGetWorkflowResults() {
		return new GetWorkflowResults();
	}

	/**
	 * Create an instance of {@link UnitInfo }
	 *
	 */
	public UnitInfo createUnitInfo() {
		return new UnitInfo();
	}

	/**
	 * Create an instance of {@link DeviceInfo }
	 *
	 */
	public DeviceInfo createDeviceInfo() {
		return new DeviceInfo();
	}

	/**
	 * Create an instance of {@link JobProgress }
	 *
	 */
	public JobProgress createJobProgress() {
		return new JobProgress();
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
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetWorkflowResultsResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "getWorkflowResultsResponse")
	public JAXBElement<GetWorkflowResultsResponse> createGetWorkflowResultsResponse(GetWorkflowResultsResponse value) {
		return new JAXBElement<GetWorkflowResultsResponse>(_GetWorkflowResultsResponse_QNAME, GetWorkflowResultsResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link SubmitWorkflow }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "submitWorkflow")
	public JAXBElement<SubmitWorkflow> createSubmitWorkflow(SubmitWorkflow value) {
		return new JAXBElement<SubmitWorkflow>(_SubmitWorkflow_QNAME, SubmitWorkflow.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetWorkflowProgress }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "getWorkflowProgress")
	public JAXBElement<GetWorkflowProgress> createGetWorkflowProgress(GetWorkflowProgress value) {
		return new JAXBElement<GetWorkflowProgress>(_GetWorkflowProgress_QNAME, GetWorkflowProgress.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetWorkflowResults }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "getWorkflowResults")
	public JAXBElement<GetWorkflowResults> createGetWorkflowResults(GetWorkflowResults value) {
		return new JAXBElement<GetWorkflowResults>(_GetWorkflowResults_QNAME, GetWorkflowResults.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link TerminateWorkflowResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "terminateWorkflowResponse")
	public JAXBElement<TerminateWorkflowResponse> createTerminateWorkflowResponse(TerminateWorkflowResponse value) {
		return new JAXBElement<TerminateWorkflowResponse>(_TerminateWorkflowResponse_QNAME, TerminateWorkflowResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link BrowseWorkflows }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "browseWorkflows")
	public JAXBElement<BrowseWorkflows> createBrowseWorkflows(BrowseWorkflows value) {
		return new JAXBElement<BrowseWorkflows>(_BrowseWorkflows_QNAME, BrowseWorkflows.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link TerminateWorkflow }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "terminateWorkflow")
	public JAXBElement<TerminateWorkflow> createTerminateWorkflow(TerminateWorkflow value) {
		return new JAXBElement<TerminateWorkflow>(_TerminateWorkflow_QNAME, TerminateWorkflow.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link BrowseWorkflowsResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "browseWorkflowsResponse")
	public JAXBElement<BrowseWorkflowsResponse> createBrowseWorkflowsResponse(BrowseWorkflowsResponse value) {
		return new JAXBElement<BrowseWorkflowsResponse>(_BrowseWorkflowsResponse_QNAME, BrowseWorkflowsResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link BrowseInfrastructureResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "browseInfrastructureResponse")
	public JAXBElement<BrowseInfrastructureResponse> createBrowseInfrastructureResponse(BrowseInfrastructureResponse value) {
		return new JAXBElement<BrowseInfrastructureResponse>(_BrowseInfrastructureResponse_QNAME, BrowseInfrastructureResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link BrowseInfrastructure }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "browseInfrastructure")
	public JAXBElement<BrowseInfrastructure> createBrowseInfrastructure(BrowseInfrastructure value) {
		return new JAXBElement<BrowseInfrastructure>(_BrowseInfrastructure_QNAME, BrowseInfrastructure.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link SubmitWorkflowResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "submitWorkflowResponse")
	public JAXBElement<SubmitWorkflowResponse> createSubmitWorkflowResponse(SubmitWorkflowResponse value) {
		return new JAXBElement<SubmitWorkflowResponse>(_SubmitWorkflowResponse_QNAME, SubmitWorkflowResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetWorkflowProgressResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "getWorkflowProgressResponse")
	public JAXBElement<GetWorkflowProgressResponse> createGetWorkflowProgressResponse(GetWorkflowProgressResponse value) {
		return new JAXBElement<GetWorkflowProgressResponse>(_GetWorkflowProgressResponse_QNAME, GetWorkflowProgressResponse.class, null, value);
	}
}
