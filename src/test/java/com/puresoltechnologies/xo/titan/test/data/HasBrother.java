package com.puresoltechnologies.xo.titan.test.data;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;

@EdgeDefinition("hasBrother")
public interface HasBrother {

	@Outgoing
	Person getSibling();

	void setSibling(Person sibling);

	@Incoming
	Person getBrother();

	void setBrother(Person brother);
}
