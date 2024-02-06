package com.github.plugatarev.cracker.service;

import com.github.plugatarev.cracker.common.WorkerCrackingRequest;
import com.github.plugatarev.cracker.common.WorkerCrackingResponse;
import com.github.plugatarev.cracker.util.MD5HashGenerator;
import lombok.RequiredArgsConstructor;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.paukov.combinatorics.CombinatoricsFactory.createPermutationWithRepetitionGenerator;
import static org.paukov.combinatorics.CombinatoricsFactory.createVector;

@Service
@RequiredArgsConstructor
public class DefaultCrackingTaskService implements CrackingTaskService {

    private final WebClientResultSendingService sendingService;
    private final Executor crackingTaskExecutor;

    @Override
    public void executeCrackingTask(WorkerCrackingRequest managerRequest) {
        CompletableFuture.supplyAsync(() -> executeTask(managerRequest), crackingTaskExecutor)
                .thenAccept(sendingService::sendResultToManager);
    }

    public static WorkerCrackingResponse executeTask(WorkerCrackingRequest managerRequest) {
        final BigDecimal allPossibleWordsNumber = countNumberOfAllPossibleWords(managerRequest.alphabet(),
                managerRequest.hashLength());
        final BigDecimal wordsProPart = countNumberOfWordsProPart(allPossibleWordsNumber, managerRequest.taskPartId());
        final ICombinatoricsVector<String> alphabetVector = createVector(managerRequest.alphabet());
        BigInteger wordsCounter = BigInteger.ZERO;
        int currentWordLength = 1;
        Generator<String> generator = createPermutationWithRepetitionGenerator(alphabetVector, currentWordLength);
        Iterator<ICombinatoricsVector<String>> iterator = generator.iterator();
        while (wordsCounter.compareTo(wordsProPart.multiply(BigDecimal
                .valueOf(managerRequest.taskPartId())).toBigInteger()) < 0) {
            if (iterator.hasNext()) {
                wordsCounter = wordsCounter.add(BigInteger.ONE);
                iterator.next();
            } else {
                currentWordLength++;
                generator = createPermutationWithRepetitionGenerator(alphabetVector, currentWordLength);
                iterator = generator.iterator();
            }
        }
        BigInteger currentPartWordsCounter = BigInteger.ZERO;
        final StringBuilder stringBuilder = new StringBuilder();
        final List<String> suitableWords = new ArrayList<>();
        while (wordsCounter.compareTo(allPossibleWordsNumber.toBigInteger()) < 0 && currentPartWordsCounter
                .compareTo(wordsProPart.toBigInteger()) < 0) {
            if (iterator.hasNext()) {
                final ICombinatoricsVector<String> vector = iterator.next();
                final String currentWord = getWordFrom(vector, stringBuilder);
                if (managerRequest.hash().equals(MD5HashGenerator
                        .generateHashFrom(currentWord))) {
                    suitableWords.add(currentWord);
                }
                stringBuilder.setLength(0);
                wordsCounter = wordsCounter.add(BigInteger.ONE);
                currentPartWordsCounter = currentPartWordsCounter.add(BigInteger.ONE);
            } else {
                currentWordLength++;
                generator = createPermutationWithRepetitionGenerator(alphabetVector, currentWordLength);
                iterator = generator.iterator();
            }
        }
        return new WorkerCrackingResponse(managerRequest.requestId(), managerRequest.taskPartId(), suitableWords);

    }

    private static BigDecimal countNumberOfAllPossibleWords(List<String> alphabet, int maxWordLength) {
        BigDecimal allPossibleWordsNumber = BigDecimal.ZERO;
        BigDecimal currentLengthPossibleWordsNumber = BigDecimal.ONE;
        for (int i = 0; i < maxWordLength; i++) {
            currentLengthPossibleWordsNumber = currentLengthPossibleWordsNumber.multiply(BigDecimal
                    .valueOf(alphabet.size()));
            allPossibleWordsNumber = allPossibleWordsNumber.add(currentLengthPossibleWordsNumber);
        }
        return allPossibleWordsNumber;
    }

    private static BigDecimal countNumberOfWordsProPart(BigDecimal allPossibleWordsNumber, int partCount) {
        return allPossibleWordsNumber.divide(BigDecimal.valueOf(partCount), RoundingMode.CEILING);
    }

    private static String getWordFrom(ICombinatoricsVector<String> vector, StringBuilder stringBuilder) {
        for (String vectorElem : vector) {
            stringBuilder.append(vectorElem);
        }
        return stringBuilder.toString();
    }
}
