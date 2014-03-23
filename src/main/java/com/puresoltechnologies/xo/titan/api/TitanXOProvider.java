package com.puresoltechnologies.xo.titan.api;

import java.net.URI;

import com.buschmais.cdo.api.CdoException;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.buschmais.cdo.spi.bootstrap.CdoDatastoreProvider;
import com.buschmais.cdo.spi.datastore.Datastore;
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
		CdoDatastoreProvider<TitanNodeMetadata, String, TitanRelationMetadata, String> {

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
			CdoUnit cdoUnit) {
		if (cdoUnit == null) {
			throw new IllegalArgumentException("CdoUnit must not be null!");
		}
		URI uri = cdoUnit.getUri();
		if (uri == null) {
			throw new CdoException("No URI is specified for the store.");
		}
		String scheme = uri.getScheme();
		if (!scheme.startsWith(scheme)) {
			throw new CdoException("Only URIs starting with '"
					+ TITAN_SCHEME_PREFIX + "' are supported by this store.");
		}
		String host = uri.getHost();
		int port = uri.getPort();
		String keyspace = TitanCassandraStore.retrieveKeyspaceFromURI(uri);
		switch (scheme) {
		case TITAN_CASSANDRA_SCHEME:
			return new TitanCassandraStore(host, port, keyspace);
		default:
			throw new CdoException("Scheme '" + scheme
					+ "' is not supported by this store.");
		}
	}
}
