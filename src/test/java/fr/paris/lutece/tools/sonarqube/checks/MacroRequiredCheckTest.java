/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.paris.lutece.tools.sonarqube.checks;

import java.io.File;
import org.junit.Rule;
import org.junit.Test;
import org.sonar.plugins.html.checks.HtmlIssue;
import org.sonar.plugins.html.checks.style.InlineStyleCheck;
import org.sonar.plugins.html.visitor.HtmlSourceCode;

/**
 *
 * @author pierre
 */
public class MacroRequiredCheckTest
{

    @Rule
    public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

    @Test
    public void test() throws Exception
    {
        HtmlSourceCode sourceCode = TestHelper.scan(new File("src/test/files/macro_required.html"), new MacroRequiredCheck());

        /*    checkMessagesVerifier.verify(sourceCode.getIssues())
      .next().atLocation(1, 6, 1, 13).withMessage("Use CSS classes instead.");
         */
        
        for( HtmlIssue issue : sourceCode.getIssues())
        {
            System.out.println( "line : " + issue.line() + " : " + issue.message() ); 
        }
    }

}
