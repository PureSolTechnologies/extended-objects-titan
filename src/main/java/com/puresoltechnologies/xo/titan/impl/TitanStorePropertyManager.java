package com.puresoltechnologies.xo.titan.impl;

import com.buschmais.cdo.spi.datastore.DatastorePropertyManager;
import com.buschmais.cdo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.cdo.spi.metadata.type.RelationTypeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanPrimitivePropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class TitanStorePropertyManager
		implements
		DatastorePropertyManager<Vertex, Edge, TitanPrimitivePropertyMetadata, TitanRelationMetadata> {

	@Override
	public void setEntityProperty(
			Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanPrimitivePropertyMetadata> metadata,
			Object value) {
		vertex.setProperty(metadata.getAnnotatedMethod().getName(), value);
	}

	@Override
	public void setRelationProperty(
			Edge edge,
			PrimitivePropertyMethodMetadata<TitanPrimitivePropertyMetadata> metadata,
			Object value) {
		edge.setProperty(metadata.getAnnotatedMethod().getName(), value);
	}

	@Override
	public boolean hasEntityProperty(
			Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanPrimitivePropertyMetadata> metadata) {
		return vertex.getProperty(metadata.getAnnotatedMethod().getName()) != null;
	}

	@Override
	public boolean hasRelationProperty(
			Edge edge,
			PrimitivePropertyMethodMetadata<TitanPrimitivePropertyMetadata> metadata) {
		return edge.getProperty(metadata.getAnnotatedMethod().getName()) != null;
	}

	@Override
	public void removeEntityProperty(
			Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanPrimitivePropertyMetadata> metadata) {
		vertex.removeProperty(metadata.getAnnotatedMethod().getName());
	}

	@Override
	public void removeRelationProperty(
			Edge edge,
			PrimitivePropertyMethodMetadata<TitanPrimitivePropertyMetadata> metadata) {
		edge.removeProperty(metadata.getAnnotatedMethod().getName());
	}

	@Override
	public Object getEntityProperty(
			Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanPrimitivePropertyMetadata> metadata) {
		return vertex.getProperty(metadata.getAnnotatedMethod().getName());
	}

	@Override
	public Object getRelationProperty(
			Edge edge,
			PrimitivePropertyMethodMetadata<TitanPrimitivePropertyMetadata> metadata) {
		return edge.getProperty(metadata.getAnnotatedMethod().getName());
	}

	@Override
	public boolean hasSingleRelation(Vertex source,
			RelationTypeMetadata<TitanRelationMetadata> metadata,
			RelationTypeMetadata.Direction direction) {
		// TODO
		return false;
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
		// TODO
		return null;
	}

	@Override
	public Edge createRelation(Vertex source,
			RelationTypeMetadata<TitanRelationMetadata> metadata,
			RelationTypeMetadata.Direction direction, Vertex target) {
		// TODO
		return null;
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
