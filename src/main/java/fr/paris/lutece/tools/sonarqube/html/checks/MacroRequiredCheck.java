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
package fr.paris.lutece.tools.sonarqube.html.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.html.checks.AbstractPageCheck;
import org.sonar.plugins.html.node.Attribute;
import org.sonar.plugins.html.node.TagNode;
import org.sonar.squidbridge.annotations.ActivatedByDefault;

/**
 * MacroRequiredRule
 */
@Rule(key = MacroRequiredCheck.KEY )
@ActivatedByDefault
public class MacroRequiredCheck extends AbstractPageCheck
{

    public static final String KEY = "MacroRequiredCheck";

    private static final String[] MACROS_REQUIRED =
    {
        "table |@table",
        "i |@icon",
        "input |@input",
        "select |@select",
        "a |@aButton",
        "button |@button",
        "form |@tForm",
        "fieldset |@fieldset",
        "legend |@fieldste",
        "ul |@ul",
        "div row |@row",
        "div col-md- |@columns",
        "div box |@box",
        "div form-group |@formGroup",
        "span fa|@icon",
        "span glyphicon|@icon",
        "i fa|@icon",
        "i glyphicon|@icon",
        "span badge|@tag",
        "span label|@tag"
        
    };

    @Override
    public void startElement( TagNode element )
    {
        for( String strMacroConversion : MACROS_REQUIRED )
        {
            String[] params = strMacroConversion.split( "\\|" );
            String strElement = params[0].trim();
            String[] fulltag = strElement.split( " " );
            String strMacro = params[1];
            String strTag = fulltag[0];

            if( strTag.equalsIgnoreCase( element.getNodeName() ) )
            {
                if( fulltag.length > 1 )
                {
                    String strAttribute = fulltag[1];
                    for( Attribute attribute : element.getAttributes() )
                    {
                        if( attribute.getName().equals( "class" ) && attribute.getValue().contains( strAttribute ) )
                        {
                            createViolation( element, "Use Freemarker macro " + strMacro + " instead of HTML tag <" + strTag + " class=\"" + strAttribute + "\">" );
                        }
                    }
                }
                else
                {
                    createViolation( element, "Use Freemarker macro " + strMacro + " instead of HTML tag <" + strTag + ">" );
                }
            }
        }

    }
}
