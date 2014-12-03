package com.puresoltechnologies.xo.titan.impl;

import java.lang.annotation.Annotation;

import com.buschmais.xo.spi.datastore.DatastoreEntityManager;
import com.buschmais.xo.spi.datastore.DatastoreQuery;
import com.buschmais.xo.spi.datastore.DatastoreRelationManager;
import com.buschmais.xo.spi.datastore.DatastoreSession;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.puresoltechnologies.xo.titan.api.annotation.Gremlin;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanEdgeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanPropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanVertexMetadata;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * This class implements a XO DatastoreSession for Titan database.
 * 
 * @author Rick-Rainer Ludwig
 */
public class TitanStoreSession
	implements
	DatastoreSession<Object, Vertex, TitanVertexMetadata, String, Object, Edge, TitanEdgeMetadata, String, TitanPropertyMetadata> {

    /**
     * This constant contains the prefix for discriminator properties.
     */
    public static final String XO_DISCRIMINATORS_PROPERTY = "_xo_discriminator_";

    /**
     * This field contains the Titan graph as {@link TitanGraph} object.
     */
    private final TitanGraph titanGraph;
    private final TitanStoreTransaction transaction;

    private final TitanStoreVertexManager vertexManager;
    private final TitanStoreEdgeManager edgeManager;

    /**
     * This is the initial value constructor.
     * 
     * @param titanGraph
     *            is the Titan graph as TitanGraph object on which this session
     *            shall work on.
     */
    public TitanStoreSession(TitanGraph titanGraph) {
	this.titanGraph = titanGraph;
	this.transaction = new TitanStoreTransaction(titanGraph);
	this.vertexManager = new TitanStoreVertexManager(titanGraph);
	this.edgeManager = new TitanStoreEdgeManager(titanGraph);
    }

    /**
     * Returns the Titan graph which is currently opened.
     * 
     * @return A TitanGraph object is returned.
     */
    public final TitanGraph getTitanGraph() {
	return titanGraph;
    }

    @Override
    public DatastoreTransaction getDatastoreTransaction() {
	return transaction;
    }

    @Override
    public void close() {
	// Nothing to do here...
    }

    @Override
    public DatastoreEntityManager<Object, Vertex, TitanVertexMetadata, String, TitanPropertyMetadata> getDatastoreEntityManager() {
	return vertexManager;
    }

    @Override
    public DatastoreRelationManager<Vertex, Object, Edge, TitanEdgeMetadata, String, TitanPropertyMetadata> getDatastoreRelationManager() {
	return edgeManager;
    }

    @Override
    public Class<? extends Annotation> getDefaultQueryLanguage() {
	return Gremlin.class;
    }

    @Override
    public <QL extends Annotation> DatastoreQuery<QL> createQuery(
	    Class<QL> queryLanguage) {
	if (!queryLanguage.equals(Gremlin.class)) {
	    throw new IllegalArgumentException("Query language "
		    + queryLanguage.getName() + " is not supported.");
	}
	@SuppressWarnings("unchecked")
	DatastoreQuery<QL> query = (DatastoreQuery<QL>) new GremlinQuery(
		titanGraph);
	return query;
    }

}
