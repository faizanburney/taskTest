package com.freenow.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freenow.domainvalue.EngineType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarDTO
{
    @JsonIgnore
    private Long id;

    @NotNull(message = "The license plate can not be null!")
    private String licensePlate;

    @NotNull(message = "The number of seats must be specified!")
    @Min(value = 2, message = "The minimum number of seats is 2")
    @Max(value = 5, message = "The maximum number of seats is 5")
    private Integer seatCount;

    private Boolean convertible = false;

    @NotNull(message = "The type of the Engine must be specified")
    private EngineType engineType;

    @NotNull(message = "The Manufacturer must be specified")
    private String manufacturer;

    @Min(value = 0, message = "The minimum rating can not be less than 0")
    @Max(value = 5, message = "The maximum rating can not be greater than 5")
    private Double rating;

    @JsonProperty
    public Long getId() {
        return id;
    }
}
