package com.github.plugatarev.cracker.common;

import java.util.List;

public record WorkerCrackingResponse(RequestId id, int workerPart, List<String> data) {
}
