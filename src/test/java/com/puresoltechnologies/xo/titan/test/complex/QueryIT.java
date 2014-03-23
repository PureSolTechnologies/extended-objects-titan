package com.puresoltechnologies.xo.titan.test.complex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.Query;
import com.buschmais.cdo.api.Query.Result;
import com.buschmais.cdo.api.ResultIterable;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;
import com.puresoltechnologies.xo.titan.test.data.Person;

@RunWith(Parameterized.class)
public class QueryIT extends AbstractXOTitanTest {

	public QueryIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws IOException {
		return configuredCdoUnits();
	}

	@Before
	public void setupData() {
		addStarwarsData(getCdoManager());
	}

	@Test
	public void test() {
		CdoManager cdoManager = getCdoManager();
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
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();

		Query<Person> query = cdoManager.createQuery(
				"_().has('lastName', 'Skywalker').has('firstName','Luke')",
				Person.class);
		Result<Person> result = query.execute();
		Person lukeSkywalker = result.getSingleResult();
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
