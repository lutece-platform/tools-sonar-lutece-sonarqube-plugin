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
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

/**
 * Declare rule metadata in server repository of rules. That allows to list the
 * rules in the page "Rules".
 */
public class LuteceRulesDefinition implements RulesDefinition
{

    private static final Set<String> TEMPLATE_RULE_KEYS = Collections.unmodifiableSet(Stream.of(
            "TemplateRuleCheck").collect(Collectors.toSet()));

    @Override
    public void define(Context context)
    {
        NewRepository repository = context
                .createRepository(LutecePluginConstants.REPOSITORY_KEY, LutecePluginConstants.LFMT_LANGUAGE_KEY )
                .setName(LutecePluginConstants.REPOSITORY_NAME);

        RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader( LutecePluginConstants.LUTECE_RESOURCE_BASE_PATH, LutecePluginConstants.LUTECE_WAY_PROFILE_PATH );

//        ruleMetadataLoader.addRulesByAnnotatedClass(repository, HtmlCheckClasses.getCheckClasses());
        ruleMetadataLoader.addRulesByRuleKey( repository, HtmlCheckClasses.getRuleKeys() );

        for (NewRule rule : repository.rules())
        {
            if (TEMPLATE_RULE_KEYS.contains(rule.key()))
            {
                rule.setTemplate(true);
            }
        }

        repository.done();
    }


}
