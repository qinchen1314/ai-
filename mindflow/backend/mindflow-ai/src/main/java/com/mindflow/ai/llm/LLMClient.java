package com.mindflow.ai.llm;

public interface LLMClient {

    String complete(String provider, String model, String prompt);
}
