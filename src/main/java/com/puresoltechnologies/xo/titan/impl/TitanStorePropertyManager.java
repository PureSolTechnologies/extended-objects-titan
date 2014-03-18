package com.puresoltechnologies.xo.titan.impl;

import com.buschmais.cdo.api.CdoException;
import com.buschmais.cdo.spi.datastore.DatastorePropertyManager;
import com.buschmais.cdo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.cdo.spi.metadata.type.RelationTypeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanNodeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.VertexQuery;

public class TitanStorePropertyManager
		implements
		DatastorePropertyManager<Vertex, Edge, TitanNodeMetadata, TitanRelationMetadata> {

	@Override
	public void setEntityProperty(Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanNodeMetadata> metadata,
			Object value) {
		vertex.setProperty(metadata.getAnnotatedMethod().getName(), value);
	}

	@Override
	public void setRelationProperty(Edge edge,
			PrimitivePropertyMethodMetadata<TitanNodeMetadata> metadata,
			Object value) {
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
		return getSingleRelation(source, metadata, direction) != null;
	}

	@Override
	public Edge getSingleRelation(Vertex source,
			RelationTypeMetadata<TitanRelationMetadata> metadata,
			RelationTypeMetadata.Direction direction) {
		// TODO
		return null;
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
			throw new CdoException("Unknown direction '" + direction.name()
					+ "'.");
		}
		return query.edges();
	}

	@Override
	public Edge createRelation(Vertex source,
			RelationTypeMetadata<TitanRelationMetadata> metadata,
			RelationTypeMetadata.Direction direction, Vertex target) {
		String name = metadata.getAnnotatedType().getName();
		switch (direction) {
		case TO:
			return source.addEdge(name, target);
		case FROM:
			return target.addEdge(name, source);
		default:
			throw new CdoException("Unknown direction '" + direction.name()
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
