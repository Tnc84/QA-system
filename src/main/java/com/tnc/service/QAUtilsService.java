package com.tnc.service;

import com.tnc.model.QARequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QAUtilsService {

    public String formatPrompt(QARequest request) {
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

    public String cleanResponse(String response) {
        int answerStart = response.toLowerCase().indexOf("answer:");
        if (answerStart >= 0) {
            response = response.substring(answerStart + 7).trim();
        }
        return response;
    }

    public double calculateConfidence(String response) {
        if (response.contains("cannot answer")) {
            return 0.0;
        }
        double lengthScore = Math.min(response.length() / 100.0, 1.0);
        return 0.5 + (lengthScore * 0.5);
    }
}
