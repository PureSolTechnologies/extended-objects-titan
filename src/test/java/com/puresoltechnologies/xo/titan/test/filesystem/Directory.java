package com.puresoltechnologies.xo.titan.test.filesystem;

import java.util.List;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;
import com.puresoltechnologies.xo.titan.api.annotation.Indexed;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;

@VertexDefinition("directory")
public interface Directory {

    @Indexed
    String getName();

    void setName(String name);

    @Incoming
    @ContainsDirectory
    Directory getParentDirectory();

    @Outgoing
    @ContainsFile
    List<File> getFiles();

    @Outgoing
    @ContainsDirectory
    List<Directory> getDirectories();

}
