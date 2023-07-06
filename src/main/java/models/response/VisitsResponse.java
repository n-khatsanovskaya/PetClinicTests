package models.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import groovy.transform.EqualsAndHashCode;
import groovy.transform.builder.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        Map<String, String> map = new HashMap<>();
        Optional.ofNullable(date).ifPresent(value -> map.put("date", value));
        Optional.ofNullable(description).ifPresent(value -> map.put("description", value));
        Optional.ofNullable(id).ifPresent(value -> map.put("id", value.toString()));
        Optional.ofNullable(petId).ifPresent(value -> map.put("petId", value.toString()));
        return map;
    }
}
