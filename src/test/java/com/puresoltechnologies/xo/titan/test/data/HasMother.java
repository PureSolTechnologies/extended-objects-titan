package com.puresoltechnologies.xo.titan.test.data;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeType;

@EdgeType("hasMother")
public interface HasMother {

	void setBrother(Person mother);

	Person getMother();

}
