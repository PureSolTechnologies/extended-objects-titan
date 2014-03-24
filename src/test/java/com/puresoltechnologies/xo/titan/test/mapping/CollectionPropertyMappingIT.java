package com.puresoltechnologies.xo.titan.test.mapping;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.Query;
import com.buschmais.cdo.api.Query.Result.CompositeRowObject;
import com.buschmais.cdo.api.ResultIterator;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;

@Ignore("Not fully implemented, yet.")
@RunWith(Parameterized.class)
public class CollectionPropertyMappingIT extends AbstractXOTitanTest {

	public CollectionPropertyMappingIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws URISyntaxException {
		return cdoUnits(A.class, B.class);
	}

	@Test
	public void setProperty() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		B b = cdoManager.create(B.class);
		Set<B> setOfB = a.getSetOfB();
		assertThat(setOfB.add(b), equalTo(true));
		assertThat(setOfB.add(b), equalTo(false));
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		assertThat(setOfB.size(), equalTo(1));
		assertThat(setOfB.remove(b), equalTo(true));
		assertThat(setOfB.remove(b), equalTo(false));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void mappedSetProperty() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		B b = cdoManager.create(B.class);
		a.getMappedSetOfB().add(b);
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		Query<CompositeRowObject> query = cdoManager
				.createQuery("_().has('_xo_discriminator_A').outE.label('MAPPED_SET_OF_B').V.map");
		CompositeRowObject result = query.execute().getSingleResult();
		// TestResult result =
		// executeQuery("match (a:A)-[:MAPPED_SET_OF_B]->(b) return b");
		assertThat(result.get("_xo_discriminator_b", String.class), is("b"));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void listProperty() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		B b = cdoManager.create(B.class);
		List<B> listOfB = a.getListOfB();
		assertThat(listOfB.add(b), equalTo(true));
		assertThat(listOfB.add(b), equalTo(true));
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		assertThat(listOfB.size(), equalTo(2));
		assertThat(listOfB.remove(b), equalTo(true));
		assertThat(listOfB.remove(b), equalTo(true));
		assertThat(listOfB.remove(b), equalTo(false));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void mappedListProperty() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		B b = cdoManager.create(B.class);
		a.getMappedListOfB().add(b);
		a.getMappedListOfB().add(b);
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		Query<CompositeRowObject> query = cdoManager
				.createQuery("_().has('_xo_discriminator_A').outE.label('MAPPED_SET_OF_B').V.map");
		ResultIterator<CompositeRowObject> result = query.execute().iterator();
		assertTrue(result.hasNext());
		CompositeRowObject result1 = result.next();
		assertTrue(result.hasNext());
		CompositeRowObject result2 = result.next();
		assertFalse(result.hasNext());
		// TestResult result =
		// executeQuery("match (a:A)-[:MAPPED_LIST_OF_B]->(b) return b");
		assertThat(result1.get("_xo_discriminator_b", String.class), is("b"));
		assertThat(result2.get("_xo_discriminator_b", String.class), is("b"));
		cdoManager.currentTransaction().commit();
	}
}
