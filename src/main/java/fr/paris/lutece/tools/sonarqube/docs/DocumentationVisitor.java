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

import com.google.common.base.Preconditions;
import com.sonar.sslr.api.AstNode;

import java.lang.annotation.Annotation;
import javax.annotation.Nullable;

import org.sonar.api.utils.AnnotationUtils;
import org.sonar.squidbridge.annotations.SqaleLinearRemediation;
import org.sonar.squidbridge.annotations.SqaleLinearWithOffsetRemediation;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.CodeCheck;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.SquidCheck;

public class DocumentationVisitor extends SquidCheck
{

    public void addIssue( AstNode node, CodeCheck check, String message )
    {
        addIssue( node.getTokenLine(), check, message, null );
    }

    public void addIssue( AstNode node, CodeCheck check, String message, Double cost )
    {
        addIssue( node.getTokenLine(), check, message, cost );
    }

    public void addIssue( int line, CodeCheck check, String message )
    {
        addIssue( line, check, message, null );
    }

    public void addIssueOnFile( CodeCheck check, String message )
    {
        addIssue( -1, check, message, null );
    }

    public void addIssue( @Nullable Integer line, CodeCheck check, String message, @Nullable Double cost )
    {
        Preconditions.checkNotNull( check );
        Preconditions.checkNotNull( message );
        CheckMessage checkMessage = new CheckMessage( check, message );
        if( line > 0 )
        {
            checkMessage.setLine( line );
        }
        if( cost == null )
        {
            Annotation linear = AnnotationUtils.getAnnotation( check, SqaleLinearRemediation.class );
            Annotation linearWithOffset = AnnotationUtils.getAnnotation( check, SqaleLinearWithOffsetRemediation.class );
            if( linear != null || linearWithOffset != null )
            {
                throw new IllegalStateException( "A check annotated with a linear SQALE function should provide an effort to fix." );
            }
        }
        else
        {
            checkMessage.setCost( cost );
        }

        if( getContext().peekSourceCode() instanceof SourceFile )
        {
            getContext().peekSourceCode().log( checkMessage );
        }
        else if( getContext().peekSourceCode().getParent( SourceFile.class ) != null )
        {
            getContext().peekSourceCode().getParent( SourceFile.class ).log( checkMessage );
        }
        else
        {
            throw new IllegalStateException( "Unable to log a check message on source code '"
                    + ( getContext().peekSourceCode() == null ? "[NULL]" : getContext().peekSourceCode().getKey() ) + "'" );
        }
    }

}
