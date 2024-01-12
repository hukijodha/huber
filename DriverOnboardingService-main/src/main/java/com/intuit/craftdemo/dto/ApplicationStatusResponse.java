package com.intuit.craftdemo.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@Setter
@Getter
@Builder
public class ApplicationStatusResponse {
    public String status;
    public String nextStep;
    public List<String> nextStepsProperties;
}
