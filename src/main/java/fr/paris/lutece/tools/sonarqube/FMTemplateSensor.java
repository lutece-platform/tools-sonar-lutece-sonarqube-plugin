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

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sonar.api.SonarProduct;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.html.analyzers.ComplexityVisitor;
import org.sonar.plugins.html.analyzers.PageCountLines;
import org.sonar.plugins.html.checks.AbstractPageCheck;
import org.sonar.plugins.html.checks.HtmlIssue;
import org.sonar.plugins.html.checks.PreciseHtmlIssue;
import org.sonar.plugins.html.core.HtmlTokensVisitor;
import org.sonar.plugins.html.lex.PageLexer;
import org.sonar.plugins.html.visitor.DefaultNodeVisitor;
import org.sonar.plugins.html.visitor.HtmlAstScanner;
import org.sonar.plugins.html.visitor.HtmlSourceCode;
import org.sonar.plugins.html.visitor.NoSonarScanner;

public final class FMTemplateSensor implements Sensor
{

    private static final Logger LOG = Loggers.get(FMTemplateSensor.class);

    private final NoSonarFilter noSonarFilter;
    private final Checks<Object> checks;
    private final FileLinesContextFactory fileLinesContextFactory;

    public FMTemplateSensor(NoSonarFilter noSonarFilter, FileLinesContextFactory fileLinesContextFactory, CheckFactory checkFactory)
    {
        this.noSonarFilter = noSonarFilter;
        this.checks = checkFactory.create(LuteceRulesDefinition.REPOSITORY_KEY).addAnnotatedChecks((Iterable) CheckClasses.getCheckClasses());
        this.fileLinesContextFactory = fileLinesContextFactory;
    }

    @Override
    public void describe(SensorDescriptor descriptor)
    {
        descriptor
                .name( FMTemplate.LANGUAGE_NAME)
                .onlyOnFileType(InputFile.Type.MAIN);
    }

    @Override
    public void execute(SensorContext sensorContext)
    {

        FileSystem fileSystem = sensorContext.fileSystem();

        // configure page scanner and the visitors
        final HtmlAstScanner scanner = setupScanner(sensorContext);

        FilePredicates predicates = fileSystem.predicates();
        Iterable<InputFile> inputFiles = fileSystem.inputFiles(
                predicates.and(
                        predicates.hasType(InputFile.Type.MAIN),
                        predicates.hasLanguages(FMTemplate.LANGUAGE_KEY)
                ));

        for (InputFile inputFile : inputFiles)
        {
            if (sensorContext.isCancelled())
            {
                return;
            }

            HtmlSourceCode sourceCode = new HtmlSourceCode(inputFile);

            try (Reader reader = new InputStreamReader(inputFile.inputStream(), inputFile.charset()))
            {
                PageLexer lexer = new PageLexer();
                scanner.scan(lexer.parse(reader), sourceCode);
                saveMetrics(sensorContext, sourceCode);
                saveLineLevelMeasures(inputFile, sourceCode);

            }
            catch (Exception e)
            {
                LOG.error("Cannot analyze file " + inputFile, e);
                sensorContext.newAnalysisError()
                        .onFile(inputFile)
                        .message(e.getMessage())
                        .save();
            }
        }
    }

    private static void saveMetrics(SensorContext context, HtmlSourceCode sourceCode)
    {
        InputFile inputFile = sourceCode.inputFile();

        for (Map.Entry<Metric<Integer>, Integer> entry : sourceCode.getMeasures().entrySet())
        {
            context.<Integer>newMeasure()
                    .on(inputFile)
                    .forMetric(entry.getKey())
                    .withValue(entry.getValue())
                    .save();
        }

        for (HtmlIssue issue : sourceCode.getIssues())
        {
            NewIssue newIssue = context.newIssue()
                    .forRule(issue.ruleKey())
                    .gap(issue.cost());
            NewIssueLocation location = locationForIssue(inputFile, issue, newIssue);
            newIssue.at(location);
            newIssue.save();
        }
    }

    private static NewIssueLocation locationForIssue(InputFile inputFile, HtmlIssue issue, NewIssue newIssue)
    {
        NewIssueLocation location = newIssue.newLocation()
                .on(inputFile)
                .message(issue.message());
        Integer line = issue.line();
        if (issue instanceof PreciseHtmlIssue)
        {
            PreciseHtmlIssue preciseHtmlIssue = (PreciseHtmlIssue) issue;
            location.at(inputFile.newRange(issue.line(),
                    preciseHtmlIssue.startColumn(),
                    preciseHtmlIssue.endLine(),
                    preciseHtmlIssue.endColumn()));
        }
        else if (line != null)
        {
            location.at(inputFile.selectLine(line));
        }
        return location;
    }

    private void saveLineLevelMeasures(InputFile inputFile, HtmlSourceCode htmlSourceCode)
    {
        FileLinesContext fileLinesContext = fileLinesContextFactory.createFor(inputFile);

        for (Integer line : htmlSourceCode.getDetailedLinesOfCode())
        {
            fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, 1);
        }

        fileLinesContext.save();
    }

    /**
     * Create PageScanner with Visitors.
     */
    private HtmlAstScanner setupScanner(SensorContext context)
    {
        List<DefaultNodeVisitor> visitors = new ArrayList<>();
        if (context.runtime().getProduct() != SonarProduct.SONARLINT)
        {
            visitors.add(new HtmlTokensVisitor(context));
        }
        visitors.add(new PageCountLines());
        visitors.add(new ComplexityVisitor());
        visitors.add(new NoSonarScanner(noSonarFilter));
        HtmlAstScanner scanner = new HtmlAstScanner(visitors);

        for (Object check : checks.all())
        {
            ((AbstractPageCheck) check).setRuleKey(checks.ruleKey(check));
            scanner.addVisitor((AbstractPageCheck) check);
        }
        return scanner;
    }

}
