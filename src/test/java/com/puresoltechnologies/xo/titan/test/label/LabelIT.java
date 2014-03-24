package com.puresoltechnologies.xo.titan.test.label;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.Query;
import com.buschmais.cdo.api.Query.Result.CompositeRowObject;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;

@RunWith(Parameterized.class)
public class LabelIT extends AbstractXOTitanTest {

	public LabelIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws URISyntaxException {
		return cdoUnits(ImplicitLabel.class, ExplicitLabel.class);
	}

	@Test
	public void implicitLabel() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		cdoManager.create(ImplicitLabel.class);
		Query<CompositeRowObject> query = cdoManager.createQuery("_().map");
		CompositeRowObject result = query.execute().getSingleResult();
		assertThat(result.get("_xo_discriminator_ImplicitLabel", String.class),
				is("ImplicitLabel"));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void explicitLabel() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		cdoManager.create(ExplicitLabel.class);
		Query<CompositeRowObject> query = cdoManager.createQuery("_().map");
		CompositeRowObject result = query.execute().getSingleResult();
		assertThat(
				result.get("_xo_discriminator_EXPLICIT_LABEL", String.class),
				is("EXPLICIT_LABEL"));
		cdoManager.currentTransaction().commit();
	}
}
