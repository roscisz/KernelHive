TMPDIR=wsimport
CLUSTER_WSDL=http://localhost:8080/ClusterBeanService/ClusterBean?wsdl
#CLUSTER_WSDL=cluster.wsdl
CLUSTER_PACKAGE=pl.gda.pg.eti.kernelhive.common.clusterService
CLUSTER_PKGDIR=pl/gda/pg/eti/kernelhive/common/clusterService
CLIENT_WSDL=http://localhost:8080/ClientBeanService/ClientBean?wsdl
#CLIENT_WSDL=client.wsdl
CLIENT_PACKAGE=pl.gda.pg.eti.kernelhive.common.clientService
CLIENT_PKGDIR=pl/gda/pg/eti/kernelhive/common/clientService
MONITORING_CLIENT_WSDL=http://localhost:8080/MonitoringClientBeanService/MonitoringClientBean?wsdl
#MONITORING_CLIENT_WSDL=monitoring-client.wsdl
MONITORING_CLIENT_PACKAGE=pl.gda.pg.eti.kernelhive.common.monitoring.service
MONITORING_CLIENT_PKGDIR=pl/gda/pg/eti/kernelhive/common/monitoring/service
DESTDIR=src/

mkdir $TMPDIR
wsimport -verbose -s $TMPDIR -p $CLUSTER_PACKAGE -keep -Xnocompile $CLUSTER_WSDL 
cp $TMPDIR/$CLUSTER_PKGDIR/ClusterBean.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/ClusterBeanService.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/GetJob.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/GetJobResponse.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/ObjectFactory.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/ReportProgress.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/ReportProgressResponse.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/ReportOver.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/ReportOverResponse.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/Update.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/ReportPreview.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/ReportPreviewResponse.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/UpdateResponse.java $DESTDIR/$CLUSTER_PKGDIR
wsimport -verbose -s $TMPDIR -p $CLIENT_PACKAGE -keep -Xnocompile $CLIENT_WSDL
cp $TMPDIR/$CLIENT_PKGDIR/BrowseInfrastructure.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/BrowseInfrastructureResponse.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/BrowseWorkflows.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/BrowseWorkflowsResponse.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/ClientBean.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/ClientBeanService.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/TerminateWorkflow.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/TerminateWorkflowResponse.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/GetWorkflowResults.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/GetWorkflowResultsResponse.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/GetJobProgress.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/GetJobProgressResponse.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/ObjectFactory.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/SubmitWorkflow.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/SubmitWorkflowResponse.java $DESTDIR/$CLIENT_PKGDIR
wsimport -verbose -s $TMPDIR -p $MONITORING_CLIENT_PACKAGE -keep -Xnocompile $MONITORING_CLIENT_WSDL
rm $TMPDIR/$MONITORING_CLIENT_PKGDIR/package-info.java
cp $TMPDIR/$MONITORING_CLIENT_PKGDIR/*.java $DESTDIR/$MONITORING_CLIENT_PKGDIR
rm -r $TMPDIR
