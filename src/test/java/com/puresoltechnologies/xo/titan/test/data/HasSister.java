package com.puresoltechnologies.xo.titan.test.data;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeType;

@EdgeType("hasSister")
public interface HasSister {

	void setBrother(Person sister);

	Person getSister();

}
