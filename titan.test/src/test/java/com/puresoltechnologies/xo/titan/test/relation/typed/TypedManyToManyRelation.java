package com.puresoltechnologies.xo.titan.test.relation.typed;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;

@EdgeDefinition("ManyToMany")
public interface TypedManyToManyRelation extends TypedRelation {

	@Outgoing
	A getA();

	@Incoming
	B getB();

}
