package com.intuit.craftdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class HuberEvent{
   String licenseNumber;
   String status;
}
