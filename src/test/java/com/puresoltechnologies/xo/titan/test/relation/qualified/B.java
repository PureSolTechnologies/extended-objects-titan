package com.puresoltechnologies.xo.titan.test.relation.qualified;

import java.util.List;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition
public interface B {

	@Incoming
	@QualifiedOneToOne
	A getOneToOne();

	void setOneToOne(A a);

	@Incoming
	@QualifiedOneToMany
	A getManyToOne();

	@Incoming
	@QualifiedManyToMany
	List<A> getManyToMany();

}
