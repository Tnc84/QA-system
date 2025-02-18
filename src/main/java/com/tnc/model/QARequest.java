package com.tnc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QARequest {

    @NotBlank(message = "Question cannot be empty")
    private String question;
    @NotBlank(message = "Context cannot be empty")
    private String context;
}
