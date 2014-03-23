package com.puresoltechnologies.xo.titan.test.gremlin;

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
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;
import com.puresoltechnologies.xo.titan.test.data.Person;

@RunWith(Parameterized.class)
public class GremlinIT extends AbstractXOTitanTest {

	public GremlinIT(CdoUnit cdoUnit) {
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
	public void findSkywalkerFamily() {
		CdoManager cdoManager = getCdoManager();
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
