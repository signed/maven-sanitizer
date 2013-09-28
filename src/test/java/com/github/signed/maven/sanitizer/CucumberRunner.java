package com.github.signed.maven.sanitizer;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true, snippets = SnippetType.CAMELCASE)
public class CucumberRunner {
}
