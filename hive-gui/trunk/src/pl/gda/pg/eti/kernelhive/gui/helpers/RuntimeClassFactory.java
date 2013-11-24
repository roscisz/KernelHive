/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.gda.pg.eti.kernelhive.gui.helpers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import pl.gda.pg.eti.kernelhive.gui.component.workflow.preview.IPreviewProvider;

/**
 *
 * @author szymon
 */
public class RuntimeClassFactory {

	private static final Logger LOGGER = Logger.getLogger(RuntimeClassFactory.class.getName());

	public static IPreviewProvider getPreviewProvider(File sourceFile) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		try (StandardJavaFileManager fileManager = compiler
				.getStandardFileManager(null, null, null)) {
			Iterable javaFiles = fileManager.getJavaFileObjects(sourceFile);
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(tmpDir));
			List<String> options = Arrays.asList("-d", tmpDir.getAbsolutePath());
			compiler.getTask(null, fileManager, null, null, null, javaFiles)
					.call();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error during runtime compilation", e);
		}

		try {
			URL[] urls = new URL[]{new URL("file://" + tmpDir + "/")};
			System.out.println(urls[0].toString());
			URLClassLoader ucl = new URLClassLoader(urls);
			Class clazz = ucl.loadClass("pl.gda.pg.eti.kernelhive.gui.component.workflow.preview.PreviewProvider");
			System.out.println("Class has been successfully loaded");

			IPreviewProvider provider = (IPreviewProvider) clazz.newInstance();
			return provider;
		} catch (MalformedURLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
			Logger.getLogger(RuntimeClassFactory.class.getName())
					.log(Level.SEVERE, "Error while creating PreviewProider", ex);
			return null;
		}
	}
}
