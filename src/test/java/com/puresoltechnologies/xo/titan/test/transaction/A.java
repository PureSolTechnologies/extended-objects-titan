package com.puresoltechnologies.xo.titan.test.transaction;

import java.util.List;

import com.buschmais.xo.api.annotation.ImplementedBy;
import com.buschmais.xo.api.annotation.ResultOf;
import com.buschmais.xo.api.annotation.ResultOf.Parameter;
import com.buschmais.xo.api.proxy.ProxyMethod;
import com.puresoltechnologies.xo.titan.api.annotation.Gremlin;
import com.puresoltechnologies.xo.titan.api.annotation.Indexed;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;
import com.tinkerpop.blueprints.Vertex;

@VertexDefinition("A")
public interface A {

	@Indexed
	String getValue();

	void setValue(String value);

	List<B> getListOfB();

	@ImplementedBy(ThrowException.class)
	void throwException(String value) throws Exception;

	@ImplementedBy(ThrowRuntimeException.class)
	void throwRuntimeException(String value);

	@ResultOf
	ByValue getByValue(@Parameter("value") String value);

	@Gremlin("_().has('value', {value})")
	interface ByValue {
		A getA();
	}

	class ThrowException implements ProxyMethod<Vertex> {
		@Override
		public Object invoke(Vertex node, Object instance, Object[] args)
				throws Exception {
			((A) instance).setValue((String) args[0]);
			throw new Exception();
		}
	}

	class ThrowRuntimeException implements ProxyMethod<Vertex> {
		@Override
		public Object invoke(Vertex node, Object instance, Object[] args) {
			((A) instance).setValue((String) args[0]);
			throw new RuntimeException();
		}
	}
}
