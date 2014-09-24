package com.puresoltechnologies.xo.titan.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.buschmais.xo.api.ResultIterator;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.DatastorePropertyManager;
import com.buschmais.xo.spi.datastore.TypeMetadataSet;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.method.PrimitivePropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanPropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanVertexMetadata;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanGraphQuery;
import com.tinkerpop.blueprints.Vertex;

/**
 * This class implements the XO {@link DatastorePropertyManager} for Titan
 * database.
 * 
 * @author Rick-Rainer Ludwig
 */
public class TitanStoreVertexManager
		implements
		DatastoreEntityManager<Object, Vertex, TitanVertexMetadata, String, TitanPropertyMetadata> {

	private final TitanGraph titanGraph;

	TitanStoreVertexManager(TitanGraph titanGraph) {
		this.titanGraph = titanGraph;
	}

	@Override
	public void setProperty(Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata,
			Object value) {
		vertex.setProperty(metadata.getDatastoreMetadata().getName(), value);
	}

	@Override
	public boolean hasProperty(Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata) {
		return vertex.getProperty(metadata.getDatastoreMetadata().getName()) != null;
	}

	@Override
	public void removeProperty(Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata) {
		vertex.removeProperty(metadata.getDatastoreMetadata().getName());
	}

	@Override
	public Object getProperty(Vertex vertex,
			PrimitivePropertyMethodMetadata<TitanPropertyMetadata> metadata) {
		return vertex.getProperty(metadata.getDatastoreMetadata().getName());
	}

	@Override
	public boolean isEntity(Object o) {
		return Vertex.class.isAssignableFrom(o.getClass());
	}

	@Override
	public Set<String> getEntityDiscriminators(Vertex vertex) {
		Set<String> discriminators = new HashSet<>();
		for (String key : vertex.getPropertyKeys()) {
			if (key.startsWith(TitanStoreSession.XO_DISCRIMINATORS_PROPERTY)) {
				String discriminator = vertex.getProperty(key);
				discriminators.add(discriminator);
			}
		}
		if (discriminators.size() == 0) {
			throw new XOException(
					"A vertex was found without discriminators. Does another framework alter the database?");
		}
		return discriminators;
	}

	@Override
	public Object getEntityId(Vertex vertex) {
		return vertex.getId();
	}

	@Override
	public Vertex createEntity(
			TypeMetadataSet<EntityTypeMetadata<TitanVertexMetadata>> types,
			Set<String> discriminators,
			Map<PrimitivePropertyMethodMetadata<TitanPropertyMetadata>, Object> exampleEntity) {
		Vertex vertex = titanGraph.addVertex(null);
		for (String discriminator : discriminators) {
			vertex.setProperty(TitanStoreSession.XO_DISCRIMINATORS_PROPERTY
					+ discriminator, discriminator);
		}
		return vertex;
	}

	@Override
	public void deleteEntity(Vertex vertex) {
		vertex.remove();
	}

	@Override
	public ResultIterator<Vertex> findEntity(
			EntityTypeMetadata<TitanVertexMetadata> type,
			String discriminator,
			Map<PrimitivePropertyMethodMetadata<TitanPropertyMetadata>, Object> values) {
		if (values.size() > 1) {
			throw new XOException(
					"Only one property value is supported for find operation");
		}
		TitanGraphQuery<?> query = titanGraph.query();
		query = query.has(TitanStoreSession.XO_DISCRIMINATORS_PROPERTY
				+ discriminator);

		IndexedPropertyMethodMetadata<?> indexedProperty = type
				.getDatastoreMetadata().getIndexedProperty();
		if (indexedProperty == null) {
			indexedProperty = type.getIndexedProperty();
		}
		if (indexedProperty == null) {
			throw new XOException("Type "
					+ type.getAnnotatedType().getAnnotatedElement().getName()
					+ " has no indexed property.");
		}
		PrimitivePropertyMethodMetadata<TitanPropertyMetadata> propertyMethodMetadata = indexedProperty
				.getPropertyMethodMetadata();
		String name = propertyMethodMetadata.getDatastoreMetadata().getName();
		query = query.has(name, values.values().iterator().next());
		Iterable<Vertex> vertices = query.vertices();
		final Iterator<Vertex> iterator = vertices.iterator();

		return new ResultIterator<Vertex>() {

			@Override
			public boolean hasNext() {
				return iterator.hasNext();
			}

			@Override
			public Vertex next() {
				return iterator.next();
			}

			@Override
			public void remove() {
				iterator.remove();
			}

			@Override
			public void close() {
				// intentionally left empty
			}
		};
	}

	@Override
	public void migrateEntity(
			Vertex vertex,
			TypeMetadataSet<EntityTypeMetadata<TitanVertexMetadata>> types,
			Set<String> discriminators,
			TypeMetadataSet<EntityTypeMetadata<TitanVertexMetadata>> targetTypes,
			Set<String> targetDiscriminators) {
		for (String discriminator : discriminators) {
			if (!targetDiscriminators.contains(discriminator)) {
				vertex.removeProperty(TitanStoreSession.XO_DISCRIMINATORS_PROPERTY
						+ discriminator);
			}
		}
		for (String discriminator : targetDiscriminators) {
			if (!discriminators.contains(discriminator)) {
				vertex.setProperty(TitanStoreSession.XO_DISCRIMINATORS_PROPERTY
						+ discriminator, discriminator);
			}
		}
	}

	@Override
	public void flushEntity(Vertex vertex) {
		// intentionally left empty
	}

}
