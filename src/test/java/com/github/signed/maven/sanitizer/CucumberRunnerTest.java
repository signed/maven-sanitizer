package com.github.signed.maven.sanitizer;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true, snippets = SnippetType.CAMELCASE, tags = {"~@implementing"})
public class CucumberRunnerTest {
}
