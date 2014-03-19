package com.puresoltechnologies.xo.titan.test.data;

import java.util.Set;

import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition
public interface Person {

	void setFirstName(String firstName);

	String getFirstName();

	void setLastName(String lastName);

	String getLastName();

	void setMother(Person mother);

	Person getMother();

	void setFather(Person father);

	Person getFather();

	Set<Person> getSisters();

	Set<Person> getBrothers();
}
