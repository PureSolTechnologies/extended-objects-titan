package com.puresoltechnologies.xo.titan.test.filesystem;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.Indexed;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition("file")
public interface File {

    @Indexed
    String getName();

    void setName(String name);

    @Incoming
    @ContainsFile
    Directory getParentDirectory();

}
