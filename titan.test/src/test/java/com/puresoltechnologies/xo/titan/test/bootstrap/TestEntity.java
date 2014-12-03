package com.puresoltechnologies.xo.titan.test.bootstrap;

import com.puresoltechnologies.xo.titan.api.annotation.Indexed;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition
public interface TestEntity {

	@Indexed
	String getName();

	void setName(String name);

	TestEntity getTestEntity();

	void setTestEntity(TestEntity testEntity);

}
