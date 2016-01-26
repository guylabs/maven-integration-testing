package org.apache.maven.its.mng5783.plugin;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Test mojo.
 *
 * @goal test
 */
public class TestMojo
    extends AbstractMojo
{
    /**
     * The build directory of the project.
     *
     * @parameter property="project.build.directory"
     */
    private File outputDirectory;

    /**
     * The plugin artifacts.
     *
     * @parameter property="plugin.artifacts"
     */
    private List<Artifact> artifacts;

    public void execute()
        throws MojoExecutionException
    {
        try
        {
            File file = new File( outputDirectory, "dependencies.txt" );
            file.getParentFile().mkdirs();
            BufferedWriter w = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file ), "UTF-8" ) );
            try
            {
                for ( Artifact artifact : artifacts )
                {
                    w.write( artifact.getId() );
                    w.newLine();
                }
            }
            finally
            {
                w.close();
            }
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }

    }
}
