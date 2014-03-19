package com.puresoltechnologies.xo.titan.test.data;

import com.buschmais.cdo.api.CdoManager;

/**
 * This class provides some test data for testing.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class TestData {

	/**
	 * Adds the Starwars example data to the Titan database.
	 */
	public static void addStarwars(CdoManager cdoManager) {
		cdoManager.currentTransaction().begin();

		Person padmeSkywalker = cdoManager.create(Person.class);
		padmeSkywalker.setFirstName("Padme");
		padmeSkywalker.setLastName("Skywalker");

		Person anakinSkywalker = cdoManager.create(Person.class);
		anakinSkywalker.setFirstName("Anakin");
		anakinSkywalker.setLastName("Skywalker");

		Person leaSkywalker = cdoManager.create(Person.class);
		leaSkywalker.setFirstName("Lea");
		leaSkywalker.setLastName("Skywalker");
		leaSkywalker.setMother(padmeSkywalker);
		leaSkywalker.setFather(anakinSkywalker);

		Person lukeSkywalker = cdoManager.create(Person.class);
		lukeSkywalker.setFirstName("Luke");
		lukeSkywalker.setLastName("Skywalker");
		lukeSkywalker.setMother(padmeSkywalker);
		lukeSkywalker.setFather(anakinSkywalker);

		cdoManager.currentTransaction().commit();
	}
}
