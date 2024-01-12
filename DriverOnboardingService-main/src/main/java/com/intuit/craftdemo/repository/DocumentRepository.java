package com.intuit.craftdemo.repository;


import com.intuit.craftdemo.entities.Document;
import com.intuit.craftdemo.entities.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Driver, Long> {

    @Modifying
    @Query("UPDATE Document dd " +
            "SET dd.documentFile = :newDocumentName " +
            "WHERE dd.documentId.driverId = :driverId " +
            "AND dd.documentId.documents = :documentType")
    void updateDocumentName(@Param("driverId") Long driverId,
                            @Param("documentType") String documentType,
                            @Param("newDocumentName") String newDocumentName);

    @Modifying
    @Query("UPDATE Document dd " +
            "SET dd.documentStatus = :documentStatus " +
            "WHERE dd.documentFile = :documentName  "
           )
    void updateDocumentStatusByFileName(@Param("documentName") String documentName,@Param("documentStatus") String documentStatus);

    @Query("SELECT dd.documentFile FROM Document dd " +
            "WHERE dd.documentId.driverId = :driverId " +
            "AND dd.documentId.documents = :documentType")
    Optional<String> findDocumentFileNameByDriverIdAndDocumentType(
            @Param("driverId") Long driverId,
            @Param("documentType") String documentType);


    @Query("SELECT DISTINCT dd.documentId.documents FROM Document dd " +
            "WHERE dd.documentStatus = 'VERIFICATION_FAILED' " +
            "AND dd.documentId.driverId = :driverId")
    List<String> findDocumentTypesWithVerificationFailedStatusByDriverId(
            @Param("driverId") Long driverId);

    @Query("SELECT dd FROM Document dd " +
            "WHERE dd.documentId.driverId = :driverId")
    List<Document> findAllDocumentsByDriverId(@Param("driverId") Long driverId);


}
