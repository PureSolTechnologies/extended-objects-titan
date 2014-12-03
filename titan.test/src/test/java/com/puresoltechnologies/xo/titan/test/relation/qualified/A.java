package com.puresoltechnologies.xo.titan.test.relation.qualified;

import java.util.List;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition
public interface A {

	@Outgoing
	@QualifiedOneToOne
	B getOneToOne();

	void setOneToOne(B b);

	@Outgoing
	@QualifiedOneToMany
	List<B> getOneToMany();

	@Outgoing
	@QualifiedManyToMany
	List<B> getManyToMany();

}
