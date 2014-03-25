package com.puresoltechnologies.xo.titan.test.query;

import com.puresoltechnologies.xo.titan.api.annotation.Gremlin;

@Gremlin("match (a:A) where a.value={value} return a")
public interface InstanceByValue {

	A getA();

}
