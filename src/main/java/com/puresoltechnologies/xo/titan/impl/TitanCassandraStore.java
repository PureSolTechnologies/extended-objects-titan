package com.puresoltechnologies.xo.titan.impl;

import java.net.URI;
import java.util.Collection;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.xo.api.CompositeObject;
import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.Datastore;
import com.buschmais.xo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;
import com.buschmais.xo.spi.metadata.type.EntityTypeMetadata;
import com.buschmais.xo.spi.metadata.type.RelationTypeMetadata;
import com.buschmais.xo.spi.metadata.type.TypeMetadata;
import com.buschmais.xo.spi.reflection.AnnotatedType;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanEdgeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanIndexedPropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanVertexMetadata;
import com.thinkaurelius.titan.core.KeyMaker;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanKey;
import com.thinkaurelius.titan.core.TitanType;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;

/**
 * <p>
 * This class implements an XO {@link Datastore} for Titan on Cassandra.
 * </p>
 * <p>
 * For details have a look to <a
 * href="https://github.com/thinkaurelius/titan/wiki/Using-Cassandra"
 * >https://github.com/thinkaurelius/titan/wiki/Using-Cassandra</a>
 * </p>
 * 
 * @author Rick-Rainer Ludwig
 */
public class TitanCassandraStore
	implements
	Datastore<TitanStoreSession, TitanVertexMetadata, String, TitanEdgeMetadata, String> {

    private static final Logger logger = LoggerFactory
	    .getLogger(TitanCassandraStore.class);

    /**
     * This constant contains the default name of the Titan keyspace which is
     * set to {@value #DEFAULT_TITAN_KEYSPACE}.
     */
    public static final String DEFAULT_TITAN_KEYSPACE = "titan";

    /**
     * This constant contains the default port
     * {@value #DEFAULT_CASSANDRA_THRIFT_PORT} for the Thrift interface of
     * Cassandra.
     */
    public static final int DEFAULT_CASSANDRA_THRIFT_PORT = 9160;

    /**
     * This constant contains the name of the index to be used for properties.
     */
    public static final String INDEX_NAME = "standard";

    /**
     * This is a helper method to retrieve the keyspace name from a store URI.
     * The keyspace is taken from the path part of the URI and may be empty, if
     * the default keyspace {@value TitanCassandraStore#DEFAULT_TITAN_KEYSPACE}
     * is to be used.
     * 
     * @param uri
     *            is the URI where the keyspace name is to be extracted from.
     * @return The name of the keyspace is returned as {@link String} .
     */
    public static String retrieveKeyspaceFromURI(URI uri) {
	String path = uri.getPath();
	if (path.startsWith("/")) {
	    path = path.substring(1);
	}
	if (path.endsWith("/")) {
	    path = path.substring(0, path.length() - 1);
	}
	String[] splits = path.split("/");
	if (splits.length > 1) {
	    throw new XOException(
		    "The URI for this store may only contain a single path entry for the keyspace.");
	}
	return splits[0];
    }

    /**
     * This field contains the whole titanGraph after connection to the
     * database.
     */
    private TitanGraph titanGraph = null;

    /**
     * This field contains the Cassandra host to connect to.
     */
    private final String host;
    /**
     * This field contains the port of Cassandra.
     */
    private final int port;
    /**
     * This is the name of the keyspace to use for Titan.
     */
    private final String keyspace;

    /**
     * This is the initial value constructor.
     * 
     * @param host
     *            is the host for Cassandra for Titan to connect to.
     * @param port
     *            is the port for Cassandra for Titan to connect to.
     * @param keyspace
     *            is
     */
    public TitanCassandraStore(String host, int port, String keyspace) {
	if ((host == null) || (host.isEmpty())) {
	    throw new IllegalArgumentException(
		    "The host must not be null or empty.");
	}
	this.host = host;
	if (port <= 0) {
	    this.port = DEFAULT_CASSANDRA_THRIFT_PORT;
	} else {
	    this.port = port;
	}
	if ((keyspace == null) || (keyspace.isEmpty())) {
	    this.keyspace = DEFAULT_TITAN_KEYSPACE;
	} else {
	    this.keyspace = keyspace;
	}
    }

    /**
     * Returns the host name of the Cassandra server.
     * 
     * @return A {@link String} with the host name is returned.
     */
    public String getHost() {
	return host;
    }

    /**
     * Returns the port of the Cassandra server.
     * 
     * @return An <code>int</code> is returned with the port.
     */
    public int getPort() {
	return port;
    }

    /**
     * Returns the currently used keyspace.
     * 
     * @return A {@link String} is returned with the name of the keyspace.
     */
    public String getKeyspace() {
	return keyspace;
    }

    /**
     * This method returns the {@link TitanGraph} object when database is
     * connected.
     * 
     * @return A {@link TitanGraph} is returned.
     */
    public final TitanGraph getTitanGraph() {
	return titanGraph;
    }

    @Override
    public DatastoreMetadataFactory<TitanVertexMetadata, String, TitanEdgeMetadata, String> getMetadataFactory() {
	return new TitanMetadataFactory();
    }

    @Override
    public void init(Collection<TypeMetadata> registeredMetadata) {
	logger.info("Initializing eXtended Objects for Titan on Cassandra...");
	Configuration configuration = new BaseConfiguration();
	configuration.setProperty("storage.backend", "cassandra");
	configuration.setProperty("storage.hostname", host);
	if (port > 0) {
	    configuration.setProperty("storage.port", port);
	}
	if (keyspace != null) {
	    configuration.setProperty("storage.keyspace", keyspace);
	}
	titanGraph = TitanFactory.open(configuration);
	checkAndInitializeDiscriminatorProperties(registeredMetadata);
	checkAndInitializePropertyIndizes(registeredMetadata);
    }

    private void checkAndInitializeDiscriminatorProperties(
	    Collection<TypeMetadata> registeredMetadata) {
	for (TypeMetadata metadata : registeredMetadata) {
	    AnnotatedType annotatedType = metadata.getAnnotatedType();
	    if (CompositeObject.class.equals(annotatedType
		    .getAnnotatedElement())) {
		continue;
	    }
	    Class<? extends Element> type;
	    String discriminatorName;
	    if (annotatedType.getAnnotation(VertexDefinition.class) != null) {
		type = Vertex.class;
		TitanVertexMetadata datastoreMetadata = (TitanVertexMetadata) ((EntityTypeMetadata<?>) metadata)
			.getDatastoreMetadata();
		discriminatorName = TitanStoreSession.XO_DISCRIMINATORS_PROPERTY
			+ datastoreMetadata.getDiscriminator();
	    } else if (annotatedType.getAnnotation(EdgeDefinition.class) != null) {
		type = Edge.class;
		TitanEdgeMetadata datastoreMetadata = (TitanEdgeMetadata) ((RelationTypeMetadata<?>) metadata)
			.getDatastoreMetadata();
		discriminatorName = TitanStoreSession.XO_DISCRIMINATORS_PROPERTY
			+ datastoreMetadata.getDiscriminator();
	    } else {
		continue;
	    }
	    logger.info("Discriminator '"
		    + discriminatorName
		    + "' is used in vertizes or edges. Check for presence of index...");
	    checkAndCreatePropertyIndex(discriminatorName, String.class, type,
		    false);
	}
    }

    private void checkAndInitializePropertyIndizes(
	    Collection<TypeMetadata> registeredMetadata) {
	for (TypeMetadata metadata : registeredMetadata) {
	    IndexedPropertyMethodMetadata<?> indexedProperty = metadata
		    .getIndexedProperty();
	    if (indexedProperty != null) {
		TitanIndexedPropertyMetadata datastoreMetadata = (TitanIndexedPropertyMetadata) indexedProperty
			.getDatastoreMetadata();
		String name = datastoreMetadata.getName();
		Class<?> dataType = datastoreMetadata.getDataType();
		Class<? extends Element> type = datastoreMetadata.getType();
		boolean unique = datastoreMetadata.isUnique();
		logger.info("Indexed property '" + name
			+ "' was found. Check for presence of index...");
		checkAndCreatePropertyIndex(name, dataType, type, unique);
	    }
	}
    }

    private void checkAndCreatePropertyIndex(String name, Class<?> dataType,
	    Class<? extends Element> type, boolean unique) {
	TitanType titanType = titanGraph.getType(name);
	if (titanType == null) {
	    logger.info("Create index for property (or discriminator) '" + name
		    + "'.");
	    createIndex(name, unique, dataType, type);
	} else if (titanType.isPropertyKey()) {
	    TitanKey titanKey = (TitanKey) titanType;
	    if (!titanKey.hasIndex(INDEX_NAME, type)) {
		logger.warn("Property (or discriminator) '"
			+ name
			+ "' shall be an indexed property, but this property is already in use and does not have an index. It is not possible to do anything about it. :-(");
	    }
	} else {
	    throw new XOException(
		    "Property '"
			    + name
			    + "' shall be an indexed property, but this name is already in use, but not as property.");
	}
    }

    private void createIndex(String name, boolean unique, Class<?> dataType,
	    Class<? extends Element> type) {
	logger.info("Create index for property '" + name + "'. (unique="
		+ unique + ";dataType=" + dataType.getName() + ";type="
		+ type.getName() + ")");
	KeyMaker makeKey = titanGraph.makeKey(name);
	makeKey.single();
	makeKey.dataType(dataType);
	makeKey.indexed(INDEX_NAME, type);
	if (unique) {
	    makeKey.unique();
	}
	makeKey.make();
    }

    @Override
    public TitanStoreSession createSession() {
	return new TitanStoreSession(titanGraph);
    }

    @Override
    public void close() {
	logger.info("Shutting down eXtended Objects for Titan on Cassandra...");
	titanGraph.shutdown();
	titanGraph = null;
    }
}
