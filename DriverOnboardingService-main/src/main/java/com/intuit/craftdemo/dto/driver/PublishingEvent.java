package com.intuit.craftdemo.dto.driver;

import com.intuit.craftdemo.entities.Document;
import com.intuit.craftdemo.entities.Driver;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class PublishingEvent {
    Driver driver;
    List<Document> document;
}
