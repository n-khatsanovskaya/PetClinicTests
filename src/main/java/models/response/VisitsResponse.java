package models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import groovy.transform.EqualsAndHashCode;
import groovy.transform.builder.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@EqualsAndHashCode
@Getter
@JsonIgnoreProperties
public class VisitsResponse {
    private String date;
    @JsonProperty("description")
    private String description;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("petId")
    private Integer petId;

    public Map<String, String> convertToMap() {
        return Map.of(
            "date", date,
            "description", description,
            "id", id.toString(),
            "petId", petId.toString()
        );
    }
}
