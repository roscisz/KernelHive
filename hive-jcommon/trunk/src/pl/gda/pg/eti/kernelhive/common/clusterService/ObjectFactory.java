/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Szymon Bultrowicz
 *
 * This file is part of KernelHive.
 * KernelHive is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * KernelHive is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with KernelHive. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.gda.pg.eti.kernelhive.common.clusterService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * pl.gda.pg.eti.kernelhive.common.clusterService package.
 * <p>An ObjectFactory allows you to programatically construct new instances of
 * the Java representation for XML content. The Java representation of XML
 * content can consist of schema derived interfaces and classes representing the
 * binding of schema type definitions, element declarations and model groups.
 * Factory methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _ReportProgress_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "reportProgress");
	private final static QName _ReportPreviewResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "reportPreviewResponse");
	private final static QName _ReportOver_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "reportOver");
	private final static QName _ReportOverResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "reportOverResponse");
	private final static QName _GetJobResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "getJobResponse");
	private final static QName _UpdateResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "updateResponse");
	private final static QName _GetJob_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "getJob");
	private final static QName _Update_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "update");
	private final static QName _ReportPreview_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "reportPreview");
	private final static QName _ReportProgressResponse_QNAME = new QName("http://engine.kernelhive.eti.pg.gda.pl/", "reportProgressResponse");
	private final static QName _ReportPreviewArg1_QNAME = new QName("", "arg1");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * pl.gda.pg.eti.kernelhive.common.clusterService
	 *
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Cluster }
	 *
	 */
	public Cluster createCluster() {
		return new Cluster();
	}

	/**
	 * Create an instance of {@link Update }
	 *
	 */
	public Update createUpdate() {
		return new Update();
	}

	/**
	 * Create an instance of {@link GetJob }
	 *
	 */
	public GetJob createGetJob() {
		return new GetJob();
	}

	/**
	 * Create an instance of {@link ReportProgressResponse }
	 *
	 */
	public ReportProgressResponse createReportProgressResponse() {
		return new ReportProgressResponse();
	}

	/**
	 * Create an instance of {@link ReportPreview }
	 *
	 */
	public ReportPreview createReportPreview() {
		return new ReportPreview();
	}

	/**
	 * Create an instance of {@link ReportOver }
	 *
	 */
	public ReportOver createReportOver() {
		return new ReportOver();
	}

	/**
	 * Create an instance of {@link UpdateResponse }
	 *
	 */
	public UpdateResponse createUpdateResponse() {
		return new UpdateResponse();
	}

	/**
	 * Create an instance of {@link GetJobResponse }
	 *
	 */
	public GetJobResponse createGetJobResponse() {
		return new GetJobResponse();
	}

	/**
	 * Create an instance of {@link ReportOverResponse }
	 *
	 */
	public ReportOverResponse createReportOverResponse() {
		return new ReportOverResponse();
	}

	/**
	 * Create an instance of {@link ReportPreviewResponse }
	 *
	 */
	public ReportPreviewResponse createReportPreviewResponse() {
		return new ReportPreviewResponse();
	}

	/**
	 * Create an instance of {@link ReportProgress }
	 *
	 */
	public ReportProgress createReportProgress() {
		return new ReportProgress();
	}

	/**
	 * Create an instance of {@link JobInfo }
	 *
	 */
	public JobInfo createJobInfo() {
		return new JobInfo();
	}

	/**
	 * Create an instance of {@link Unit }
	 *
	 */
	public Unit createUnit() {
		return new Unit();
	}

	/**
	 * Create an instance of {@link Device }
	 *
	 */
	public Device createDevice() {
		return new Device();
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link ReportProgress }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "reportProgress")
	public JAXBElement<ReportProgress> createReportProgress(ReportProgress value) {
		return new JAXBElement<ReportProgress>(_ReportProgress_QNAME, ReportProgress.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link ReportPreviewResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "reportPreviewResponse")
	public JAXBElement<ReportPreviewResponse> createReportPreviewResponse(ReportPreviewResponse value) {
		return new JAXBElement<ReportPreviewResponse>(_ReportPreviewResponse_QNAME, ReportPreviewResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link ReportOver }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "reportOver")
	public JAXBElement<ReportOver> createReportOver(ReportOver value) {
		return new JAXBElement<ReportOver>(_ReportOver_QNAME, ReportOver.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link ReportOverResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "reportOverResponse")
	public JAXBElement<ReportOverResponse> createReportOverResponse(ReportOverResponse value) {
		return new JAXBElement<ReportOverResponse>(_ReportOverResponse_QNAME, ReportOverResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetJobResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "getJobResponse")
	public JAXBElement<GetJobResponse> createGetJobResponse(GetJobResponse value) {
		return new JAXBElement<GetJobResponse>(_GetJobResponse_QNAME, GetJobResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link UpdateResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "updateResponse")
	public JAXBElement<UpdateResponse> createUpdateResponse(UpdateResponse value) {
		return new JAXBElement<UpdateResponse>(_UpdateResponse_QNAME, UpdateResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link GetJob }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "getJob")
	public JAXBElement<GetJob> createGetJob(GetJob value) {
		return new JAXBElement<GetJob>(_GetJob_QNAME, GetJob.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link Update }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "update")
	public JAXBElement<Update> createUpdate(Update value) {
		return new JAXBElement<Update>(_Update_QNAME, Update.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link ReportPreview }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "reportPreview")
	public JAXBElement<ReportPreview> createReportPreview(ReportPreview value) {
		return new JAXBElement<ReportPreview>(_ReportPreview_QNAME, ReportPreview.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link ReportProgressResponse }{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "http://engine.kernelhive.eti.pg.gda.pl/", name = "reportProgressResponse")
	public JAXBElement<ReportProgressResponse> createReportProgressResponse(ReportProgressResponse value) {
		return new JAXBElement<ReportProgressResponse>(_ReportProgressResponse_QNAME, ReportProgressResponse.class, null, value);
	}

	/**
	 * Create an instance of
	 * {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
	 *
	 */
	@XmlElementDecl(namespace = "", name = "arg1", scope = ReportPreview.class)
	public JAXBElement<byte[]> createReportPreviewArg1(byte[] value) {
		return new JAXBElement<byte[]>(_ReportPreviewArg1_QNAME, byte[].class, ReportPreview.class, ((byte[]) value));
	}
}
