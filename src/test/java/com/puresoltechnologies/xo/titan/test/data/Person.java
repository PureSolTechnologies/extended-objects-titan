package com.puresoltechnologies.xo.titan.test.data;

import com.puresoltechnologies.xo.titan.api.annotation.VertexType;

@VertexType
public interface Person {

	void setFirstName(String firstName);

	String getFirstName();

	void setLastName(String lastName);

	String getLastName();

}
