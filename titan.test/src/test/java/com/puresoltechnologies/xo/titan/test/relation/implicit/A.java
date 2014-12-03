package com.puresoltechnologies.xo.titan.test.relation.implicit;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition
public interface A {

	@Outgoing
	@ImplicitOneToOne
	B getOneToOne();

	void setOneToOne(B b);

}
