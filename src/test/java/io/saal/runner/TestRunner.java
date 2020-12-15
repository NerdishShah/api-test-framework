package io.saal.runner;

import static io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = "io.saal.stepdefs", dryRun = false, strict = true, monochrome = true, snippets = CAMELCASE, tags = {
		"@getColor" }, plugin = { "pretty", "html:target/cucumber-html-report",
				"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
				"usage:target/cucumber-usage.json", "json:target/cucumber-report-json.json" })
public class TestRunner {

}
