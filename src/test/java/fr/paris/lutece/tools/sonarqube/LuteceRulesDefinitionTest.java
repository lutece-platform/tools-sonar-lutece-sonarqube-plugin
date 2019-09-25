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

import fr.paris.lutece.tools.sonarqube.html.HtmlCheckClasses;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.fest.assertions.Assertions.assertThat;
import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;


public class LuteceRulesDefinitionTest
{

    @Test
    public void test()
    {
        LuteceRulesDefinition rulesDefinition = new LuteceRulesDefinition();
        RulesDefinition.Context context = new RulesDefinition.Context();
        rulesDefinition.define(context);
        RulesDefinition.Repository repository = context.repository( LutecePluginConstants.REPOSITORY_KEY);

        assertThat(repository.name()).isEqualTo("Lutece Repository");
        assertThat(repository.language()).isEqualTo( LutecePluginConstants.LFMT_LANGUAGE_KEY );

        assertThat(repository.rules()).hasSize(HtmlCheckClasses.getCheckClasses().size());

        RulesDefinition.Rule alertUseRule = repository.rule("MacroRequiredCheck");
        assertThat(alertUseRule).isNotNull();
//        assertThat(alertUseRule.name()).isEqualTo("Track uses of disallowed attributes");

        Set<String> templateRules = repository.rules().stream()
                .filter(RulesDefinition.Rule::template)
                .map(RulesDefinition.Rule::key)
                .collect(Collectors.toSet());
        assertThat(templateRules).hasSize(0);

        for (RulesDefinition.Rule rule : repository.rules())
        {
            for (RulesDefinition.Param param : rule.params())
            {
                assertThat(param.description()).as("description for " + param.key()).isNotEmpty();
            }
        }

        List<RulesDefinition.Rule> activated = repository.rules().stream().filter(RulesDefinition.Rule::activatedByDefault).collect(Collectors.toList());
        assertThat(activated).isNotEmpty();
        assertThat(activated.size()).isLessThanOrEqualTo(HtmlCheckClasses.getCheckClasses().size());
    }

}
