package com.puresoltechnologies.xo.titan.test.query;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;

@EdgeDefinition("A2B")
public interface A2B {

	@Outgoing
	A getA();

	@Incoming
	B getB();

}
