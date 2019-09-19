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
package fr.paris.lutece.tools.sonarqube;

import org.junit.Test;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction.Type;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Param;
import org.sonar.api.server.rule.RulesDefinition.Repository;
import org.sonar.api.server.rule.RulesDefinition.Rule;

import static org.fest.assertions.Assertions.assertThat;

public class LuteceRulesDefinitionTest
{

    @Test
    public void test()
    {
        LuteceRulesDefinition rulesDefinition = new LuteceRulesDefinition();
        RulesDefinition.Context context = new RulesDefinition.Context();
        rulesDefinition.define( context );
        RulesDefinition.Repository repository = context.repository( LuteceRulesDefinition.REPOSITORY_KEY );

        assertThat( repository.name() ).isEqualTo( "Lutece Repository" );
        assertThat( repository.language() ).isEqualTo( "java" );
        assertThat( repository.rules() ).hasSize( RulesList.getChecks().size() );

        assertRuleProperties( repository );
        assertParameterProperties( repository );
        assertAllRuleParametersHaveDescription( repository );
    }

    private void assertParameterProperties( Repository repository )
    {
        // TooManyLinesInFunctionCheck
        Param max = repository.rule( "DeprecatedMacro" ).param( "name" );
        assertThat( max ).isNotNull();
        assertThat( max.defaultValue() ).isEqualTo( "Inject" );
        assertThat( max.description() ).isEqualTo( "Name of the annotation to avoid, without the prefix @, for instance 'Override'" );
        assertThat( max.type() ).isEqualTo( RuleParamType.STRING );
    }

    private void assertRuleProperties( Repository repository )
    {
        Rule rule = repository.rule( "DeprecatedMacro" );
        assertThat( rule ).isNotNull();
        assertThat( rule.name() ).isEqualTo( "Title of AvoidAnnotation" );
        assertThat( rule.debtRemediationFunction().type() ).isEqualTo( Type.CONSTANT_ISSUE );
        assertThat( rule.type() ).isEqualTo( RuleType.CODE_SMELL );
    }

    private void assertAllRuleParametersHaveDescription( Repository repository )
    {
        for( Rule rule : repository.rules() )
        {
            for( Param param : rule.params() )
            {
                assertThat( param.description() ).as( "description for " + param.key() ).isNotEmpty();
            }
        }
    }
}
