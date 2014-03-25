package com.puresoltechnologies.xo.titan.test.query;

import com.puresoltechnologies.xo.titan.api.annotation.Indexed;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition("A")
public interface A {

	@Indexed
	String getValue();

	void setValue(String value);

	A2B getA2B();

}
