package pl.gda.pg.eti.kernelhive.engine.http.file.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

public class HttpFileUploadClient implements IHttpFileUploadClient {

	private HttpClient client;

	@Override
	public void postFileUpload(String uploadServletUrl, byte[] bytes)
			throws IOException {
		File file = null;

		try {
			client = new DefaultHttpClient();
			client.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			// if uploadServletURL is malformed - exception is thrown
			new URL(uploadServletUrl);

			file = File.createTempFile(UUID.randomUUID().toString(), "kh");
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file));
			bos.write(bytes);
			bos.flush();
			bos.close();

			HttpPost post = new HttpPost(uploadServletUrl);
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			entity.addPart("file", new FileBody(file,
					"application/octet-stream"));
			post.setEntity(entity);
			client.execute(post);
		} catch (IOException e) {
			throw e;
		} finally {
			client.getConnectionManager().shutdown();
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

}
