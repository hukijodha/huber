package com.intuit.craftdemo.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class OnBoardingEvent {
   String firstName;
   String lastName;
   List<String> documentsShared;
   String phoneNumber;
   String address1;
   String address2;
}
