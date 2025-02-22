package com.tnc.service;

import com.tnc.model.QARequest;
import com.tnc.model.QAResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QAService {
    private final ModelService modelService;
    private final QAUtilsService qaUtils;

    @Autowired
    public QAService(ModelService modelService, QAUtilsService qaUtils) {
        this.modelService = modelService;
        this.qaUtils = qaUtils;
    }

    public QAResponse generateAnswer(QARequest request) {
        long startTime = System.currentTimeMillis();

        try {
            String prompt = qaUtils.formatPrompt(request);
            String response = modelService.generate(prompt);
            double confidence = qaUtils.calculateConfidence(response);

            return QAResponse.builder()
                    .answer(qaUtils.cleanResponse(response))
                    .confidence(confidence)
                    .processingTimeMs(System.currentTimeMillis() - startTime)
                    .build();

        } catch (Exception e) {
            log.error("Error generating answer", e);
            throw new RuntimeException("Failed to generate answer", e);
        }
    }
}