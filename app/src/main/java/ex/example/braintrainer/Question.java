package ex.example.braintrainer;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Question {
    private final String operation;
    private final List<Integer> operands;

    public boolean isCorrectAnswer(int guess) {
        return guess == getAnswer();
    }

    public int getAnswer() {
        switch (operation) {
            case "+":
                return operands.get(0) + operands.get(1);
            case "-":
                return operands.get(0) - operands.get(1);
            case "x":
                return operands.get(0) * operands.get(1);
            case "÷":
                if (operands.get(1) == 0) {
                    throw new RuntimeException("Bad Operand: Cannot divide by 0");
                }
                return operands.get(0) / operands.get(1);
            default:
                throw new RuntimeException("Bad Operator:" + operation);
        }
    }

    public String getQuestionString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < operands.size() - 1; i++) {
            stringBuilder.append(operands.get(i));
            stringBuilder.append(" ");
            stringBuilder.append(operation);
            stringBuilder.append(" ");
        }

        stringBuilder.append(operands.get(operands.size() - 1));
        return stringBuilder.toString();
    }
}
