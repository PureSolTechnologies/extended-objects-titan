package com.puresoltechnologies.xo.titan.test.relation.typed;

import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition("C")
public interface C {

	TypeA getTypeA();

	TypeB getTypeB();

}
