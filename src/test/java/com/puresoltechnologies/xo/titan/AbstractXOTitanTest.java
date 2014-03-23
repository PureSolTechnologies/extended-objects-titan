package com.puresoltechnologies.xo.titan;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.CdoManagerFactory;
import com.buschmais.cdo.api.ConcurrencyMode;
import com.buschmais.cdo.api.Transaction;
import com.buschmais.cdo.api.ValidationMode;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.buschmais.cdo.impl.bootstrap.CdoUnitFactory;
import com.buschmais.cdo.spi.metadata.type.TypeMetadata;
import com.puresoltechnologies.xo.titan.api.TitanXOProvider;
import com.puresoltechnologies.xo.titan.impl.TitanCassandraStore;
import com.puresoltechnologies.xo.titan.test.data.TestData;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Vertex;

/**
 * This is an abstract parent class for XO-Titan tests.
 * 
 * @author Rick-Rainer Ludwig
 */
public abstract class AbstractXOTitanTest {

	private static final String CDO_CONFIGURATION_RESOURCE = "/META-INF/cdo.xml";

	/**
	 * This is the default local URI for testing.
	 */
	private static final URI DEFAULT_LOCAL_URI;
	static {
		try {
			DEFAULT_LOCAL_URI = new URI(
					"titan-cassandra://localhost:9160/titantest");
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private static void clearTitanKeyspace(CdoUnit cdoUnit) {
		Class<?> provider = cdoUnit.getProvider();
		if (TitanXOProvider.class.equals(provider)) {
			clearTitanKeyspace(cdoUnit.getUri());
		}
	}

	/**
	 * Clears the keyspaces assigned to the specified URI.
	 * 
	 * @param uri
	 *            is an {@link URI} pointing to the keyspace to be cleaned.
	 * @throws URISyntaxException
	 *             is thrown if the URI is found invalid.
	 */
	private static void clearTitanKeyspace(URI uri) {
		String host = uri.getHost();
		int port = Integer.valueOf(uri.getPort());
		String keyspace = TitanCassandraStore.retrieveKeyspaceFromURI(uri);
		TitanCassandraStore titanCassandraStore = new TitanCassandraStore(host,
				port, keyspace);
		titanCassandraStore.init(new HashSet<TypeMetadata>());
		TitanGraph titanGraph = titanCassandraStore.getTitanGraph();
		Iterable<Vertex> vertices = titanGraph.query().vertices();
		for (Vertex vertex : vertices) {
			vertex.remove();
		}
		titanGraph.commit();
	}

	protected static Collection<Object[]> configuredCdoUnits()
			throws IOException {
		List<Object[]> cdoUnits = new ArrayList<>();
		List<CdoUnit> readCdoUnits = CdoUnitFactory.getInstance().getCdoUnits(
				AbstractXOTitanTest.class
						.getResource(CDO_CONFIGURATION_RESOURCE));
		for (CdoUnit cdoUnit : readCdoUnits) {
			cdoUnits.add(new Object[] { cdoUnit });
		}
		return cdoUnits;
	}

	protected static Collection<Object[]> cdoUnits() {
		return cdoUnits(Arrays.asList(DEFAULT_LOCAL_URI),
				Collections.<Class<?>> emptyList(),
				Collections.<Class<?>> emptyList(), ValidationMode.AUTO,
				ConcurrencyMode.SINGLETHREADED,
				Transaction.TransactionAttribute.MANDATORY);
	}

	protected static Collection<Object[]> cdoUnits(Class<?>... types) {
		return cdoUnits(Arrays.asList(DEFAULT_LOCAL_URI), Arrays.asList(types),
				Collections.<Class<?>> emptyList(), ValidationMode.AUTO,
				ConcurrencyMode.SINGLETHREADED,
				Transaction.TransactionAttribute.MANDATORY);
	}

	protected static Collection<Object[]> cdoUnits(List<URI> uris,
			List<? extends Class<?>> types) {
		return cdoUnits(uris, types, Collections.<Class<?>> emptyList(),
				ValidationMode.AUTO, ConcurrencyMode.SINGLETHREADED,
				Transaction.TransactionAttribute.MANDATORY);
	}

	protected static Collection<Object[]> cdoUnits(
			List<? extends Class<?>> types,
			List<? extends Class<?>> instanceListeners,
			ValidationMode validationMode, ConcurrencyMode concurrencyMode,
			Transaction.TransactionAttribute transactionAttribute) {
		return cdoUnits(Arrays.asList(DEFAULT_LOCAL_URI), types,
				instanceListeners, validationMode, concurrencyMode,
				transactionAttribute);
	}

	protected static Collection<Object[]> cdoUnits(List<URI> uris,
			List<? extends Class<?>> types,
			List<? extends Class<?>> instanceListenerTypes,
			ValidationMode valiationMode, ConcurrencyMode concurrencyMode,
			Transaction.TransactionAttribute transactionAttribute) {
		List<Object[]> cdoUnits = new ArrayList<>(uris.size());
		for (URI uri : uris) {
			CdoUnit unit = new CdoUnit("default", "Default CDO unit", uri,
					TitanXOProvider.class, new HashSet<>(types),
					instanceListenerTypes, valiationMode, concurrencyMode,
					transactionAttribute, new Properties());
			cdoUnits.add(new Object[] { unit });
		}
		return cdoUnits;
	}

	/**
	 * This method adds the Starwars characters data into the Titan database for
	 * testing purposes.
	 * 
	 * @param cdoManager2
	 * 
	 * @param cdoManagerFactory
	 */
	protected static void addStarwarsData(CdoManager cdoManager) {
		TestData.addStarwars(cdoManager);
	}

	private CdoManagerFactory cdoManagerFactory;
	private CdoManager cdoManager;

	private final CdoUnit cdoUnit;

	public AbstractXOTitanTest(CdoUnit cdoUnit) {
		super();
		this.cdoUnit = cdoUnit;
	}

	@Before
	public final void setup() {
		cdoManagerFactory = com.buschmais.cdo.api.bootstrap.Cdo
				.createCdoManagerFactory("Titan");
		cdoManager = cdoManagerFactory.createCdoManager();
		clearTitanKeyspace(cdoUnit);
	}

	@After
	public final void destroy() {
		if (cdoManager != null) {
			cdoManager.close();
		}
		if (cdoManagerFactory != null) {
			cdoManagerFactory.close();
		}
	}

	public CdoManagerFactory getCdoManagerFactory() {
		return cdoManagerFactory;
	}

	public CdoManager getCdoManager() {
		return cdoManager;
	}

}
