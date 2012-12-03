package pl.gda.pg.eti.kernelhive.repository.loader;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.configuration.ConfigurationException;

import pl.gda.pg.eti.kernelhive.repository.configuration.RemoteRepositoryConfiguration;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.repository.kernel.repository.IKernelRepository;

public class RepositoryLoaderService {

	private static final String KERNEL_REPOSITORY_CLASS_NAME = "pl.gda.pg.eti.kernelhive.repository.kernel.repository.KernelRepository";
	private static final String GRAPH_NODE_BUILDER_CLASS_NAME = "pl.gda.pg.eti.kernelhive.repository.graph.node.GraphNodeBuilder";
	private static final String PACKAGE_NAME_TO_LOAD = "pl.gda.pg.eti.kernelhive";

	private static RepositoryLoaderService _service = null;

	private URL jarUrl = null;
	private JarFileLoaderService fileLoader;
	private boolean loaded = false;
	@SuppressWarnings("rawtypes")
	private List<Class> loadedClasses;

	public static RepositoryLoaderService getInstance()
			throws RepositoryLoaderServiceException {
		if (_service == null) {
			_service = new RepositoryLoaderService();
		}
		return _service;
	}

	private RepositoryLoaderService() throws RepositoryLoaderServiceException {
		try {
			jarUrl = RemoteRepositoryConfiguration.getInstance()
					.getKernelRepositoryJarURL();
			fileLoader = new JarFileLoaderService(jarUrl);
		} catch (final ConfigurationException e) {
			throw new RepositoryLoaderServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("rawtypes")
	private void loadJAR(final URL jarUrl)
			throws RepositoryLoaderServiceException {
		CustomURLClassLoader cl = null;
		try {
			final URL url = new URL("jar:" + jarUrl + "!/");
			url.openConnection();
			cl = new CustomURLClassLoader(new URL[] { url });
			final List<String> classNames = getClassesNamesInPackage(url,
					PACKAGE_NAME_TO_LOAD);
			loadedClasses = new ArrayList<Class>();

			for (final String className : classNames) {
				loadedClasses.add(Class.forName(className, true, cl));
			}
			cl.getLoadedClasses();
			cl.close();
			cl = null;
		} catch (final MalformedURLException e) {
			throw new RepositoryLoaderServiceException(e.getMessage());
		} catch (final ClassNotFoundException e) {
			throw new RepositoryLoaderServiceException(e.getMessage());
		} catch (final IOException e) {
			throw new RepositoryLoaderServiceException(e.getMessage());
		}
	}

	private List<String> getClassesNamesInPackage(final URL url,
			String packageName) {
		final List<String> classes = new ArrayList<String>();
		packageName = packageName.replaceAll("\\.", "/");
		try {
			JarURLConnection juc = (JarURLConnection) url.openConnection();
			JarFile jf = juc.getJarFile();
			final Enumeration<JarEntry> entries = jf.entries();
			while (entries.hasMoreElements()) {
				final JarEntry je = entries.nextElement();
				if ((je.getName().startsWith(packageName))
						&& (je.getName().endsWith(".class"))) {
					classes.add(je.getName().replaceAll("/", "\\.")
							.replaceAll("\\.class", ""));
				}
			}
			jf.close();
			jf = null;
			juc = null;
			System.gc();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return classes;
	}

	public void reloadRemoteRepository()
			throws RepositoryLoaderServiceException {
		try {
			fileLoader.downloadJar();
			final File jarFile = fileLoader.getJar();
			loadJAR(jarFile.toURI().toURL());
		} catch (final IOException e) {
			throw new RepositoryLoaderServiceException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public IKernelRepository getRepository()
			throws RepositoryLoaderServiceException {
		if (!loaded) {
			reloadRemoteRepository();
			loaded = true;
		}
		IKernelRepository repo = null;
		for (final Class clazz : loadedClasses) {
			if (clazz.getName().equals(KERNEL_REPOSITORY_CLASS_NAME)) {
				try {
					repo = (IKernelRepository) clazz.newInstance();// singelton?
					repo.setJarFileLocation(fileLoader.getJar());
				} catch (final InstantiationException e) {
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return repo;
	}

	@SuppressWarnings("rawtypes")
	public IGraphNodeBuilder createGraphNodeBuilder() {
		IGraphNodeBuilder builder = null;
		for (final Class clazz : loadedClasses) {
			if (clazz.getName().equals(GRAPH_NODE_BUILDER_CLASS_NAME)) {
				try {
					builder = (IGraphNodeBuilder) clazz.newInstance();
				} catch (final InstantiationException e) {
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return builder;
	}
}
