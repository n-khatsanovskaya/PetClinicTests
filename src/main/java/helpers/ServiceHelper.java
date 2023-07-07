package helpers;

import client.RestAssuredClient;
import config.PetClinicConfig;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import models.request.VisitsRequest;
import models.response.VisitsResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


public class ServiceHelper extends RestAssuredClient {
  public ServiceHelper() {
    super(PetClinicConfig.BASE_URL);
  }

  @Step("Get visits")
  public List<VisitsResponse> getVisits() {
    Response response = get(PetClinicConfig.VISITS);
    response.then().statusCode(200);
    return response.as(new TypeRef<>() {});
  }

  @Step("Get visit by id")
  public VisitsResponse getVisitById(String id) {
    Response response = get(PetClinicConfig.VISITS_ID, Map.of("visitId", id));
    try {
      VisitsResponse visit =  response.as(VisitsResponse.class);
      response.then().statusCode(200);
      return visit;
    }
    catch (IllegalStateException e) {
      return null;
    }
  }

  @Step("Get visit by id failed response")
  public Response getVisitByIdFailedResponse(String id, Integer code) {
    Response response = get(PetClinicConfig.VISITS_ID, Map.of("visitId", id));
    response.then().statusCode(code);
    return response;
  }

  @Step("Add visit for pet")
  public VisitsResponse addVisit(VisitsRequest body) {
    Response response = post(PetClinicConfig.VISITS, body);
    response.then().statusCode(200);
    return response.as(VisitsResponse.class);
  }

  @Step("Add visit unsuccessfull")
  public Response addVisitUnsuccessfull(VisitsRequest body) {
    Response response = post(PetClinicConfig.VISITS, body);
    response.then().statusCode(400);
    return response;
  }

  @Step("Add visit for pet")
  public VisitsResponse addPetVisit(String petId, String ownerId, VisitsRequest body) {
    Response response = post(PetClinicConfig.OWNERS_PETS_VISITS, Map.of("petId", petId, "ownerId", ownerId), body);
    response.then().statusCode(201);
    return response.as(VisitsResponse.class);
  }

  @Step("Add visit for pet with default parameters")
  public VisitsResponse addPetVisit() {
    VisitsRequest body = new VisitsRequest(
        String.valueOf(LocalDate.now()),
        "new visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
        null,
        null);
    Response response = post(PetClinicConfig.OWNERS_PETS_VISITS, Map.of("petId", "1", "ownerId", "1"), body);
    response.then().statusCode(201);
    return response.as(VisitsResponse.class);
  }

  @Step("Add visit for pet unsuccessful")
  public Response addPetVisitUnsuccessfull(String petId, String ownerId, VisitsRequest body, Integer status) {
    Response response = post(PetClinicConfig.OWNERS_PETS_VISITS, Map.of("petId", petId, "ownerId", ownerId), body);
    response.then().statusCode(status);
    return response;
  }

  @Step("Update visit")
  public VisitsResponse updateVisit(String id, VisitsRequest body) {
    Response response = put(PetClinicConfig.VISITS_ID, Map.of("visitId", id), body);
    response.then().statusCode(201);
    VisitsResponse visit = response.as(VisitsResponse.class);
    return visit;
  }

  @Step("Update visit")
  public Response updateVisitResponse(String id, VisitsRequest body, Integer status) {
    Response response = put(PetClinicConfig.VISITS_ID, Map.of("visitId", id), body);
    response.then().statusCode(status);//should be 200
    return response;
  }

  @Step("Delete visit")
  public VisitsResponse deleteVisit(String id) {
    return deleteVisit(id, 204);
  }

  public VisitsResponse deleteVisit(String id, Integer code) {
    try {
      Response response = delete(PetClinicConfig.VISITS_ID, Map.of("visitId", id));
      response.then().statusCode(code);
      VisitsResponse visit = response.as(VisitsResponse.class);
      return visit;
    }
    catch (IllegalStateException e) {
      return null;
    }
  }
}
