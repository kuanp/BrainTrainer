package ex.example.braintrainer;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ScoreKeeper {
    private int numCorrect = 0;
    private int numIncorrect = 0;
    private int total = 0;

    public void recordCorrect() {
        numCorrect ++;
        total ++;
    }

    public void recordIncorrect() {
        numIncorrect++;
        total++;
    }

    public String getScore() {
        return String.format("%s/%s", numCorrect, total);
    }
}
