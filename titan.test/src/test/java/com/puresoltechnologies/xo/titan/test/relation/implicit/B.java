package com.puresoltechnologies.xo.titan.test.relation.implicit;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition
public interface B {

	@Incoming
	@ImplicitOneToOne
	A getOneToOne();

	void setOneToOne(A a);
}
