
$SONARQUBE_HOME/bin/linux-x86-64/sonar.sh stop
echo "deploying plugin's jar..."
cp target/*.jar $SONARQUBE_HOME/extensions/plugins/
echo "deployed plugin's jar."
$SONARQUBE_HOME/bin/linux-x86-64/sonar.sh start
