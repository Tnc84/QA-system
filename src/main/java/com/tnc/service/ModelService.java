package com.tnc.service;


import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import com.tnc.config.ModelConfig;
import com.tnc.model.QARequest;
import com.tnc.model.QAResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
@Slf4j
public class ModelService {
    private final ModelConfig config;
    private HuggingFaceTokenizer tokenizer;
    private Process modelProcess;

    @Autowired
    public ModelService(ModelConfig config) {
        this.config = config;
    }

    @PostConstruct
    public void initModel() {
        try {
            tokenizer = HuggingFaceTokenizer.newInstance(config.getModelName());
            startModelProcess();
            log.info("Model service initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize model service", e);
            throw new RuntimeException("Model initialization failed", e);
        }
    }

    public QAResponse generateAnswer(@Valid QARequest request) {
        long startTime = System.currentTimeMillis();
        try {
            String prompt = formatPrompt(request);
            String response = generate(prompt);
            double confidence = calculateConfidence(response);

            return QAResponse.builder()
                    .answer(cleanResponse(response))
                    .confidence(confidence)
                    .processingTimeMs(System.currentTimeMillis() - startTime)
                    .build();

        } catch (Exception e) {
            log.error("Error generating answer for question: {}", request.getQuestion(), e);
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

    String generate(String prompt) {
        try {
            var encoding = tokenizer.encode(prompt);
            log.debug("Input tokens: {}", encoding.getTokens().length);
            return processWithLocalModel(prompt);
        } catch (Exception e) {
            log.error("Error generating response", e);
            throw new RuntimeException("Generation failed", e);
        }
    }

    private String processWithLocalModel(String prompt) {
        // TODO: Implement actual model communication
        return "Sample response - implement actual model communication";
    }

    private String cleanResponse(String response) {
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

    private void startModelProcess() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    Paths.get(config.getModelPath()).toString(),
                    "--temp", String.valueOf(config.getTemperature()),
                    "--ctx_size", String.valueOf(config.getMaxTokens())
            );
            pb.redirectErrorStream(true);
            modelProcess = pb.start();
            log.info("Model process started successfully");
        } catch (Exception e) {
            log.error("Failed to start model process", e);
            throw new RuntimeException("Model process start failed", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            if (modelProcess != null) {
                modelProcess.destroy();
                log.info("Model process terminated");
            }
            if (tokenizer != null) {
                tokenizer.close();
                log.info("Tokenizer resources released");
            }
        } catch (Exception e) {
            log.error("Error during cleanup", e);
        }
    }
}

