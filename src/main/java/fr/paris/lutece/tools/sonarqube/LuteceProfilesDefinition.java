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

import fr.paris.lutece.tools.sonarqube.html.FMTCheckClasses;
import org.sonar.api.SonarRuntime;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonarsource.analyzer.commons.BuiltInQualityProfileJsonLoader;

/**
 * LuteceProfilesDefinition
 */
public class LuteceProfilesDefinition implements BuiltInQualityProfilesDefinition
{

    private final SonarRuntime sonarRuntime;

    public LuteceProfilesDefinition( SonarRuntime sonarRuntime )
    {
        this.sonarRuntime = sonarRuntime;
    }

    @Override
    public void define( Context context )
    {
        createProfile(context, LutecePluginConstants.LUTECE_PROFILE, LutecePluginConstants.FMT_LANGUAGE_KEY,
               LutecePluginConstants.FMT_REPOSITORY_KEY, LutecePluginConstants.FMT_WAY_PROFILE_PATH , LutecePluginConstants.FMT_RESOURCE_PATH);

        createProfile(context, LutecePluginConstants.LUTECE_PROFILE, LutecePluginConstants.XML_LANGUAGE_KEY,
               LutecePluginConstants.XML_REPOSITORY_KEY, LutecePluginConstants.XML_WAY_PROFILE_PATH , LutecePluginConstants.XML_RESOURCE_PATH);
    }
    
    private void createProfile( Context context, String name, String language, String repositoryKey , String profilePath , String resourcePath )
    {
        NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile( name , language );
        BuiltInQualityProfileJsonLoader.load( profile, repositoryKey , profilePath , resourcePath, sonarRuntime );
        profile.done();
        
    }

}
