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

import com.buschmais.xo.api.ConcurrencyMode;
import com.buschmais.xo.api.Transaction;
import com.buschmais.xo.api.ValidationMode;
import com.buschmais.xo.api.XOManager;
import com.buschmais.xo.api.XOManagerFactory;
import com.buschmais.xo.api.bootstrap.XO;
import com.buschmais.xo.api.bootstrap.XOUnit;
import com.buschmais.xo.impl.bootstrap.XOUnitFactory;
import com.buschmais.xo.spi.metadata.type.TypeMetadata;
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

	private static final String XO_CONFIGURATION_RESOURCE = "/META-INF/xo.xml";

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

	private static void clearTitanKeyspace(XOUnit xoUnit) {
		Class<?> provider = xoUnit.getProvider();
		if (TitanXOProvider.class.equals(provider)) {
			clearTitanKeyspace(xoUnit.getUri());
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

	protected static Collection<Object[]> configuredXOUnits()
			throws IOException {
		List<Object[]> xoUnits = new ArrayList<>();
		List<XOUnit> readXOUnits = XOUnitFactory.getInstance().getXOUnits(
				AbstractXOTitanTest.class
						.getResource(XO_CONFIGURATION_RESOURCE));
		for (XOUnit xoUnit : readXOUnits) {
			xoUnits.add(new Object[] { xoUnit });
		}
		return xoUnits;
	}

	protected static Collection<Object[]> xoUnits() {
		return xoUnits(Arrays.asList(DEFAULT_LOCAL_URI),
				Collections.<Class<?>> emptyList(),
				Collections.<Class<?>> emptyList(), ValidationMode.AUTO,
				ConcurrencyMode.SINGLETHREADED,
				Transaction.TransactionAttribute.MANDATORY);
	}

	protected static Collection<Object[]> xoUnits(Class<?>... types) {
		return xoUnits(Arrays.asList(DEFAULT_LOCAL_URI), Arrays.asList(types),
				Collections.<Class<?>> emptyList(), ValidationMode.AUTO,
				ConcurrencyMode.SINGLETHREADED,
				Transaction.TransactionAttribute.MANDATORY);
	}

	protected static Collection<Object[]> xoUnits(List<URI> uris,
			List<? extends Class<?>> types) {
		return xoUnits(uris, types, Collections.<Class<?>> emptyList(),
				ValidationMode.AUTO, ConcurrencyMode.SINGLETHREADED,
				Transaction.TransactionAttribute.MANDATORY);
	}

	protected static Collection<Object[]> xoUnits(
			List<? extends Class<?>> types,
			List<? extends Class<?>> instanceListeners,
			ValidationMode validationMode, ConcurrencyMode concurrencyMode,
			Transaction.TransactionAttribute transactionAttribute) {
		return xoUnits(Arrays.asList(DEFAULT_LOCAL_URI), types,
				instanceListeners, validationMode, concurrencyMode,
				transactionAttribute);
	}

	protected static Collection<Object[]> xoUnits(List<URI> uris,
			List<? extends Class<?>> types,
			List<? extends Class<?>> instanceListenerTypes,
			ValidationMode valiationMode, ConcurrencyMode concurrencyMode,
			Transaction.TransactionAttribute transactionAttribute) {
		List<Object[]> xoUnits = new ArrayList<>(uris.size());
		for (URI uri : uris) {
			XOUnit xoUnit = new XOUnit("default", "Default XO unit", uri,
					TitanXOProvider.class, new HashSet<>(types),
					instanceListenerTypes, valiationMode, concurrencyMode,
					transactionAttribute, new Properties());
			xoUnits.add(new Object[] { xoUnit });
		}
		return xoUnits;
	}

	/**
	 * This method adds the Starwars characters data into the Titan database for
	 * testing purposes.
	 * 
	 * @param xoManager
	 * 
	 * @param xoManagerFactory
	 */
	protected static void addStarwarsData(XOManager xoManager) {
		TestData.addStarwars(xoManager);
	}

	private XOManagerFactory xoManagerFactory;
	private XOManager xoManager;

	private final XOUnit xoUnit;

	public AbstractXOTitanTest(XOUnit xoUnit) {
		super();
		this.xoUnit = xoUnit;
	}

	@Before
	public final void setup() {
		xoManagerFactory = XO.createXOManagerFactory(xoUnit);
		xoManager = xoManagerFactory.createXOManager();
		clearTitanKeyspace(xoUnit);
	}

	@After
	public final void destroy() {
		if (xoManager != null) {
			xoManager.close();
		}
		if (xoManagerFactory != null) {
			xoManagerFactory.close();
		}
	}

	public XOManagerFactory getXOManagerFactory() {
		return xoManagerFactory;
	}

	public XOManager getXOManager() {
		return xoManager;
	}

}
