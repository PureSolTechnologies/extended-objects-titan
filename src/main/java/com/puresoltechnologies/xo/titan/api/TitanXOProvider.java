package com.puresoltechnologies.xo.titan.api;

import java.net.URI;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.spi.bootstrap.XODatastoreProvider;
import com.buschmais.xo.spi.datastore.Datastore;
import com.puresoltechnologies.xo.titan.impl.TitanCassandraStore;
import com.puresoltechnologies.xo.titan.impl.TitanStoreSession;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanNodeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;

/**
 * This class implements the XO {@link CdoDatastoreProvider} for Titan database.
 * 
 * @author Rick-Rainer Ludwig
 */
public class TitanXOProvider
		implements
		XODatastoreProvider<TitanNodeMetadata, String, TitanRelationMetadata, String> {

	/**
	 * This constant contains {@value #TITAN_SCHEME_PREFIX} as prefix for all
	 * URI protocols referencing a Titan store provider.
	 */
	private static final String TITAN_SCHEME_PREFIX = "titan-";

	/**
	 * This constant contains {@value #TITAN_CASSANDRA_SCHEME} as protocol a
	 * Titan store provider on Cassandra.
	 */
	private static final String TITAN_CASSANDRA_SCHEME = TITAN_SCHEME_PREFIX
			+ "cassandra";

	@Override
	public Datastore<TitanStoreSession, TitanNodeMetadata, String, TitanRelationMetadata, String> createDatastore(
			XOUnit xoUnit) {
		if (xoUnit == null) {
			throw new IllegalArgumentException("CdoUnit must not be null!");
		}
		URI uri = xoUnit.getUri();
		if (uri == null) {
			throw new XOException("No URI is specified for the store.");
		}
		String scheme = uri.getScheme();
		if (!scheme.startsWith(scheme)) {
			throw new XOException("Only URIs starting with '"
					+ TITAN_SCHEME_PREFIX + "' are supported by this store.");
		}
		String host = uri.getHost();
		int port = uri.getPort();
		String keyspace = TitanCassandraStore.retrieveKeyspaceFromURI(uri);
		switch (scheme) {
		case TITAN_CASSANDRA_SCHEME:
			return new TitanCassandraStore(host, port, keyspace);
		default:
			throw new XOException("Scheme '" + scheme
					+ "' is not supported by this store.");
		}
	}
}
