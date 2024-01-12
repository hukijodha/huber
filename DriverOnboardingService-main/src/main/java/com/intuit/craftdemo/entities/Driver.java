package com.intuit.craftdemo.entities;


import com.intuit.craftdemo.enums.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "drivers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String addressLine1;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    private String addressLine2;
    private String country;
    private String city;
    private String state;

    @Column(nullable = false)
    private String pinCode;

    private String highestQualification;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection
    @CollectionTable(
            name = "driver_documents",
            joinColumns = @JoinColumn(name = "driver_id")
    )
    @MapKeyColumn(name = "documents")
    @Column(name = "document_file")
    private Map<String, String> documents;

    @ElementCollection
    @CollectionTable(
            name = "driver_known_languages",
            joinColumns = @JoinColumn(name = "driver_id")
    )
    @Column(name = "known_languages")
    private List<String> knownLanguages;
    private boolean hasDrivingExperience;
    private String lastEmployerDetails;
    private boolean acceptTerms;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    @OneToMany(mappedBy = "driver")
    private List<Token> tokens;

}
