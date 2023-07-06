package models.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import groovy.transform.EqualsAndHashCode;
import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Builder
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisitsRequest {
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
