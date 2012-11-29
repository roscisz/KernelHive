package pl.gda.pg.eti.kernelhive.repository.loader;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import pl.gda.pg.eti.kernelhive.repository.configuration.RepositoryConfiguration;
import pl.gda.pg.eti.kernelhive.repository.graph.node.IGraphNodeBuilder;
import pl.gda.pg.eti.kernelhive.repository.kernel.repository.IKernelRepository;

public class RepositoryLoaderService {

	private static final String KERNEL_REPOSITORY_CLASS_NAME = "pl.gda.pg.eti.kernelhive.repository.kernel.repository.KernelRepository";
	private static final String GRAPH_NODE_BUILDER_CLASS_NAME = "pl.gda.pg.eti.kernelhive.repository.graph.node.GraphNodeBuilder";
	private static final String PACKAGE_NAME_TO_LOAD = "pl.gda.pg.eti.kernelhive";

	private static RepositoryLoaderService _service = null;

	private String jarUrlStr = null;
	@SuppressWarnings("rawtypes")
	private List<Class> loadedClasses;

	public static RepositoryLoaderService getInstance() {
		if (_service == null) {
			_service = new RepositoryLoaderService();
			_service.loadRemoteRepository();
		}
		return _service;
	}

	private RepositoryLoaderService() {

	}

	@SuppressWarnings("rawtypes")
	private void loadRemoteJAR(String jarUrl) {
		CustomURLClassLoader cl = null;
		try {
			URL url = new URL("jar:" + jarUrl + "!/");
			url.openConnection();
			cl = new CustomURLClassLoader(new URL[] { url });
			List<String> classNames = getClassesNamesInPackage(url,
					PACKAGE_NAME_TO_LOAD);
			loadedClasses = new ArrayList<Class>();

			for (String className : classNames) {
				loadedClasses.add(Class.forName(className, true, cl));
			}
			cl.getLoadedClasses();
			cl.close();
			cl = null;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<String> getClassesNamesInPackage(URL url, String packageName) {
		List<String> classes = new ArrayList<String>();
		packageName = packageName.replaceAll("\\.", "/");
		// System.out.println("Jar " + url + " looking for " + packageName);
		try {
			JarURLConnection juc = (JarURLConnection) url.openConnection();
			JarFile jf = juc.getJarFile();
			Enumeration<JarEntry> entries = jf.entries();
			while (entries.hasMoreElements()) {
				JarEntry je = entries.nextElement();
				if ((je.getName().startsWith(packageName))
						&& (je.getName().endsWith(".class"))) {
					// System.out.println("Found " +
					// je.getName().replaceAll("/", "\\."));
					classes.add(je.getName().replaceAll("/", "\\.")
							.replaceAll("\\.class", ""));
				}
			}
			jf.close();
			jf = null;
			juc = null;
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classes;
	}

	private String getJarVersion(String jarUrl) {
		String version = null;
		try {
			URL url = new URL("jar:" + jarUrl + "!/");
			JarURLConnection juc = (JarURLConnection) url.openConnection();
			JarFile jf = juc.getJarFile();
			Attributes attrs = jf.getManifest().getMainAttributes();
			version = attrs.getValue("Manifest-Version");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return version;
	}

	private void loadRemoteRepository() {
		jarUrlStr = RepositoryConfiguration.getInstance()
				.getKernelRepositoryJarURL();
		loadRemoteJAR(jarUrlStr);
	}

	public String getRepositoryVersion() {
		return getJarVersion(jarUrlStr);
	}

	@SuppressWarnings("rawtypes")
	public IKernelRepository getRepository() {
		IKernelRepository repo = null;
		for (Class clazz : loadedClasses) {
			if (clazz.getName().equals(KERNEL_REPOSITORY_CLASS_NAME)) {
				try {
					repo = (IKernelRepository) clazz.newInstance();// singelton?
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return repo;
	}

	@SuppressWarnings("rawtypes")
	public IGraphNodeBuilder createGraphNodeBuilder() {
		IGraphNodeBuilder builder = null;
		for (Class clazz : loadedClasses) {
			if (clazz.getName().equals(GRAPH_NODE_BUILDER_CLASS_NAME)) {
				try {
					builder = (IGraphNodeBuilder) clazz.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return builder;
	}
}
