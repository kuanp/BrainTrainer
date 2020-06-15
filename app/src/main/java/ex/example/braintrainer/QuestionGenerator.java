package ex.example.braintrainer;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuestionGenerator {
    private static final int MAX = 20;

    private Set<Question> pastQuestions;
    private final Random rand;

    public QuestionGenerator() {
        pastQuestions = new HashSet<>();
        rand = new Random();
    }

    public List<Integer> generateAnswerOptions(Question question, int numOptions) {
        List<Integer> options = new ArrayList<>();
        Set<Integer> pickedNumbers = new HashSet<>();
        pickedNumbers.add(question.getAnswer());

        // generate some random options, ensure no overlap
        for (int i = 0; i < numOptions; i++) {
            int wrongOption = generateNumber(question.getAnswer());
            while (pickedNumbers.contains(wrongOption)) {
                wrongOption = generateNumber(wrongOption);
            }

            pickedNumbers.add(wrongOption);
            options.add(wrongOption);
        }

        // set the correct answer to a random index
        options.set(rand.nextInt(numOptions), question.getAnswer());

        return options;
    }

    public Question generateNewQuestion() {
        while (true) {
            Question generatedQuestion =
                    Question.builder()
                            .operation(generateOperation())
                            .operands(Arrays.asList(generateNumber(), generateNumber()))
                            .build();

            // make sure we never generate the same question again
            if (!pastQuestions.contains(generatedQuestion)) {
                pastQuestions.add(generatedQuestion);
                return generatedQuestion;
            }
        }
    }

    private String generateOperation() {
        int operatorSeed = rand.nextInt(4);
        switch (operatorSeed) {
            case 0:
                return "+";
            case 1:
                return "-";
            case 2:
                return "x";
            case 3:
                // division disabled since it can generate decimals. return + instead
                return "+";
            default:
                throw new RuntimeException("generateOperation is broken");
        }
    }

    private int generateNumber() {
        return rand.nextInt(MAX) + 1; // cannot return 0
    }

    private int generateNumber(int anchor) {
        if (rand.nextInt() % 2 == 0) {
            return anchor + generateNumber();
        } else {
            return anchor - generateNumber();
        }
    }

}
