package fr.paris.lutece.tools.sonarqube;

import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;

/**
 * Sonar way profile for the JSP language
 */
public final class FMTemplateQualityProfile implements BuiltInQualityProfilesDefinition {

  private static final String NAME = "Lutece way";

  @Override
  public void define(BuiltInQualityProfilesDefinition.Context context) {
    BuiltInQualityProfilesDefinition.NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(NAME, FMTemplate.LANGUAGE_KEY);
    profile.done();
  }

}
