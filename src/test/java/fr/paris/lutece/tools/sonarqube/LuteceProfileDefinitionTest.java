/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.utils.Version;

/**
 * LuteceProfileDefinitionTest
 */
public class LuteceProfileDefinitionTest
{

    @Test
    public void luteceProfile()
    {
        SonarRuntime sonarRuntime = SonarRuntimeImpl.forSonarQube(Version.create(7, 3), SonarQubeSide.SERVER);
        ValidationMessages validation = ValidationMessages.create();
        BuiltInQualityProfilesDefinition.Context context = new BuiltInQualityProfilesDefinition.Context();

        BuiltInQualityProfilesDefinition profileDef = new LuteceProfilesDefinition(sonarRuntime);
        profileDef.define(context);
        BuiltInQualityProfilesDefinition.BuiltInQualityProfile profile = context.profile( "lutece_ftl", "Lutece way");
        assertThat(profile.language()).isEqualTo( "lutece_ftl");
        List<BuiltInQualityProfilesDefinition.BuiltInActiveRule> activeRules = profile.rules();
        assertThat(activeRules.size()).as("Expected number of rules in profile").isGreaterThanOrEqualTo(2);
        assertThat(profile.name()).isEqualTo("Lutece way");
        Set<String> keys = new HashSet<>();
        for (BuiltInQualityProfilesDefinition.BuiltInActiveRule activeRule : activeRules)
        {
            keys.add(activeRule.ruleKey());
        }
        //Check that we store active rule with legacy keys, not RSPEC keys
        assertThat(keys).contains("MacroRequiredCheck");
        assertThat(keys).contains("DeprecatedMacroCheck");
        assertThat(validation.hasErrors()).isFalse();

        // Check that we use severity from the read rule and not default one.
        assertThat(activeRules.get(0).overriddenSeverity()).isNull();
    }
}
