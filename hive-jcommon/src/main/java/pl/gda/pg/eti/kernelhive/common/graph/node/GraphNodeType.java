/**
 * Copyright (c) 2014 Gdansk University of Technology
 * Copyright (c) 2014 Pawel Rosciszewski
 * Copyright (c) 2014 Marcel Schally-Kacprzak
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
package pl.gda.pg.eti.kernelhive.common.graph.node;

public enum GraphNodeType {
	GENERIC("generic"), MERGER("DataMerger"), PARTITIONER("DataPartitioner"), PROCESSOR(
			"DataProcessor"), COMPOSITE("composite"), MASTERSLAVE("masterslave"), DAC(
			"dac"), EXPANDABLE("expandable");

	private String type;

	GraphNodeType(final String str) {
		this.type = str;
	}

	@Override
	public String toString() {
		return type;
	}

	public static GraphNodeType getType(final String type) {
		final GraphNodeType[] types = GraphNodeType.values();
		for (final GraphNodeType t : types) {
			if (t.toString().equalsIgnoreCase(type)) {
				return t;
			}
		}
		return null;
	}
}
