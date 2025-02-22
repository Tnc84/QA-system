# QA System with LLaMA Integration

## Project Overview
This is a Question-Answering system built with Spring Boot that integrates with the LLaMA (Large Language Model) for generating context-aware answers. The system provides a RESTful API that accepts questions along with context and returns AI-generated answers.

## Technical Stack
- Java 21
- Spring Boot 3.4.2
- DJL (Deep Java Library) 0.26.0
- LLaMA Model
- Maven for dependency management
- Lombok for reducing boilerplate code

## Project Structure
```
com.tnc/
├── QaSystemApplication.java
├── config/
│   └── ModelConfig.java
├── controller/
│   └── QAController.java
├── model/
│   ├── QARequest.java
│   └── QAResponse.java
└── service/
    ├── ModelService.java
    ├── QAService.java
    └── QAUtilsService.java
```

## Component Description

### Configuration
- `ModelConfig`: Handles LLaMA model configuration including model path, token limits, and temperature settings

### Controllers
- `QAController`: REST endpoint handling QA requests at `/api/qa/answer`

### Models
- `QARequest`: Data model for incoming questions with validation
- `QAResponse`: Response structure with answer, confidence score, and processing time

### Services
- `ModelService`: Core service managing LLaMA model interactions
- `QAService`: Orchestrates the QA process
- `QAUtilsService`: Utility service for prompt formatting and response processing

## Current Features
1. Context-aware question answering
2. Input validation
3. Confidence score calculation
4. Processing time tracking
5. Error handling and logging
6. Health check endpoint

## Development Progress

### Current Status
- Basic Q&A functionality implemented
- LLaMA model integration established
- REST API endpoints operational
- Basic error handling in place

### Next Steps
1. [ ] Implement comprehensive test coverage
2. [ ] Add model performance metrics
3. [ ] Implement caching for frequent questions
4. [ ] Add rate limiting
5. [ ] Enhance error handling and recovery
6. [ ] Implement async processing for long-running queries

### Future Enhancements
1. Model fine-tuning capabilities
2. Multiple model support
3. Response streaming
4. Authentication and authorization
5. API documentation with Swagger/OpenAPI
6. Docker containerization

## Session Log

### Session [DATE] - Initial Setup
- Basic project structure created
- LLaMA integration implemented
- Basic Q&A functionality working

## Configuration
Current application.properties settings:
```properties
server.port=8080
llama.model-path=D:/LLAMA/.llama/checkpoints/Llama3.2-3B
llama.max-tokens=2048
llama.temperature=0.7
model.model-name=llama
```

## API Usage
POST request to `/api/qa/answer`:
```json
{
    "question": "Your question here",
    "context": "Context information here"
}
```

Response format:
```json
{
    "answer": "Generated answer",
    "confidence": 0.85,
    "processingTimeMs": 1234
}
```

## Notes for Next Session
- Review current implementation of confidence score calculation
- Consider adding request/response DTOs
- Evaluate performance optimization opportunities
- Plan integration tests implementation