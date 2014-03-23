package com.puresoltechnologies.xo.titan.test.delegate;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;

@EdgeDefinition("RELATION")
public interface A2B {

	@Outgoing
	A getA();

	@Incoming
	B getB();
}
