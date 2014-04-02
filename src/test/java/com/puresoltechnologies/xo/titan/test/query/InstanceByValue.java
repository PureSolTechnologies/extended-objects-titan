package com.puresoltechnologies.xo.titan.test.query;

import com.puresoltechnologies.xo.titan.api.annotation.Gremlin;

@Gremlin(value = "_().has('_xo_discriminator_A').has('value', {value})", name = "a")
public interface InstanceByValue {

	A getA();

}
