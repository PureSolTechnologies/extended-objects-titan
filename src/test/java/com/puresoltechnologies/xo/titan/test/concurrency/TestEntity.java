package com.puresoltechnologies.xo.titan.test.concurrency;

import java.util.concurrent.TimeUnit;

import com.buschmais.cdo.api.annotation.ImplementedBy;
import com.buschmais.cdo.api.proxy.ProxyMethod;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;
import com.tinkerpop.blueprints.Vertex;

@VertexDefinition
public interface TestEntity {

	@ImplementedBy(IncrementAndGet.class)
	int incrementAndGet();

	public class IncrementAndGet implements ProxyMethod<Vertex> {

		@Override
		public Object invoke(Vertex node, Object instance, Object[] args)
				throws Exception {
			Integer value = node.getProperty("value");
			if (value == null) {
				value = 0;
			}
			TimeUnit.SECONDS.sleep(5);
			value++;
			node.setProperty("value", value);
			return value;
		}
	}

}
