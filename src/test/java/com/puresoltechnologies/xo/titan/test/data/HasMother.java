package com.puresoltechnologies.xo.titan.test.data;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;

@EdgeDefinition("hasMother")
public interface HasMother {

	void setBrother(Person mother);

	@Outgoing
	Person getMother();

}
