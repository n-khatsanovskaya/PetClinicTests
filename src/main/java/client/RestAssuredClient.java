package client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;
import java.util.Optional;

public abstract class RestAssuredClient {
  String baseUrl;

  public RestAssuredClient(String baseUrl) {
    this.baseUrl = baseUrl;

    RestAssured.requestSpecification = new RequestSpecBuilder()
        .addFilter(new ResponseLoggingFilter())
        .build();

    RestAssured.responseSpecification = new ResponseSpecBuilder()
//        .expectResponseTime(lessThan(1000L))
        .build();

    RestAssured.filters(new AllureRestAssured());
  }

  public Response get(String path) {
    return get(path, null);
  }

  public Response get(String path, Map<String, String> params) {

    RequestSpecification httpRequest = RestAssured.given()
        .log()
        .all(true);
    Optional.ofNullable(params).ifPresent(value -> httpRequest.pathParams(value));
    httpRequest.baseUri(baseUrl);
    httpRequest.header("Content-Type", "application/json");
    return httpRequest.get(path);
  }

  public Response post(String path, Object body) {
    return post(path, null, body);
  }

  public Response post(String path, Map<String, String> params, Object body) {
    RequestSpecification httpRequest = RestAssured.given()
        .body(body)
        .log()
        .all(true);

    Optional.ofNullable(params).ifPresent(value -> httpRequest.pathParams(value));

    httpRequest.baseUri(baseUrl);
    httpRequest.header("Content-Type", "application/json");
    return httpRequest.post(path);
  }

  public Response put(String path, Map<String, String> params, Object body) {
    RequestSpecification httpRequest = RestAssured.given()
        .pathParams(params)
        .body(body)
        .log()
        .all(true);
    httpRequest.baseUri(baseUrl);
    httpRequest.header("Content-Type", "application/json");
    return httpRequest.put(path);
  }

  public Response delete(String path, Map<String, String> params) {
    RequestSpecification httpRequest = RestAssured.given()
        .pathParams(params)
        .log()
        .all(true);
    httpRequest.baseUri(baseUrl);
    return httpRequest.delete(path);
  }
}
