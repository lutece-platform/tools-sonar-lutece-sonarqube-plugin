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

import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;

import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.MethodTree;

/**
 * DeprecatedMacroRule
 */
@Rule(key = "DeprecatedMacro")
public class DeprecatedMacroRule extends BaseTreeVisitor implements JavaFileScanner
{

    private JavaFileScannerContext context;

    protected static final String COMPANY_NAME = "MyCompany";

    @Override
    public void scanFile( JavaFileScannerContext context )
    {
        this.context = context;

        // The call to the scan method on the root of the tree triggers the visit of the AST by this visitor
        scan( context.getTree() );

        // For debugging purpose, you can print out the entire AST of the analyzed file
        System.out.println( PrinterVisitor.print( context.getTree() ) );
    }

    /**
     * Overriding the visitor method to implement the logic of the rule.
     *
     * @param tree AST of the visited method.
     */
    @Override
    public void visitMethod( MethodTree tree )
    {

        if( tree.simpleName().name().toUpperCase().contains( COMPANY_NAME.toUpperCase() ) )
        {
            // Adds an issue by attaching it with the tree and the rule
            context.reportIssue( this, tree, "Avoid using Brand in method name" );
        }
        // The call to the super implementation allows to continue the visit of the AST.
        // Be careful to always call this method to visit every node of the tree.
        super.visitMethod( tree );

        // All the code located after the call to the overridden method is executed when leaving the node
    }
}
