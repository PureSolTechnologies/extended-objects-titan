package com.puresoltechnologies.xo.titan.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import com.buschmais.cdo.api.CdoException;

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

	@Test(expected = IllegalArgumentException.class)
	public void testNullHost() {
		new TitanCassandraStore(null, 123, "keyspace");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyHost() {
		new TitanCassandraStore("", 123, "keyspace");
	}

	@Test
	public void testNegativePort() {
		TitanCassandraStore store = new TitanCassandraStore("host", -1,
				"keyspace");
		assertThat(store.getPort(),
				is(TitanCassandraStore.DEFAULT_CASSANDRA_THRIFT_PORT));
	}

	@Test
	public void testZeroPort() {
		TitanCassandraStore store = new TitanCassandraStore("host", 0,
				"keyspace");
		assertThat(store.getPort(),
				is(TitanCassandraStore.DEFAULT_CASSANDRA_THRIFT_PORT));
	}

	@Test
	public void testNullKeyspace() {
		TitanCassandraStore store = new TitanCassandraStore("host", 123, null);
		assertThat(store.getKeyspace(),
				is(TitanCassandraStore.DEFAULT_TITAN_KEYSPACE));
	}

	@Test
	public void testEmptyKeyspace() {
		TitanCassandraStore store = new TitanCassandraStore("host", 123, "");
		assertThat(store.getKeyspace(),
				is(TitanCassandraStore.DEFAULT_TITAN_KEYSPACE));
	}
}
