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

/**
 * LutecePluginConstants
 */
public class LutecePluginConstants
{

    public static final String LUTECE_PROFILE = "Lutece way";
    public static final String LUTECE_JSON_PROFILE = "Lutece_way_profile.json";

    // Lutece Freemarker Templates Language
    public static final String FMT_REPOSITORY_KEY = "lutece-templates";
    public static final String FMT_REPOSITORY_NAME = "Lutece Repository";
    public static final String FMT_LANGUAGE_KEY = "lutece_ftl";
    public static final String FMT_LANGUAGE_NAME = "Lutece Freemarker Template";
    public static final String FMT_RESOURCE_PATH = "org/sonar/l10n/lutece/rules/freemarker";
    public static final String FMT_WAY_PROFILE_PATH = FMT_RESOURCE_PATH + "/" + LUTECE_JSON_PROFILE;
    
    public static final String XML_REPOSITORY_KEY = "lutece-xml";
    public static final String XML_REPOSITORY_NAME = "Lutece Repository";
    public static final String XML_LANGUAGE_KEY = "xml";
    public static final String XML_LANGUAGE_NAME = "XML";
    public static final String XML_RESOURCE_PATH = "org/sonar/l10n/xml/rules/xml";
    public static final String XML_WAY_PROFILE_PATH = XML_RESOURCE_PATH + "/" + LUTECE_JSON_PROFILE;


}
