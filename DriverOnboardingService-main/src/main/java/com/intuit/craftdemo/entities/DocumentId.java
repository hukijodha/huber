package com.intuit.craftdemo.entities;


import lombok.*;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class DocumentId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "documents")
    private String documents;
}
