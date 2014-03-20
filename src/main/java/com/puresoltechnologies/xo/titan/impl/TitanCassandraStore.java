package com.puresoltechnologies.xo.titan.impl;

import java.net.URI;
import java.util.Collection;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import com.buschmais.cdo.api.CdoException;
import com.buschmais.cdo.spi.datastore.Datastore;
import com.buschmais.cdo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.cdo.spi.metadata.type.TypeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanNodeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;
import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;

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
		Datastore<TitanStoreSession, TitanNodeMetadata, String, TitanRelationMetadata, String> {

	/**
	 * This constant contains the default name of the Titan keyspace which is
	 * set to {@value #DEFAULT_TITAN_KEYSPACE}.
	 */
	private static final String DEFAULT_TITAN_KEYSPACE = "titan";

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
			throw new CdoException(
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
		this.host = host;
		this.port = port;
		if ((keyspace == null) || (keyspace.isEmpty())) {
			// Titan will use its default keyspace.
			this.keyspace = DEFAULT_TITAN_KEYSPACE;
		} else {
			this.keyspace = keyspace;
		}
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
	public DatastoreMetadataFactory<TitanNodeMetadata, String, TitanRelationMetadata, String> getMetadataFactory() {
		return new TitanMetadataFactory();
	}

	@Override
	public void init(Collection<TypeMetadata> registeredMetadata) {
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
	}

	@Override
	public TitanStoreSession createSession() {
		return new TitanStoreSession(titanGraph);
	}

	@Override
	public void close() {
		titanGraph.shutdown();
		titanGraph = null;
	}
}
