package com.puresoltechnologies.xo.titan.impl;

import java.util.Iterator;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanEdgeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanPropertyMetadata;
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
	DatastorePropertyManager<Vertex, Edge, TitanPropertyMetadata, TitanEdgeMetadata> {

    @Override
    public void setEntityProperty(Vertex vertex,
	    PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata,
	    Object value) {
	vertex.setProperty(metadata.getDatastoreMetadata().getName(), value);
    }

    @Override
    public void setRelationProperty(Edge edge,
	    PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata,
	    Object value) {
	edge.setProperty(metadata.getDatastoreMetadata().getName(), value);
    }

    @Override
    public boolean hasEntityProperty(Vertex vertex,
	    PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata) {
	return vertex.getProperty(metadata.getDatastoreMetadata().getName()) != null;
    }

    @Override
    public boolean hasRelationProperty(Edge edge,
	    PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata) {
	return edge.getProperty(metadata.getDatastoreMetadata().getName()) != null;
    }

    @Override
    public void removeEntityProperty(Vertex vertex,
	    PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata) {
	vertex.removeProperty(metadata.getDatastoreMetadata().getName());
    }

    @Override
    public void removeRelationProperty(Edge edge,
	    PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata) {
	edge.removeProperty(metadata.getDatastoreMetadata().getName());
    }

    @Override
    public Object getEntityProperty(Vertex vertex,
	    PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata) {
	return vertex.getProperty(metadata.getDatastoreMetadata().getName());
    }

    @Override
    public Object getRelationProperty(Edge edge,
	    PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata) {
	return edge.getProperty(metadata.getDatastoreMetadata().getName());
    }

    @Override
    public boolean hasSingleRelation(Vertex source,
	    RelationTypeMetadata<TitanEdgeMetadata> metadata,
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
	    RelationTypeMetadata<TitanEdgeMetadata> metadata,
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
	    RelationTypeMetadata<TitanEdgeMetadata> metadata,
	    RelationTypeMetadata.Direction direction) {
	VertexQuery query = source.query();
	String discriminator = metadata.getDatastoreMetadata()
		.getDiscriminator();
	switch (direction) {
	case TO:
	    query = query.direction(Direction.IN).labels(discriminator);
	    break;
	case FROM:
	    query = query.direction(Direction.OUT).labels(discriminator);
	    break;
	default:
	    throw new XOException("Unknown direction '" + direction.name()
		    + "'.");
	}
	return query.edges();
    }

    @Override
    public Edge createRelation(Vertex source,
	    RelationTypeMetadata<TitanEdgeMetadata> metadata,
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
