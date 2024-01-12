package com.intuit.craftdemo.entities;

import com.intuit.craftdemo.enums.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "driver_documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document implements Serializable {

        private static final long serialVersionUID = 1L;

        @EmbeddedId
        private DocumentId documentId;

        @ManyToOne
        @JoinColumn(name = "driver_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "driver_document"))
        private Driver driver;

        @Column(name = "document_file")
        private String documentFile;

        @Column(name = "documentStatus")
        @Enumerated(EnumType.STRING)
        private DocumentStatus documentStatus;
}
