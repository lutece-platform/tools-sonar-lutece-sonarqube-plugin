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
package fr.paris.lutece.tools.sonarqube.checks;

import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.Tree;

public class PrinterVisitor extends BaseTreeVisitor
{

    private static final int INDENT_SPACES = 2;

    private final StringBuilder sb;
    private int indentLevel;

    public PrinterVisitor()
    {
        sb = new StringBuilder();
        indentLevel = 0;
    }

    public static String print( Tree tree )
    {
        PrinterVisitor pv = new PrinterVisitor();
        pv.scan( tree );
        return pv.sb.toString();
    }

    private StringBuilder indent()
    {
        return sb.append( StringUtils.leftPad( "", INDENT_SPACES * indentLevel ) );
    }

    @Override
    protected void scan( List<? extends Tree> trees )
    {
        if( !trees.isEmpty() )
        {
            sb.deleteCharAt( sb.length() - 1 );
            sb.append( " : [\n" );
            super.scan( trees );
            indent().append( "]\n" );
        }
    }

    @Override
    protected void scan( @Nullable Tree tree )
    {
        if( tree != null && tree.getClass().getInterfaces() != null && tree.getClass().getInterfaces().length > 0 )
        {
            String nodeName = tree.getClass().getInterfaces()[0].getSimpleName();
            indent().append( nodeName ).append( "\n" );
        }
        indentLevel++;
        super.scan( tree );
        indentLevel--;
    }
}
