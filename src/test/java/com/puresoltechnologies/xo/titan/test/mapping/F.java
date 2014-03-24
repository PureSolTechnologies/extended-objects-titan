package com.puresoltechnologies.xo.titan.test.mapping;

import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition("F")
public interface F {

	E2F getE2F();

	void setValue(String value);

	String getValue();

}
