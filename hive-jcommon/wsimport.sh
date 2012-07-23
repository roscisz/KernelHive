TMPDIR=wsimport
WSDL=http://localhost:8080/ClusterBeanService/ClusterBean?wsdl
PACKAGE=pl.gda.pg.eti.kernelhive.common.structure
PKGDIR=pl/gda/pg/eti/kernelhive/common/structure
DESTDIR=trunk/src/

mkdir $TMPDIR
wsimport -verbose -s $TMPDIR -p $PACKAGE -keep -Xnocompile $WSDL 
cp $TMPDIR/$PKGDIR/ClusterBean.java $DESTDIR/$PKGDIR
cp $TMPDIR/$PKGDIR/ClusterBeanService.java $DESTDIR/$PKGDIR
cp $TMPDIR/$PKGDIR/ObjectFactory.java $DESTDIR/$PKGDIR
cp $TMPDIR/$PKGDIR/Update.java $DESTDIR/$PKGDIR
cp $TMPDIR/$PKGDIR/UpdateResponse.java $DESTDIR/$PKGDIR
rm -r $TMPDIR
