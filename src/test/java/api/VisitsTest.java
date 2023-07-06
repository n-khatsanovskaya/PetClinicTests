package api;

import helpers.ServiceHelper;
import io.restassured.response.Response;
import models.request.VisitsRequest;
import models.response.VisitsResponse;
import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//TODO:
// Add suites support in pom.xml

public class VisitsTest {
  ServiceHelper serviceHelper = new ServiceHelper();

  @Test(description = "Get the list of all visits")
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

  @Test(description = "Add new visit", dataProvider = "visitData")
  public void createVisitTest(VisitsRequest body) {
    VisitsResponse visit = serviceHelper.addVisit(body);
    Assertions.assertThat(visit.getPetId()).as("Pet id").isEqualTo(body.getPetId());
  }

  @DataProvider
  public Object[][] visitDataMissingRequiredFields() {
    return new Object[][] {
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "new visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            null,
            1), "must not be null"},
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            null,
            7,
            1), "must not be null"}
    };
  }

  @Test(description = "Add new visit unsuccessfull", dataProvider = "visitDataMissingRequiredFields")
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
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "new visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            null,
            null), "1"},
        {new VisitsRequest(
            null,
            "new visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            null,
            null), "1"}
    };
  }

  @Test(description = "Add new visit", dataProvider = "petVisitData")
  public void createPetVisitTest(VisitsRequest body, String petId) {
    VisitsResponse visit = serviceHelper.addPetVisit(petId, "1", body);
    Assertions.assertThat(visit.convertToMap()).as("Response body").containsAllEntriesOf(body.convertToMap());
  }

  @Test(description = "Delete visit", dataProvider = "petVisitData")
  public void deleteVisitTest (VisitsRequest body, String petId) {

    VisitsResponse visit = serviceHelper.addPetVisit(petId, "1", body);

    String visitId = visit.getId().toString();
    visit = serviceHelper.deleteVisit(visitId);
    Assertions.assertThat(visit).as("Response body").isNotNull();

    serviceHelper.getVisitByIdFailedResponse(visitId, 404);
  }

  @Test(description = "Delete visit")
  public void deleteVisitUnsuccessfullTest() {
    serviceHelper.deleteVisitUnsuccessfull("100");
  }

  @DataProvider
  public Object[][] petVisitDataUnsuccessfull() {
    return new Object[][] {
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "new visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            null,
            null), "100", 404, "TBD"},
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            null,
            null,
            null), "1", 400, "TBD"}
    };
  }

  @Test(description = "Add new visit unsuccessfull", dataProvider = "petVisitDataUnsuccessfull")
  public void createPetVisitUnsuccessfullTest(VisitsRequest body, String petId, Integer status, String error) {
    Response response = serviceHelper.addPetVisitUnsuccessfull(petId, "1", body, status);
    //According to spec should check the response body
    //response.then().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("ErrorResponse.json"));
    //TODO: check desired field
    Assertions.assertThat(response.getBody().toString()
            .contains(error))
        .as("Response error message").isTrue();
    }

  @Test(description = "Get visit by id")
  public void getVisitByIdTest() {
    String id = "7";
    VisitsResponse visit = serviceHelper.getVisitById(id);
    Assertions.assertThat(visit.getId().toString()).isEqualTo(id);
  }

  @DataProvider
  public Object[][] visitIdsFailed() {
    return new Object[][] {
        {"700", 404}, {"string", 400}
    };
  }

  @Test(description = "Get visit by id", dataProvider = "visitIdsFailed")
  public void getVisitByIdUnsuccessfulTest(String id, Integer code) {
    Response response = serviceHelper.getVisitByIdFailedResponse(id, code);
    //TODO: check response structure
  }

  @DataProvider
  public Object[][] visitDataUpdate() {
    return new Object[][] {
        {new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "updated visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            7,
            10)}
    };
  }

  @Test(description = "Update visit", dataProvider = "visitDataUpdate")
  public void updateVisitTest(VisitsRequest body) {
    serviceHelper.updateVisitResponse(body.getId().toString(), body, 204);
    VisitsResponse visit = serviceHelper.getVisitById(body.getId().toString());
    Assertions.assertThat(visit.convertToMap()).as("Visit data").containsAllEntriesOf(body.convertToMap());
  }

  @DataProvider
  public Object[][] visitDataUpdateUnsuccessfull() {
    return new Object[][] {
        {"700",
          new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "updated visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            700,
            10),
          404},
        {"7",
          new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "updated visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            7,
            100),
          404}, //Not clear from documentation
        {"7",
          new VisitsRequest(
            String.valueOf(LocalDate.now()),
            null,
            7,
            1),
          400},
        {"7",
          new VisitsRequest(
            String.valueOf(LocalDate.now()),
            "updated visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
            null,
            1),
          400},
        {"7",
            new VisitsRequest(
                null,
                "updated visit " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd HH:mm:ss")),
                7,
                10),
            204} //Should be 200
    };
  }

  @Test(description = "Update visit", dataProvider = "visitDataUpdateUnsuccessfull")
  public void updateVisitUnsuccessfullTest(String visitId, VisitsRequest body, Integer code) {
    //TODO: add checking of visit fields before and after updating
    serviceHelper.updateVisitResponse(visitId, body, code);
    VisitsResponse visit = serviceHelper.getVisitById(visitId);
    Assertions.assertThat(visit.convertToMap()).as("Visit data").containsAllEntriesOf(body.convertToMap());
  }
}
