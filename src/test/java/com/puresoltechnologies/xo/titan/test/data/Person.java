package com.puresoltechnologies.xo.titan.test.data;

import java.util.Set;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;
import com.puresoltechnologies.xo.titan.api.annotation.Indexed;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition
public interface Person {

	void setFirstName(String firstName);

	String getFirstName();

	void setLastName(String lastName);

	@Indexed
	String getLastName();

	void setMother(Person mother);

	@Outgoing
	Person getMother();

	void setFather(Person father);

	@Outgoing
	Person getFather();

	@Outgoing
	Set<Person> getSisters();

	@Outgoing
	Set<Person> getBrothers();
}
