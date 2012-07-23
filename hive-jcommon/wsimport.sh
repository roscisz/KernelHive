TMPDIR=wsimport
CLUSTER_WSDL=http://localhost:8080/ClusterBeanService/ClusterBean?wsdl
CLUSTER_PACKAGE=pl.gda.pg.eti.kernelhive.common.clusterService
CLUSTER_PKGDIR=pl/gda/pg/eti/kernelhive/common/clusterService
CLIENT_WSDL=http://localhost:8080/ClientBeanService/ClientBean?wsdl
CLIENT_PACKAGE=pl.gda.pg.eti.kernelhive.common.clientService
CLIENT_PKGDIR=pl/gda/pg/eti/kernelhive/common/clientService
DESTDIR=trunk/src/

mkdir $TMPDIR
wsimport -verbose -s $TMPDIR -p $CLUSTER_PACKAGE -keep -Xnocompile $CLUSTER_WSDL 
cp $TMPDIR/$CLUSTER_PKGDIR/ClusterBean.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/ClusterBeanService.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/ObjectFactory.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/Update.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/UpdateResponse.java $DESTDIR/$CLUSTER_PKGDIR
cp $TMPDIR/$CLUSTER_PKGDIR/UpdateResponse.java $DESTDIR/$CLUSTER_PKGDIR
wsimport -verbose -s $TMPDIR -p $CLIENT_PACKAGE -keep -Xnocompile $CLIENT_WSDL
cp $TMPDIR/$CLIENT_PKGDIR/ClientBean.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/ClientBeanService.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/ObjectFactory.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/RunGraph.java $DESTDIR/$CLIENT_PKGDIR
cp $TMPDIR/$CLIENT_PKGDIR/RunGraphResponse.java $DESTDIR/$CLIENT_PKGDIR
rm -r $TMPDIR
