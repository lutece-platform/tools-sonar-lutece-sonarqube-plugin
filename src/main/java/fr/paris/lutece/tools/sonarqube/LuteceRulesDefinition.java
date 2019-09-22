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

import com.google.gson.Gson;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.plugins.html.api.HtmlConstants;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

/**
 * Declare rule metadata in server repository of rules. That allows to list the
 * rules in the page "Rules".
 */
public class LuteceRulesDefinition implements RulesDefinition
{
    // don't change that because the path is hard coded in CheckVerifier
    //private static final String RESOURCE_BASE_PATH = "/org/sonar/l10n/java/rules/squid";

    public static final String REPOSITORY_KEY = "lutece";
    private static final String REPOSITORY_NAME = "Lutece Repository";

    private final Gson gson = new Gson();

    private static final Set<String> TEMPLATE_RULE_KEYS = Collections.unmodifiableSet(Stream.of(
            "TemplateRuleCheck").collect(Collectors.toSet()));

    public static final String RESOURCE_BASE_PATH = "org/sonar/l10n/web/rules/Web";
    private static final String JSON_PROFILE = RESOURCE_BASE_PATH + "/Lutece_way_profile.json" ;

    @Override
    public void define(Context context)
    {
        NewRepository repository = context
                .createRepository(REPOSITORY_KEY, HtmlConstants.LANGUAGE_KEY)
                .setName(REPOSITORY_NAME);

        RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(RESOURCE_BASE_PATH, JSON_PROFILE );

        ruleMetadataLoader.addRulesByAnnotatedClass(repository, CheckClasses.getCheckClasses());

        for (NewRule rule : repository.rules())
        {
            if (TEMPLATE_RULE_KEYS.contains(rule.key()))
            {
                rule.setTemplate(true);
            }
        }

        repository.done();
    }

    /*
    @Override
    public void define( Context context )
    {
        NewRepository repository = context
                .createRepository( REPOSITORY_KEY, "lutece" )
                .setName( REPOSITORY_NAME );

        for( Class<? extends JavaCheck> check : RulesList.getChecks() )
        {
            new RulesDefinitionAnnotationLoader().load( repository, check );
            newRule( check, repository );
        }
        repository.done();
    }

    protected void newRule( Class<? extends JavaCheck> ruleClass, NewRepository repository )
    {

        org.sonar.check.Rule ruleAnnotation = AnnotationUtils.getAnnotation( ruleClass, org.sonar.check.Rule.class );
        if( ruleAnnotation == null )
        {
            throw new IllegalArgumentException( "No Rule annotation was found on " + ruleClass );
        }
        String ruleKey = ruleAnnotation.key();
        if( StringUtils.isEmpty( ruleKey ) )
        {
            throw new IllegalArgumentException( "No key is defined in Rule annotation of " + ruleClass );
        }
        NewRule rule = repository.rule( ruleKey );
        if( rule == null )
        {
            throw new IllegalStateException( "No rule was created for " + ruleClass + " in " + repository.key() );
        }
        ruleMetadata( rule );

        rule.setTemplate( AnnotationUtils.getAnnotation( ruleClass, RuleTemplate.class ) != null );
    }

    private void ruleMetadata( NewRule rule )
    {
        String metadataKey = rule.key();
        addHtmlDescription( rule, metadataKey );
        addMetadata( rule, metadataKey );
    }

    private void addMetadata( NewRule rule, String metadataKey )
    {
        URL resource = LuteceRulesDefinition.class.getResource( RESOURCE_BASE_PATH + "/" + metadataKey + "_java.json" );
        if( resource != null )
        {
            RuleMetatada metatada = gson.fromJson( readResource( resource ), RuleMetatada.class );
            rule.setSeverity( metatada.defaultSeverity.toUpperCase( Locale.US ) );
            rule.setName( metatada.title );
            rule.addTags( metatada.tags );
            rule.setType( RuleType.valueOf( metatada.type ) );
            rule.setStatus( RuleStatus.valueOf( metatada.status.toUpperCase( Locale.US ) ) );
            if( metatada.remediation != null )
            {
                rule.setDebtRemediationFunction( metatada.remediation.remediationFunction( rule.debtRemediationFunctions() ) );
                rule.setGapDescription( metatada.remediation.linearDesc );
            }
        }
    }

    private static void addHtmlDescription( NewRule rule, String metadataKey )
    {
        URL resource = LuteceRulesDefinition.class.getResource( RESOURCE_BASE_PATH + "/" + metadataKey + "_java.html" );
        if( resource != null )
        {
            rule.setHtmlDescription( readResource( resource ) );
        }
    }

    private static String readResource( URL resource )
    {
        try( BufferedReader reader = new BufferedReader( new InputStreamReader( resource.openStream() ) ) )
        {
            return reader.lines().collect( Collectors.joining( "\n" ) );
        }
        catch( Exception e )
        {
            throw new IllegalStateException( "Failed to read: " + resource, e );
        }
    }

    private static class RuleMetatada
    {

        String title;
        String status;
        @Nullable
        Remediation remediation;

        String type;
        String[] tags;
        String defaultSeverity;
    }

    private static class Remediation
    {

        String func;
        String constantCost;
        String linearDesc;
        String linearOffset;
        String linearFactor;

        public DebtRemediationFunction remediationFunction( DebtRemediationFunctions drf )
        {
            if( func.startsWith( "Constant" ) )
            {
                return drf.constantPerIssue( constantCost.replace( "mn", "min" ) );
            }
            if( "Linear".equals( func ) )
            {
                return drf.linear( linearFactor.replace( "mn", "min" ) );
            }
            return drf.linearWithOffset( linearFactor.replace( "mn", "min" ), linearOffset.replace( "mn", "min" ) );
        }
    }
     */
}
