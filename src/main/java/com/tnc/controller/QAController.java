package com.tnc.controller;

import com.tnc.model.QARequest;
import com.tnc.model.QAResponse;
import com.tnc.service.ModelService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qa")
@Slf4j
public class QAController {
    private final ModelService qaService;

    @Autowired
    public QAController(ModelService modelService) {
        this.qaService = modelService;
    }
    @PostMapping("/answer")
    public QAResponse getAnswer(@Valid @RequestBody QARequest request){
        log.info("Received question: {}", request.getQuestion());
        return qaService.generateAnswer(request);
    }

    public String healthCheck(){
        return "QA system is running";
    }
}
