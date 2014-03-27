package com.puresoltechnologies.xo.titan.impl;

import java.util.HashSet;
import java.util.Iterator;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanNodeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;

/**
 * This class implements the XO {@link DatastorePropertyManager} for Titan
 * database.
 * 
 * @author Rick-Rainer Ludwig
 */
public class TitanStorePropertyManager
		implements
		DatastorePropertyManager<Vertex, Edge, TitanNodeMetadata, TitanRelationMetadata> {

	@Override
	public void setEntityProperty(Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanNodeMetadata> metadata,
			Object value) {
		if (value == null) {
			if (metadata.getAnnotatedMethod().getAnnotation(NotNull.class) != null) {
				throw new ConstraintViolationException("Type '"
						+ metadata.getDatastoreMetadata().getDiscriminator()
						+ "' has an annoted NotNull constrain which failed.",
						new HashSet<ConstraintViolation<?>>());
			}
		}
		vertex.setProperty(metadata.getAnnotatedMethod().getName(), value);
	}

	@Override
	public void setRelationProperty(Edge edge,
			PrimitivePropertyMethodMetadata<TitanNodeMetadata> metadata,
			Object value) {
		if (value == null) {
			if (metadata.getAnnotatedMethod().getAnnotation(NotNull.class) != null) {
				throw new ConstraintViolationException("Type '"
						+ metadata.getDatastoreMetadata().getDiscriminator()
						+ "' has an annoted NotNull constrain which failed.",
						new HashSet<ConstraintViolation<?>>());
			}
		}
		edge.setProperty(metadata.getAnnotatedMethod().getName(), value);
	}

	@Override
	public boolean hasEntityProperty(Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanNodeMetadata> metadata) {
		return vertex.getProperty(metadata.getAnnotatedMethod().getName()) != null;
	}

	@Override
	public boolean hasRelationProperty(Edge edge,
			PrimitivePropertyMethodMetadata<TitanNodeMetadata> metadata) {
		return edge.getProperty(metadata.getAnnotatedMethod().getName()) != null;
	}

	@Override
	public void removeEntityProperty(Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanNodeMetadata> metadata) {
		vertex.removeProperty(metadata.getAnnotatedMethod().getName());
	}

	@Override
	public void removeRelationProperty(Edge edge,
			PrimitivePropertyMethodMetadata<TitanNodeMetadata> metadata) {
		edge.removeProperty(metadata.getAnnotatedMethod().getName());
	}

	@Override
	public Object getEntityProperty(Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanNodeMetadata> metadata) {
		return vertex.getProperty(metadata.getAnnotatedMethod().getName());
	}

	@Override
	public Object getRelationProperty(Edge edge,
			PrimitivePropertyMethodMetadata<TitanNodeMetadata> metadata) {
		return edge.getProperty(metadata.getAnnotatedMethod().getName());
	}

	@Override
	public boolean hasSingleRelation(Vertex source,
			RelationTypeMetadata<TitanRelationMetadata> metadata,
			RelationTypeMetadata.Direction direction) {
		String label = metadata.getDatastoreMetadata().getDiscriminator();
		long count;
		switch (direction) {
		case FROM:
			count = source.query().direction(Direction.OUT).labels(label)
					.count();
			break;
		case TO:
			count = source.query().direction(Direction.IN).labels(label)
					.count();
			break;
		default:
			throw new XOException("Unkown direction '" + direction.name()
					+ "'.");
		}
		if (count > 1) {
			throw new XOException("Multiple results are available.");
		}
		return count == 1;
	}

	@Override
	public Edge getSingleRelation(Vertex source,
			RelationTypeMetadata<TitanRelationMetadata> metadata,
			RelationTypeMetadata.Direction direction) {
		String label = metadata.getDatastoreMetadata().getDiscriminator();
		Iterable<Edge> edges;
		switch (direction) {
		case FROM:
			edges = source.getEdges(Direction.OUT, label);
			break;
		case TO:
			edges = source.getEdges(Direction.IN, label);
			break;
		default:
			throw new XOException("Unkown direction '" + direction.name()
					+ "'.");
		}
		Iterator<Edge> iterator = edges.iterator();
		if (!iterator.hasNext()) {
			throw new XOException("No result is available.");
		}
		Edge result = iterator.next();
		if (iterator.hasNext()) {
			throw new XOException("Multiple results are available.");
		}
		return result;
	}

	@Override
	public Iterable<Edge> getRelations(Vertex source,
			RelationTypeMetadata<TitanRelationMetadata> metadata,
			RelationTypeMetadata.Direction direction) {
		VertexQuery query = source.query();
		switch (direction) {
		case TO:
			query = query.direction(Direction.IN);
			break;
		case FROM:
			query = query.direction(Direction.OUT);
			break;
		default:
			throw new XOException("Unknown direction '" + direction.name()
					+ "'.");
		}
		return query.edges();
	}

	@Override
	public Edge createRelation(Vertex source,
			RelationTypeMetadata<TitanRelationMetadata> metadata,
			RelationTypeMetadata.Direction direction, Vertex target) {
		String name = metadata.getDatastoreMetadata().getDiscriminator();
		switch (direction) {
		case FROM:
			return source.addEdge(name, target);
		case TO:
			return target.addEdge(name, source);
		default:
			throw new XOException("Unknown direction '" + direction.name()
					+ "'.");
		}
	}

	@Override
	public void deleteRelation(Edge edge) {
		edge.remove();
	}

	@Override
	public Vertex getTo(Edge edge) {
		return edge.getVertex(Direction.IN);
	}

	@Override
	public Vertex getFrom(Edge edge) {
		return edge.getVertex(Direction.OUT);
	}
}
