package com.puresoltechnologies.xo.titan.test;

import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.BeforeClass;

import com.buschmais.cdo.impl.schema.v1.Cdo;
import com.buschmais.cdo.impl.schema.v1.CdoUnitType;
import com.buschmais.cdo.spi.metadata.type.TypeMetadata;
import com.puresoltechnologies.xo.titan.api.TitanXOProvider;
import com.puresoltechnologies.xo.titan.impl.TitanCassandraStore;
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
	 * This method cleans all titan keyspaces for test.
	 * 
	 * @throws JAXBException
	 *             is thrown in case the {@value #CDO_CONFIGURATION_RESOURCE}
	 *             file cannot be read for configuration.
	 * @throws URISyntaxException
	 *             is thrown in case the URI in
	 *             {@value #CDO_CONFIGURATION_RESOURCE} is invalid.
	 */
	@BeforeClass
	public static void deleteTitanKeyspace() throws JAXBException,
			URISyntaxException {
		Cdo cdoConfiguration = readCdoXml();
		clearTitanKeyspaces(cdoConfiguration);
	}

	/**
	 * This method reads the configuration {@value #CDO_CONFIGURATION_RESOURCE}.
	 * 
	 * @return A {@link Cdo} object is returned containing the configuration.
	 * @throws JAXBException
	 *             is thrown if the configuration cannot be loaded.
	 */
	private static Cdo readCdoXml() throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Cdo.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		URL cdoResource = AbstractXOTitanTest.class
				.getResource(CDO_CONFIGURATION_RESOURCE);
		assertNotNull("Could not find cdo.xml at '"
				+ CDO_CONFIGURATION_RESOURCE + "'.", cdoResource);
		Cdo cdoConfiguration = (Cdo) unmarshaller.unmarshal(cdoResource);
		return cdoConfiguration;
	}

	/**
	 * This method clears all keyspaces which are assigned to a
	 * {@link TitanXOProvider}.
	 * 
	 * @param cdoConfiguration
	 *            is the configuration read from
	 *            {@value #CDO_CONFIGURATION_RESOURCE}.
	 * @throws URISyntaxException
	 *             is throw if the URI in the configuration is found invalid.
	 */
	private static void clearTitanKeyspaces(Cdo cdoConfiguration)
			throws URISyntaxException {
		for (CdoUnitType cdoUnit : cdoConfiguration.getCdoUnit()) {
			String provider = cdoUnit.getProvider();
			if (TitanXOProvider.class.getName().equals(provider)) {
				URI uri = new URI(cdoUnit.getUrl());
				clearTitanKeyspace(uri);
			}
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
	private static void clearTitanKeyspace(URI uri) throws URISyntaxException {
		String host = uri.getHost();
		int port = Integer.valueOf(uri.getPort());
		String path = uri.getPath();
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		TitanCassandraStore titanCassandraStore = new TitanCassandraStore(host,
				port, path);
		titanCassandraStore.init(new HashSet<TypeMetadata>());
		TitanGraph titanGraph = titanCassandraStore.getTitanGraph();
		Iterable<Vertex> vertices = titanGraph.query().vertices();
		for (Vertex vertex : vertices) {
			vertex.remove();
		}
		titanGraph.commit();
	}

}
