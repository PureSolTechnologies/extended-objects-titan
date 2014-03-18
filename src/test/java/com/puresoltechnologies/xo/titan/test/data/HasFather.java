package com.puresoltechnologies.xo.titan.test.data;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeType;

@EdgeType("hasFather")
public interface HasFather {

	void setBrother(Person father);

	Person getFather();

}
