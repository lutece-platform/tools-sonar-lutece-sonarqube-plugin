/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.tools.sonarqube.checks;

import org.sonar.check.Rule;
import org.sonar.plugins.html.checks.AbstractPageCheck;
import org.sonar.plugins.html.node.TagNode;

/**
 * MacroRequiredRule
 */
@Rule(key = "MacroRequiredCheck")

public class MacroRequiredCheck extends AbstractPageCheck
{

    private static final String[] MACROS_REQUIRED =
    {
        "table |@table",
        "i |@icon",
        "input |@input",
        "select |@select",
        "a |@aButton",
        "button |@button",
        "form |@tForm",
        "ul |@ul",
        "div class=\"row\"|@row",
        "div class=\"col-|@columns",
        "div class=\"box |@box",
        "div class=\"form-group |@formGroup"
    };

    @Override
    public void startElement(TagNode element)
    {
        for (String strMacroConversion : MACROS_REQUIRED)
        {
            String[] params = strMacroConversion.split("\\|");
            String strTag = params[0].trim();
            String strMacro = params[1];
            if (strTag.equalsIgnoreCase(element.getNodeName()))
            {
                createViolation(element, "Use Freemarker macro " + strMacro + " instead of tag " + strTag );
            }
        }

    }

}
