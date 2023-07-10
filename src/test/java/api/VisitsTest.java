package api;

import helpers.ServiceHelper;
import io.restassured.response.Response;
import models.request.VisitsRequest;
import models.response.VisitsResponse;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VisitsTest {
  ServiceHelper serviceHelper = new ServiceHelper();

  @Test(description = "TC6 Get the list of all visits")
  public void getVisitsTest() {
    List<VisitsResponse> visits = serviceHelper.getVisits();
    Assertions.assertThat(visits).as("Visits response").isNotEmpty();
  }

  @DataProvider
  public Object[][] visitData() {
    return new Object[][] {
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "new visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            1,
            1)}
    };
  }

  @Test(description = "TC7 Create a visit", dataProvider = "visitData")
  public void createVisitTest(VisitsRequest body) {
    VisitsResponse visit = serviceHelper.addVisit(body);
    Assertions.assertThat(visit.convertToMap()).as("Pet id").containsAllEntriesOf(body.convertToMap());
  }

  @DataProvider
  public Object[][] visitDataMissingRequiredFields() {
    return new Object[][] {
        //TC8 Create a visit: missing required field id
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "new visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            null,
            1), "must not be null"},
        //TC8 Create a visit: missing required field description
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            null,
            7,
            1), "must not be null"}
    };
  }

  @Test(description = "TC8 Create a visit: missing required filed", dataProvider = "visitDataMissingRequiredFields")
  public void createVisitUnsuccessfullTest(VisitsRequest body, String error) {
    Response response = serviceHelper.addVisitUnsuccessfull(body);
    //According to spec should check the response body
    //response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ErrorResponse.json"));

    Assertions.assertThat(response.header("errors")
        .contains(error))
        .as("Header").isTrue();
  }

  @DataProvider
  public Object[][] petVisitData() {
    return new Object[][] {
        //TC1 Add a vet visit: all parameters have valid values
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "new visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            null,
            null), "1"},
        //TC5 Add a vet visit: missing optional field date
        {new VisitsRequest(
            null,
            "new visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            null,
            null), "1"}
    };
  }

  @Test(description = "TC1 TC5 Add a vet visit", dataProvider = "petVisitData")
  public void createPetVisitTest(VisitsRequest body, String petId) {
    //TODO: check null data doesn't affect the DB
    VisitsResponse visit = serviceHelper.addPetVisit(petId, "1", body);
    Assertions.assertThat(visit.convertToMap()).as("Response body").containsAllEntriesOf(body.convertToMap());
  }

  @Test(description = "TC19 Delete visit", dataProvider = "petVisitData")
  public void deleteVisitTest (VisitsRequest body, String petId) {

    VisitsResponse visit = serviceHelper.addPetVisit(petId, "1", body);

    String visitId = visit.getId().toString();
    visit = serviceHelper.deleteVisit(visitId);
    Assertions.assertThat(visit).as("Response body").isNotNull();

    serviceHelper.getVisitByIdFailedResponse(visitId, 404);
  }

  @Test(description = "TC20 Delete a visit by ID: not existing visit id")
  public void deleteVisitUnsuccessfullTest() {
    serviceHelper.deleteVisit("100", 400);
  }

  @DataProvider
  public Object[][] petVisitDataUnsuccessfull() {
    return new Object[][] {
        //TC2 Add a vet visit: not existing pet
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "new visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            null,
            null), "100", 404, "TBD"},
        //TC4 Adds a vet visit: missing required field description
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            null,
            null,
            null), "1", 400, "TBD"}
    };
  }

  @Test(description = "TC2 TC4 Add a vet visit: unsuccessfull", dataProvider = "petVisitDataUnsuccessfull")
  public void createPetVisitUnsuccessfullTest(VisitsRequest body, String petId, Integer status, String error) {
    Response response = serviceHelper.addPetVisitUnsuccessfull(petId, "1", body, status);
    //According to spec should check the response body
    //response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ErrorResponse.json"));
    //TODO: check error message
    Assertions.assertThat(response.getBody().toString()
            .contains(error))
        .as("Response error message").isTrue();
    }

  @Test(description = "TC10 Get visit by id")
  public void getVisitByIdTest() {
    String id = "1";
    VisitsResponse visit = serviceHelper.getVisitById(id);
    Assertions.assertThat(visit.getId().toString()).isEqualTo(id);
  }

  @DataProvider
  public Object[][] visitIdsFailed() {
    return new Object[][] {
        //TC11 Get a visit by ID: invalid parameter value
        //TC12 Get a visit by ID: not existing visit id
        {"700", 404}, {"string", 400}
    };
  }

  @Test(description = "TC11 TC12 Get visit by id unsuccessfull", dataProvider = "visitIdsFailed")
  public void getVisitByIdUnsuccessfulTest(String id, Integer code) {
    Response response = serviceHelper.getVisitByIdFailedResponse(id, code);
    //According to spec should check the response body
    //response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ErrorResponse.json"));
    //TODO: check error message
    Assertions.assertThat(response.getBody().toString()
            .contains("error"))
        .as("Response error message").isTrue();
  }

  @DataProvider
  public Object[][] visitDataUpdate() {
    return new Object[][] {
        //TC13 Update a visit by ID
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "updated visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            1,
            10)},
          //TC17 Update a visit by ID: missing optional field date
        {new VisitsRequest(
            null,
            "updated visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            1,
            10)
      }
    };
  }

  @Test(description = "TC13 TC17 Update a visit by ID", dataProvider = "visitDataUpdate")
  public void updateVisitTest(VisitsRequest body) {
    VisitsResponse visit = serviceHelper.getVisitById(body.getId().toString());

    Map<String, String> result = visit.convertToMap().entrySet().stream()
        .filter(entry -> !body.convertToMap().containsKey(entry.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    serviceHelper.updateVisitResponse(body.getId().toString(), body, 204);
    visit = serviceHelper.getVisitById(body.getId().toString());

    SoftAssertions softAssertions = new SoftAssertions();

    softAssertions.assertThat(visit.convertToMap()).as("Updated visit data").containsExactlyInAnyOrderEntriesOf(body.convertToMap());

    if (!result.isEmpty())
            softAssertions.assertThat(visit.convertToMap()).as("Not affected visit data").containsAllEntriesOf(result);

    softAssertions.assertAll();
  }

  @DataProvider
  public Object[][] visitDataUpdateUnsuccessfull() {
    return new Object[][] {
        //TC15 Update a visit by ID: not existing pet id
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "updated visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            1,
            100),
          404}, //Not clear from documentation
        //TC16 Update a visit by ID: missing required field description
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            null,
            1,
            1),
          400},
        //TC16 Update a visit by ID: missing required field id
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "updated visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            null,
            1),
          400}
    };
  }

  @Test(description = "TC15 TC16 Update a visit by id: unsuccessfull", dataProvider = "visitDataUpdateUnsuccessfull")
  public void updateVisitUnsuccessfullTest(VisitsRequest body, Integer code) {
    VisitsResponse visitBeforeUpdate = serviceHelper.addPetVisit();
    VisitsResponse visit;
    Integer visitId = visitBeforeUpdate.getId();
    try {
      serviceHelper.updateVisitResponse(visitId.toString(), body, code);
      visit = serviceHelper.getVisitById(visitId.toString());
      Assertions.assertThat(visit.convertToMap()).as("Visit data").containsExactlyInAnyOrderEntriesOf(visitBeforeUpdate.convertToMap());
    }
    catch(Exception e) {
      throw new AssertionError(e.getMessage());
    }
    serviceHelper.deleteVisit(visitId.toString());
  }

  @DataProvider
  public Object[][] visitNotExisitingData() {
    return new Object[][] {
        {"700",
            new VisitsRequest(
                String.valueOf(LocalDate.now()),
                "updated visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
                700,
                10),
            404}
    };
  }

  @Test(description = "TC14 Update a visit by ID: not existing visit id", dataProvider = "visitNotExisitingData")
  public void updateVisitIfDoesntExistTest(String visitId, VisitsRequest body, Integer code) {
    serviceHelper.updateVisitResponse(visitId, body, code);
  }
}
