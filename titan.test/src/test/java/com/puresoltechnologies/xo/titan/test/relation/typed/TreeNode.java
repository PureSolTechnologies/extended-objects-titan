package com.puresoltechnologies.xo.titan.test.relation.typed;

import java.util.List;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition
public interface TreeNode {

	void setName(String name);

	String getName();

	@Incoming
	TreeNodeRelation getParent();

	@Outgoing
	List<TreeNodeRelation> getChildren();

}
