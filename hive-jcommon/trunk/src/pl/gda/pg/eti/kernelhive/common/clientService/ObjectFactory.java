
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

    private final static QName _BrowseTasksResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseTasksResponse");
    private final static QName _DeleteTaskResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "deleteTaskResponse");
    private final static QName _GetResults_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "getResults");
    private final static QName _RunTaskResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "runTaskResponse");
    private final static QName _RunTask_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "runTask");
    private final static QName _GetResultsResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "getResultsResponse");
    private final static QName _BrowseTasks_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseTasks");
    private final static QName _BrowseInfrastructureResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseInfrastructureResponse");
    private final static QName _DeleteTask_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "deleteTask");
    private final static QName _BrowseInfrastructure_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "browseInfrastructure");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pl.gda.pg.eti.kernelhive.common.clientService
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetResults }
     * 
     */
    public GetResults createGetResults() {
        return new GetResults();
    }

    /**
     * Create an instance of {@link ClusterInfo }
     * 
     */
    public ClusterInfo createClusterInfo() {
        return new ClusterInfo();
    }

    /**
     * Create an instance of {@link RunTaskResponse }
     * 
     */
    public RunTaskResponse createRunTaskResponse() {
        return new RunTaskResponse();
    }

    /**
     * Create an instance of {@link TaskInfo }
     * 
     */
    public TaskInfo createTaskInfo() {
        return new TaskInfo();
    }

    /**
     * Create an instance of {@link BrowseInfrastructureResponse }
     * 
     */
    public BrowseInfrastructureResponse createBrowseInfrastructureResponse() {
        return new BrowseInfrastructureResponse();
    }

    /**
     * Create an instance of {@link BrowseTasks }
     * 
     */
    public BrowseTasks createBrowseTasks() {
        return new BrowseTasks();
    }

    /**
     * Create an instance of {@link BrowseTasksResponse }
     * 
     */
    public BrowseTasksResponse createBrowseTasksResponse() {
        return new BrowseTasksResponse();
    }

    /**
     * Create an instance of {@link GetResultsResponse }
     * 
     */
    public GetResultsResponse createGetResultsResponse() {
        return new GetResultsResponse();
    }

    /**
     * Create an instance of {@link RunTask }
     * 
     */
    public RunTask createRunTask() {
        return new RunTask();
    }

    /**
     * Create an instance of {@link DeleteTaskResponse }
     * 
     */
    public DeleteTaskResponse createDeleteTaskResponse() {
        return new DeleteTaskResponse();
    }

    /**
     * Create an instance of {@link BrowseInfrastructure }
     * 
     */
    public BrowseInfrastructure createBrowseInfrastructure() {
        return new BrowseInfrastructure();
    }

    /**
     * Create an instance of {@link DeleteTask }
     * 
     */
    public DeleteTask createDeleteTask() {
        return new DeleteTask();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BrowseTasksResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "browseTasksResponse")
    public JAXBElement<BrowseTasksResponse> createBrowseTasksResponse(BrowseTasksResponse value) {
        return new JAXBElement<BrowseTasksResponse>(_BrowseTasksResponse_QNAME, BrowseTasksResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "deleteTaskResponse")
    public JAXBElement<DeleteTaskResponse> createDeleteTaskResponse(DeleteTaskResponse value) {
        return new JAXBElement<DeleteTaskResponse>(_DeleteTaskResponse_QNAME, DeleteTaskResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link RunTaskResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "runTaskResponse")
    public JAXBElement<RunTaskResponse> createRunTaskResponse(RunTaskResponse value) {
        return new JAXBElement<RunTaskResponse>(_RunTaskResponse_QNAME, RunTaskResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "runTask")
    public JAXBElement<RunTask> createRunTask(RunTask value) {
        return new JAXBElement<RunTask>(_RunTask_QNAME, RunTask.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link BrowseTasks }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "browseTasks")
    public JAXBElement<BrowseTasks> createBrowseTasks(BrowseTasks value) {
        return new JAXBElement<BrowseTasks>(_BrowseTasks_QNAME, BrowseTasks.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTask }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "deleteTask")
    public JAXBElement<DeleteTask> createDeleteTask(DeleteTask value) {
        return new JAXBElement<DeleteTask>(_DeleteTask_QNAME, DeleteTask.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BrowseInfrastructure }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "browseInfrastructure")
    public JAXBElement<BrowseInfrastructure> createBrowseInfrastructure(BrowseInfrastructure value) {
        return new JAXBElement<BrowseInfrastructure>(_BrowseInfrastructure_QNAME, BrowseInfrastructure.class, null, value);
    }

}
