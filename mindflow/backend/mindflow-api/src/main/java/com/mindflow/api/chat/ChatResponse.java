package com.mindflow.api.chat;

import java.util.List;

public record ChatResponse(String answer, List<String> sources) {
}
