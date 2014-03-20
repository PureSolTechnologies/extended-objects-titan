package com.puresoltechnologies.xo.titan.test;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.buschmais.cdo.api.CdoException;
import com.puresoltechnologies.xo.titan.impl.TitanCassandraStore;

public class TitanCassandraStoreTest {

	@Test
	public void testRetrieveKeyspace() throws URISyntaxException {
		String keyspace = TitanCassandraStore.retrieveKeyspaceFromURI(new URI(
				"protocol://host:1234/keyspace"));
		assertEquals("keyspace", keyspace);

		keyspace = TitanCassandraStore.retrieveKeyspaceFromURI(new URI(
				"protocol://host:1234/keyspace//"));
		assertEquals("keyspace", keyspace);
	}

	@Test
	public void testRetrieveEmptyKeyspace() throws URISyntaxException {
		String keyspace = TitanCassandraStore.retrieveKeyspaceFromURI(new URI(
				"protocol://host:1234"));
		assertEquals("", keyspace);

		keyspace = TitanCassandraStore.retrieveKeyspaceFromURI(new URI(
				"protocol://host:1234//"));
		assertEquals("", keyspace);
	}

	@Test(expected = CdoException.class)
	public void testRetrieveMultipleKeyspace() throws URISyntaxException {
		TitanCassandraStore.retrieveKeyspaceFromURI(new URI(
				"protocol://host:1234/multi/path/"));
	}

}
