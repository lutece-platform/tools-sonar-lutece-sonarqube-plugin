/*
 * Copyright (c) 2002-2019, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.tools.sonarqube.docs;

import fr.paris.lutece.tools.sonarqube.docs.checks.ReadmeFilePresentCheck;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import org.sonar.java.checks.CheckList;

/**
 * DocumentationChecks
 */
public class DocumentationChecks
{
    private final SensorContext context;

    private static final Logger LOGGER = LoggerFactory.getLogger( DocumentationChecks.class );

    public DocumentationChecks( SensorContext context )
    {
        this.context = context;
    }

    public void reportProjectIssues()
    {
        if( context.fileSystem().baseDir() != null )
        {
            checkReadmeFilePresent( context.fileSystem().baseDir() );
        }
    }

    private void checkReadmeFilePresent( File fileRoot )
    {
        boolean readmeFileFound = false;
        for( File file : fileRoot.listFiles() )
        {
            if( file.isFile() && ( "README.md".equals( file.getName() ) ) )
            {
                readmeFileFound = true;
                break;
            }
        }

        if( !readmeFileFound )
        {
            addIssue( ReadmeFilePresentCheck.RULE_KEY, "\"README.md\" file is missing" );
        }

    }

    protected void addIssue( String ruleKey, String message )
    {
        LOGGER.info( "Adding issue: " + ruleKey + " " + message );
        NewIssue newIssue = context.newIssue().forRule( RuleKey.of( CheckList.REPOSITORY_KEY, ruleKey ) );

        newIssue
                .at( newLocation( newIssue, message ) ).save();

    }

    private static NewIssueLocation  newLocation( NewIssue issue, String message )
    {
        return issue.newLocation().message( message );
    }

}
