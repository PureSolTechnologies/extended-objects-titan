package com.puresoltechnologies.xo.titan.test.validation;

import javax.validation.constraints.NotNull;

import com.puresoltechnologies.xo.titan.api.annotation.Indexed;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition("A")
public interface A {

	@NotNull
	@Indexed
	String getName();

	void setName(String name);

	@NotNull
	B getB();

	void setB(B b);
}
