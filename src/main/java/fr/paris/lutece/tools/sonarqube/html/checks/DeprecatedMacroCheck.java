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
import org.sonar.plugins.html.node.TextNode;

/**
 * DeprecatedMacroCheck
 */
@Rule(key = "DeprecatedMacroCheck")
public class DeprecatedMacroCheck extends AbstractPageCheck
{
    public static final String KEY = "DeprecatedMacroCheck";
    
    private static final String[] DEPRECATED_MACROS = {
        "comboSortedWithParams",
        "comboWithParams",
        "comboSiteWithParams",
        "comboSite",
        "combo",
        "comboSorted",
        "fieldTextArea",
        "fieldInputCombo",
        "fieldInputRadioBoxInline",
        "fieldInputCheckBoxInline",
        "fieldInputRadioBox",
        "fieldInputCheckBox",
        "fieldInputCalendar",
        "fieldStaticText",
        "fieldInputWrapper",
        "fieldInputPassword",
        "fieldInputText",
        "filterPanel",
        "dataTable ",
        "boxSized",
        "rowBox",
        "rowBoxHeader",
        "row_class",
        "coloredBg",
        "unstyledList",
        "dropdownList ",
    };
    
    

    @Override
    public void characters( TextNode textNode )
    {
        super.characters( textNode ); 
        for (String strMacroName : DEPRECATED_MACROS)
        {
            if( textNode.getCode().contains( "<@" + strMacroName + " "))
            {
                createViolation( textNode.getStartLinePosition() , "The Freemarker macro " + strMacroName + " is deprecated and will be removed in Lutece v7" );
            }
        }
    }

      
}
