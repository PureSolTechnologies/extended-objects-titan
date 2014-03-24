package com.puresoltechnologies.xo.titan.test.mapping;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.Query.Result;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;

@Ignore("Not fully implemented, yet.")
@RunWith(Parameterized.class)
public class RelationResultOfIT extends AbstractXOTitanTest {

	private E e;
	private F f1;
	private F f2;

	private E2F e2f1;
	private E2F e2f2;

	public RelationResultOfIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws URISyntaxException {
		return cdoUnits(E.class, F.class, E2F.class);
	}

	@Before
	public void createData() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		e = cdoManager.create(E.class);
		f1 = cdoManager.create(F.class);
		e2f1 = cdoManager.create(e, E2F.class, f1);
		e2f1.setValue("E2F1");
		f2 = cdoManager.create(F.class);
		e2f2 = cdoManager.create(e, E2F.class, f2);
		e2f2.setValue("E2F2");
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void resultUsingExplicitQuery() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		Result<E2F.ByValue> byValue = e2f1
				.getResultByValueUsingExplicitQuery("E2F1");
		assertThat(byValue.getSingleResult().getF(), equalTo(f1));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void resultUsingReturnType() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		Result<E2F.ByValue> byValue = e2f1
				.getResultByValueUsingReturnType("E2F1");
		assertThat(byValue.getSingleResult().getF(), equalTo(f1));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void byValueUsingExplicitQuery() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		E2F.ByValue byValue = e2f1.getByValueUsingExplicitQuery("E2F1");
		assertThat(byValue.getF(), equalTo(f1));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void byValueUsingReturnType() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		E2F.ByValue byValue = e2f1.getByValueUsingReturnType("E2F1");
		assertThat(byValue.getF(), equalTo(f1));
		byValue = e2f1.getByValueUsingReturnType("unknownE2F");
		assertThat(byValue, equalTo(null));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void byValueUsingImplicitThis() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		E2F.ByValueUsingImplicitThis byValue = e2f1
				.getByValueUsingImplicitThis("E2F1");
		assertThat(byValue.getF(), equalTo(f1));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void resultUsingGremlin() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		Result<F> result = e2f1.getResultUsingGremlin("E2F1");
		assertThat(result, hasItems(equalTo(f1)));
		result = e2f1.getResultUsingGremlin("unknownF");
		assertThat(result.iterator().hasNext(), equalTo(false));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void singleResultUsingGremlin() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		F result = e2f1.getSingleResultUsingGremlin("E2F1");
		assertThat(result, equalTo(f1));
		result = e2f1.getSingleResultUsingGremlin("unknownF");
		assertThat(result, equalTo(null));
		cdoManager.currentTransaction().commit();
	}

}
