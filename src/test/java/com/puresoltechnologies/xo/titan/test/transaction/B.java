package com.puresoltechnologies.xo.titan.test.transaction;

import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition("B")
public interface B {

	int getValue();

	void setValue(int value);

}
