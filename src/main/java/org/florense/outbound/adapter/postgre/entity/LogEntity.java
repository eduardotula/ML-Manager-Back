package org.florense.outbound.adapter.postgre.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    private String type;
    @Column(name = "class_name")
    private String className;
    @Column(name = "params")
    private String params;
    @Column(name = "error_message", columnDefinition = "VARCHAR(500)")
    private String errorMessage;
}
