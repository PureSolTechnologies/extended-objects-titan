package com.puresoltechnologies.xo.titan.test.data;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeType;

@EdgeType("hasBrother")
public interface HasBrother {

	void setBrother(Person brother);

	Person getBrother();

}
