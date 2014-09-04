package com.github.signed.maven.sanitizer;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true, snippets = SnippetType.UNDERSCORE, tags = {"@implementing"})
public class UnderDevelopment {
}
