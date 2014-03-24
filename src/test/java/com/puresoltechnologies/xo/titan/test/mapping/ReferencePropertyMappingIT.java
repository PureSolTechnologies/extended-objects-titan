package com.puresoltechnologies.xo.titan.test.mapping;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.Query;
import com.buschmais.cdo.api.Query.Result.CompositeRowObject;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;

@Ignore("Not fully implemented, yet.")
@RunWith(Parameterized.class)
public class ReferencePropertyMappingIT extends AbstractXOTitanTest {

	public ReferencePropertyMappingIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws URISyntaxException {
		return cdoUnits(A.class, B.class);
	}

	@Test
	public void referenceProperty() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		B b1 = cdoManager.create(B.class);
		B b2 = cdoManager.create(B.class);
		a.setB(b1);
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		assertThat(a.getB(), equalTo(b1));
		a.setB(b2);
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		assertThat(a.getB(), equalTo(b2));
		a.setB(null);
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		assertThat(a.getB(), equalTo(null));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void mappedReferenceProperty() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		B b = cdoManager.create(B.class);
		a.setMappedB(b);
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		Query<CompositeRowObject> query = cdoManager
				.createQuery("_().has('_xo_discriminator_A').outE('MAPPED_B').V.map");
		CompositeRowObject result = query.execute().getSingleResult();
		// TestResult result =
		// executeQuery("match (a:A)-[:MAPPED_B]->(b) return b");
		assertThat(result.get("_xo_discriminator_B", String.class), is("B"));
		cdoManager.currentTransaction().commit();
	}

}
