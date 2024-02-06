package com.github.plugatarev.cracker.common;

import java.util.List;

public record WorkerCrackingRequest
        (RequestId requestId, String hash, int hashLength, int taskPartId, int workerCount, List<String> alphabet) {
}
