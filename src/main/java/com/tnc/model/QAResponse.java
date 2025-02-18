package com.tnc.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QAResponse {
    private String answer;
    private double confidence;
    private long processingTimeMs;
}
