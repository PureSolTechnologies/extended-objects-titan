package com.puresoltechnologies.xo.titan.test.mapping;

import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition(value = "D", usingIndexedPropertyOf = A.class)
public interface D extends A {
}
