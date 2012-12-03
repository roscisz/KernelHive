package pl.gda.pg.eti.kernelhive.repository.loader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.UUID;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class JarFileLoaderService {

	private File file;
	private final URL url;

	public JarFileLoaderService(final URL url) {
		this.url = url;
	}

	public void downloadJar() throws IOException {
		final ReadableByteChannel channel = Channels.newChannel(url
				.openStream());
		file = File.createTempFile("kh." + UUID.randomUUID(), ".jar");
		file.deleteOnExit();
		final FileOutputStream fos = new FileOutputStream(file);
		while (fos.getChannel().transferFrom(channel, 0, 1 << 24) > 0)
			;
		fos.flush();
		fos.close();
	}

	public File getJar() {
		return file;
	}

	private String getJarVersion(final String jarUrl) {
		String version = null;
		try {
			final URL url = new URL("jar:" + jarUrl + "!/");
			final JarURLConnection juc = (JarURLConnection) url
					.openConnection();
			final JarFile jf = juc.getJarFile();
			final Attributes attrs = jf.getManifest().getMainAttributes();
			version = attrs.getValue("Manifest-Version");
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return version;
	}

	String getLocalJarVersion() {
		return getJarVersion(file.getAbsolutePath());
	}

	String getRemoteJarVersion(final String url) {
		return getJarVersion(url);
	}

	/**
	 * Compares local and remote jar versions
	 * 
	 * @param remoteUrl
	 *            url to remote jar
	 * @return <0 if remote is higher version, ==0 if local and remote versions
	 *         are the same, <0 if local is higher version
	 */
	public int compareJarVersions(final String remoteUrl) {
		final String local = getLocalJarVersion();
		final String remote = getRemoteJarVersion(remoteUrl);
		return local.compareTo(remote);
	}

}
