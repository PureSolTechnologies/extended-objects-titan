package com.puresoltechnologies.xo.titan.test.mapping;

import java.util.List;

import com.buschmais.cdo.api.Query.Result;
import com.buschmais.cdo.api.annotation.ResultOf;
import com.buschmais.cdo.api.annotation.ResultOf.Parameter;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition;
import com.puresoltechnologies.xo.titan.api.annotation.Gremlin;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition("E")
public interface E {

	@EdgeDefinition("RELATED_TO")
	List<F> getRelatedTo();

	@ResultOf(query = ByValue.class, usingThisAs = "e")
	Result<ByValue> getResultByValueUsingExplicitQuery(
			@Parameter("value") String value);

	@ResultOf(usingThisAs = "e")
	Result<ByValue> getResultByValueUsingReturnType(
			@Parameter("value") String value);

	@ResultOf(query = ByValue.class, usingThisAs = "e")
	ByValue getByValueUsingExplicitQuery(@Parameter("value") String value);

	@ResultOf(usingThisAs = "e")
	ByValue getByValueUsingReturnType(@Parameter("value") String value);

	@ResultOf
	ByValueUsingImplicitThis getByValueUsingImplicitThis(
			@Parameter("value") String value);

	@ResultOf
	@Gremlin("_().has('_xo_discriminator_E').outE.has('label', 'RELATED_TO').V.has('_xo_discriminator_F').has('value', '{value}')")
	Result<F> getResultUsingCypher(@Parameter("value") String value);

	@ResultOf
	@Gremlin("_().has('_xo_discriminator_E').outE.has('label', 'RELATED_TO').V.has('_xo_discriminator_F').has('value', '{value}')")
	F getSingleResultUsingCypher(@Parameter("value") String value);

	List<E2F> getE2F();

	@Gremlin("_().has('_xo_discriminator_E').outE.has('label', 'RELATED_TO').V.has('_xo_discriminator_F').has('value', '{value}') where e={e}")
	interface ByValue {
		F getF();
	}

	@Gremlin("_().has('_xo_discriminator_E').outE.has('label', 'RELATED_TO').V.has('_xo_discriminator_F').has('value', '{value}') where e={this}")
	interface ByValueUsingImplicitThis {
		F getF();
	}
}
