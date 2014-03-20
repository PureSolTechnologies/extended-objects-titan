package com.puresoltechnologies.xo.titan.test.gremlin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.CdoManagerFactory;
import com.buschmais.cdo.api.Query;
import com.buschmais.cdo.api.Query.Result;
import com.buschmais.cdo.api.bootstrap.Cdo;
import com.puresoltechnologies.xo.titan.test.AbstractXOTitanTest;
import com.puresoltechnologies.xo.titan.test.data.Person;

public class GremlinIT extends AbstractXOTitanTest {

	private static CdoManagerFactory cdoManagerFactory;
	private CdoManager cdoManager;

	@BeforeClass
	public static void initialize() {
		cdoManagerFactory = Cdo.createCdoManagerFactory("Titan");
		addStarwarsData(cdoManagerFactory);
	}

	@AfterClass
	public static void teardown() {
		if (cdoManagerFactory != null) {
			cdoManagerFactory.close();
		}
	}

	@Before
	public void setup() {
		cdoManager = cdoManagerFactory.createCdoManager();
	}

	@After
	public void destroy() {
		cdoManager.close();
	}

	@Test
	public void findSkywalkerFamily() {
		cdoManager.currentTransaction().begin();

		Query<Person> query = cdoManager.createQuery(
				"_().has('lastName', 'Skywalker')", Person.class);
		assertNotNull(query);

		Result<Person> results = query.execute();
		assertNotNull(results);

		int count = 0;
		for (Person person : results) {
			count++;
			assertEquals("Skywalker", person.getLastName());
		}
		assertEquals(4, count);

		cdoManager.currentTransaction().commit();
	}
}
