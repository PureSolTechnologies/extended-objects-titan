package com.puresoltechnologies.xo.titan.test.relation.typed;

import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition("D")
public interface D {

	TypeA getTypeA();

	TypeB getTypeB();

}
