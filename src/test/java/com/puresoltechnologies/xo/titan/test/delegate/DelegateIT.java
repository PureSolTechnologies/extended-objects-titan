package com.puresoltechnologies.xo.titan.test.delegate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import org.hamcrest.collection.IsMapContaining;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.CompositeObject;
import com.buschmais.cdo.api.Query;
import com.buschmais.cdo.api.Query.Result;
import com.buschmais.cdo.api.Query.Result.CompositeRowObject;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;
import com.puresoltechnologies.xo.titan.impl.TitanStoreSession;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

@RunWith(Parameterized.class)
public class DelegateIT extends AbstractXOTitanTest {

	public DelegateIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws URISyntaxException {
		return cdoUnits(A.class, B.class, A2B.class);
	}

	@Test
	public void entity() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		Vertex node = ((CompositeObject) cdoManager.create(A.class))
				.getDelegate();
		assertThat(
				node.<String> getProperty(TitanStoreSession.XO_DISCRIMINATORS_PROPERTY
						+ "A"), equalTo("A"));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void relation() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		B b = cdoManager.create(B.class);
		cdoManager.create(a, A2B.class, b);
		Query<A2B> query = cdoManager.createQuery("_().has('"
				+ TitanStoreSession.XO_DISCRIMINATORS_PROPERTY + "A').outE",
				A2B.class);
		Result<A2B> result = query.execute();
		A2B a2b = result.getSingleResult();
		CompositeObject composite = (CompositeObject) a2b;
		Edge edge = composite.getDelegate();
		assertThat(edge.getLabel(), equalTo("RELATION"));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void row() {
		CdoManager cdoManager = getCdoManager();

		cdoManager.currentTransaction().begin();
		cdoManager.create(A.class);
		Query<CompositeRowObject> query = cdoManager.createQuery("_().has('"
				+ TitanStoreSession.XO_DISCRIMINATORS_PROPERTY + "A').map");
		Result<CompositeRowObject> row = query.execute();
		Map<String, Object> delegate = row.getSingleResult().getDelegate();
		assertThat(delegate, IsMapContaining.<String, Object> hasEntry(
				TitanStoreSession.XO_DISCRIMINATORS_PROPERTY + "A", "A"));
		cdoManager.currentTransaction().commit();
	}
}
