package pl.gda.pg.eti.kernelhive.repository.kernel.repository;

import java.util.List;

import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeType;

public interface IKernelRepositoryEntry {

	GraphNodeType getGraphNodeType();

	List<IKernelPathEntry> getKernelPaths();

	IKernelPathEntry getKernelPathEntryForName(String name);

	IKernelPathEntry getKernelPathEntryForId(String id);

	String getDescription();
}
