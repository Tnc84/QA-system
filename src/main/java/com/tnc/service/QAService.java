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

    @Autowired
    public QAService(ModelService modelService) {
        this.modelService = modelService;
    }

    public QAResponse generateAnswer(QARequest request) {
        long startTime = System.currentTimeMillis();

        try {
            String prompt = formatPrompt(request);
            String response = modelService.generate(prompt);
            double confidence = calculateConfidence(response);

            return QAResponse.builder()
                    .answer(cleanResponse(response))
                    .confidence(confidence)
                    .processingTimeMs(System.currentTimeMillis() - startTime)
                    .build();

        } catch (Exception e) {
            log.error("Error generating answer", e);
            throw new RuntimeException("Failed to generate answer", e);
        }
    }

    private String formatPrompt(QARequest request) {
        return String.format("""
            Context: %s
            
            Question: %s
            
            Answer the question based only on the provided context. If the answer cannot be found
            in the context, say "I cannot answer this question based on the given context."
            
            Answer:""",
                request.getContext(),
                request.getQuestion()
        );
    }

    private String cleanResponse(String response) {
        // Remove any potential prompt echoing and get only the answer part
        int answerStart = response.toLowerCase().indexOf("answer:");
        if (answerStart >= 0) {
            response = response.substring(answerStart + 7).trim();
        }
        return response;
    }

    private double calculateConfidence(String response) {
        if (response.contains("cannot answer")) {
            return 0.0;
        }
        double lengthScore = Math.min(response.length() / 100.0, 1.0);
        return 0.5 + (lengthScore * 0.5);
    }
}