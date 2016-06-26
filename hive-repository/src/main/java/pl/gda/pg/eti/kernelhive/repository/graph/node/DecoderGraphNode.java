/**
 * Copyright (c) 2016 Gdansk University of Technology
 * Copyright (c) 2016 Adrian Boguszewski
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
package pl.gda.pg.eti.kernelhive.repository.graph.node;

import pl.gda.pg.eti.kernelhive.common.validation.ValidationResult;
import pl.gda.pg.eti.kernelhive.repository.graph.node.type.GraphNodeType;

import java.util.ArrayList;
import java.util.List;

public class DecoderGraphNode extends GenericGraphNode {

    public DecoderGraphNode(String id) {
        super(id);
        type = GraphNodeType.DECODER;
    }

    @Override
    public boolean canAddFollowingNode(IGraphNode node) {
        return node.getType().isImageType();
    }

    @Override
    public boolean canAddPreviousNode(IGraphNode node) {
        return node.getType().isImageType();
    }

    @Override
    public boolean canAddChildNode(IGraphNode node) {
        return false;
    }

    @Override
    public List<ValidationResult> validate() {
        List<ValidationResult> results = new ArrayList<>();
        // children nodes? (must be 0)
        if (getChildrenNodes() != null && getChildrenNodes().size() > 0) {
            results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
                    + name + ") of type '" + type
                    + "' cannot has children nodes",
                    ValidationResult.ValidationResultType.INVALID));
        }

        // previous validations ok?
        if (results.size() == 0) {
            results.add(new ValidationResult("Node (id: " + nodeId + ", name: "
                    + name + ") validated correctly",
                    ValidationResult.ValidationResultType.VALID));
        }
        return results;
    }
}
