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
public class PrimitivePropertyMappingIT extends AbstractXOTitanTest {

	public PrimitivePropertyMappingIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws URISyntaxException {
		return cdoUnits(A.class);
	}

	@Test
	public void primitiveProperty() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		a.setString("value");
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		assertThat(a.getString(), equalTo("value"));
		a.setString("updatedValue");
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		assertThat(a.getString(), equalTo("updatedValue"));
		a.setString(null);
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		assertThat(a.getString(), equalTo(null));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void mappedPrimitiveProperty() {
		CdoManager cdoManager = getCdoManager();

		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		a.setMappedString("mappedValue");
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		Query<CompositeRowObject> query = cdoManager
				.createQuery("_().has('_xo_discriminator_A').map");
		CompositeRowObject result = query.execute().getSingleResult();
		// TestResult result =
		// executeQuery("match (a:A) return a.MAPPED_STRING as v");
		assertThat(result.get("MAPPED_STRING", String.class), is("mappedValue"));
		cdoManager.currentTransaction().commit();
	}
}
