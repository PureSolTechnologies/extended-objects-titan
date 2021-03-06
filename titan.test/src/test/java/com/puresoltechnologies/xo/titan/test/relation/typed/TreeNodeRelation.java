package com.puresoltechnologies.xo.titan.test.relation.typed;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;

@EdgeDefinition
public interface TreeNodeRelation {

	int getVersion();

	void setVersion(int version);

	@Incoming
	TreeNode getChild();

	@Outgoing
	TreeNode getParent();
}
