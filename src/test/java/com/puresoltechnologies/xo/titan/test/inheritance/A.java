package com.puresoltechnologies.xo.titan.test.inheritance;

import com.puresoltechnologies.xo.titan.api.annotation.Indexed;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition("A")
public interface A extends Version {

	@Indexed
	String getIndex();

	void setIndex(String s);

}
