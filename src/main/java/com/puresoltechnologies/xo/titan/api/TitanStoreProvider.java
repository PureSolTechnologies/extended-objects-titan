package com.puresoltechnologies.xo.titan.api;

import java.net.URI;

import com.buschmais.cdo.api.CdoException;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.buschmais.cdo.spi.bootstrap.CdoDatastoreProvider;
import com.buschmais.cdo.spi.datastore.Datastore;
import com.puresoltechnologies.xo.titan.impl.TitanStoreSession;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanNodeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;

public class TitanStoreProvider
		implements
		CdoDatastoreProvider<TitanNodeMetadata, String, TitanRelationMetadata, String> {

	private static final String TITAN_SCHEME_PREFIX = "titan-";
	private static final String TITAN_CASSANDRA_SCHEME = TITAN_SCHEME_PREFIX
			+ "cassandra";

	@Override
	public Datastore<TitanStoreSession, TitanNodeMetadata, String, TitanRelationMetadata, String> createDatastore(
			CdoUnit cdoUnit) {
		URI uri = cdoUnit.getUri();
		String scheme = uri.getScheme();
		if (!scheme.startsWith(scheme)) {
			throw new CdoException("Only URIs starting with '"
					+ TITAN_SCHEME_PREFIX + "' are supported by this store.");
		}
		String host = uri.getHost();
		int port = uri.getPort();
		String path = uri.getPath();
		switch (scheme) {
		case TITAN_CASSANDRA_SCHEME:
			return new TitanCassandraStore(host, port, path);
		default:
			throw new CdoException("Scheme '" + scheme
					+ "' is not supported by this store.");
		}
	}
}
