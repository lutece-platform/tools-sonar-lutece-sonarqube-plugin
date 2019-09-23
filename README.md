# SonarQube plugin for Lutece Rules

This project provides a [SonarQube](https://www.sonarqube.org) plugin to check Lutece specific rules.

HTML checks
- Deprecated macros (macro that has been removed in Lutece v7)
- Required usage of macro instead of HTML tags

## Installation

Build the project with Maven

```
mvn build
```

Copy the jar into SonarQube/extensions/plugins/ directory


