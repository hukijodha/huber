package com.intuit.craftdemo.dto.driver;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDriver {
    boolean status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String errorMessage;
}
