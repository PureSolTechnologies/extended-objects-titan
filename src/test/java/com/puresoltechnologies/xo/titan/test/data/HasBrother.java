package com.puresoltechnologies.xo.titan.test.data;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;

@EdgeDefinition("hasBrother")
public interface HasBrother {

	void setBrother(Person brother);

	@Outgoing
	Person getBrother();

}
