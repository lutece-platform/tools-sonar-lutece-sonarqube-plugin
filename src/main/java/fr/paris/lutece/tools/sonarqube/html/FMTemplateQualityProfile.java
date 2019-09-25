package fr.paris.lutece.tools.sonarqube.html;

import fr.paris.lutece.tools.sonarqube.LutecePluginConstants;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

/**
 * Sonar way profile for the JSP language
 */
public final class FMTemplateQualityProfile implements BuiltInQualityProfilesDefinition
{


    @Override
    public void define( BuiltInQualityProfilesDefinition.Context context )
    {
        BuiltInQualityProfilesDefinition.NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile( LutecePluginConstants.LUTECE_PROFILE, LutecePluginConstants.LFMT_LANGUAGE_KEY );
        profile.done();
    }

}
