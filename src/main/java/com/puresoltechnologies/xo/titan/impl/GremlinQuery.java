package com.puresoltechnologies.xo.titan.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.puresoltechnologies.xo.titan.api.annotation.Gremlin;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.pipes.Pipe;

public class GremlinQuery implements DatastoreQuery<Gremlin> {

	private final TitanGraph titanGraph;

	GremlinQuery(TitanGraph titanGraph) {
		this.titanGraph = titanGraph;
	}

	@Override
	public ResultIterator<Map<String, Object>> execute(String query,
			Map<String, Object> parameters) {
		final GremlinExpression gremlinExpression = GremlinManager
				.getGremlinExpression(query, parameters);
		return execute(parameters, gremlinExpression);
	}

	@Override
	public ResultIterator<Map<String, Object>> execute(Gremlin query,
			Map<String, Object> parameters) {
		final GremlinExpression gremlinExpression = GremlinManager
				.getGremlinExpression(query, parameters);
		return execute(parameters, gremlinExpression);
	}

	private ResultIterator<Map<String, Object>> execute(
			Map<String, Object> parameters,
			final GremlinExpression gremlinExpression) {
		String expression = gremlinExpression.getExpression();
		@SuppressWarnings("unchecked")
		final Pipe<Vertex, ?> pipe = com.tinkerpop.gremlin.groovy.Gremlin
				.compile(expression);
		if (parameters.containsKey("this")) {
			Object setThis = parameters.get("this");
			if (Vertex.class.isAssignableFrom(setThis.getClass())) {
				Vertex vertex = (Vertex) setThis;
				pipe.setStarts(Arrays.asList(vertex));
			} else if (Edge.class.isAssignableFrom(setThis.getClass())) {
				Edge edge = (Edge) setThis;
				pipe.setStarts(Arrays.asList(edge.getVertex(Direction.IN),
						edge.getVertex(Direction.OUT)));
			} else {
				throw new XOException("Unsupported start point '"
						+ String.valueOf(setThis) + "' (class="
						+ setThis.getClass() + ")");
			}
		} else {
			pipe.setStarts(titanGraph.query().vertices());
		}
		return new ResultIterator<Map<String, Object>>() {

			@Override
			public boolean hasNext() {
				return pipe.hasNext();
			}

			@Override
			public Map<String, Object> next() {
				Map<String, Object> results = new HashMap<>();
				Object next = pipe.next();
				if (next instanceof Vertex) {
					results.put(gremlinExpression.getResultName(), next);
				} else if (next instanceof Edge) {
					results.put(gremlinExpression.getResultName(), next);
				} else if (next instanceof Map) {
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) next;
					results.putAll(map);
				} else {
					results.put("unknown_type", next);
				}
				return results;
			}

			@Override
			public void remove() {
				pipe.remove();
			}

			@Override
			public void close() {
				// there is no close required in pipe
			}
		};
	}

}
