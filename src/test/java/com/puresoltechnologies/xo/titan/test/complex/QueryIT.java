package com.puresoltechnologies.xo.titan.test.complex;

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
import com.buschmais.cdo.api.ResultIterable;
import com.buschmais.cdo.api.bootstrap.Cdo;
import com.puresoltechnologies.xo.titan.test.AbstractXOTitanTest;
import com.puresoltechnologies.xo.titan.test.data.Person;

public class QueryIT extends AbstractXOTitanTest {

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
	public void test() {
		cdoManager.currentTransaction().begin();
		ResultIterable<Person> people = cdoManager.find(Person.class, "Test");
		assertNotNull(people);

		int count = 0;
		for (Person person : people) {
			count++;
		}
		assertEquals(4, count);

		cdoManager.currentTransaction().commit();
	}

	@Test
	public void testRelations() {
		cdoManager.currentTransaction().begin();

		Query<Person> query = cdoManager.createQuery(
				"_().has('lastName', 'Skywalker').has('firstName','Luke')",
				Person.class);
		Person lukeSkywalker = query.execute().getSingleResult();
		assertEquals("Luke", lukeSkywalker.getFirstName());
		assertEquals("Skywalker", lukeSkywalker.getLastName());

		Person anakinSkywalker = lukeSkywalker.getFather();
		assertNotNull(anakinSkywalker);
		assertEquals("Anakin", anakinSkywalker.getFirstName());
		assertEquals("Skywalker", anakinSkywalker.getLastName());

		Person leaSkywalker = lukeSkywalker.getMother();
		assertNotNull(leaSkywalker);
		assertEquals("Padme", leaSkywalker.getFirstName());
		assertEquals("Skywalker", leaSkywalker.getLastName());

		cdoManager.currentTransaction().commit();
	}

}
