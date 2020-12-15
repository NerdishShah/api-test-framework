package io.saal.stepdefs;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.jayway.jsonpath.JsonPath;

import ai.saal.config.ApplicationConfig;
import ai.saal.config.SpringConfiguration;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.saal.model.Data;

public class ColorValidationSteps {
	private ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringConfiguration.class);
	private ApplicationConfig appConfig = applicationContext.getBean(ApplicationConfig.class);
	private RequestSpecification requestSpec;
	private Response response;

	@Given("the test environment is configured")
	public void theTestEnvironmentIsConfigured() {
		RestAssured.baseURI = appConfig.getBaseUri();
		RestAssured.urlEncodingEnabled = true;
		requestSpec = RestAssured.given();
	}

	@When("I pass path params as")
	public void iPassPathParams(Map<String, String> pathParams) {
		System.out.println(pathParams);
		pathParams.forEach((k, v) -> {
			requestSpec = requestSpec.pathParam(k, v);
		});
	}

	@When("I perform GET operation {string}")
	public void iPerformGETOperation(String resource) {
		response = requestSpec.when().get(appConfig.getResource(resource));
		System.out.println(response);
		response.then().log().all();
	}

	@Then("I should get response status code as {int}")
	public void iShouldGetResponseStatusCodeAs(Integer code) {
		response.then().statusCode(code);
	}

	@Then("response content type should be in {string}")
	public void responseContentTypeShouldBeInFormat(String format) {
		if (format.equals("JSON")) {
			response.then().assertThat().contentType(JSON).and()
					.body(matchesJsonSchemaInClasspath("color-schema.json"));
		}
	}

	@Then("response body should match JSONPath expression {string}")
	public void responseBodyShouldMatchJSONPathExpression(String jPathExpression) {

		Map<String, String> jPathExpressionMap = Stream.of(jPathExpression.split(";")).map(i -> i.split(":"))
				.collect(Collectors.toMap(a -> a[0], a -> a[1]));

		SoftAssertions softly = new SoftAssertions();

		jPathExpressionMap.forEach((k, v) -> {
			Object actual = JsonPath.read(response.body().asString(), k);
			softly.assertThat(String.valueOf(actual)).contains(v);
		});
		softly.assertAll();

	}

	@DataTableType
	public Data dataEntry(Map<String, String> entry) {
		Data data = new Data();
		data.setId(Integer.valueOf(entry.get("id")));
		data.setName((String) entry.get("name"));
		data.setYear(Integer.valueOf(entry.get("year")));
		return data;
	}

	@Then("the response body should be same as")
	public void responseBodyContent(DataTable data) {
		Data expected = (Data) data.asList(Data.class).get(0);
		response.then().assertThat().body("data.id", equalTo(expected.getId()))
				.body("data.name", equalTo(expected.getName())).body("data.year", equalTo(expected.getYear()));
	}
}
