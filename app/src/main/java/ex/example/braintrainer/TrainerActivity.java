package ex.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;

public class TrainerActivity extends AppCompatActivity {
    private static final int TIME_ALLOWED_SECONDS = 30;

    private ScoreKeeper scoreKeeper;
    private QuestionGenerator questionGenerator;
    private Question currentQuestion;
    private int timeLeft;
    private CountDownTimer timer;

    private TextView timeRemainingTextView;
    private TextView scoreTextView;
    private TextView questionTextView;
    private TextView gameStateTextView;
    private TextView correctnessTextView;

    private ConstraintLayout optionsLayout;
    private Button playAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer);

        timeRemainingTextView = findViewById(R.id.timeRemainingTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        questionTextView = findViewById(R.id.questionTextView);
        gameStateTextView = findViewById(R.id.gameStateTextView);
        correctnessTextView = findViewById(R.id.correctnessTextView);

        optionsLayout = findViewById(R.id.optionsLayout);
        playAgainButton = findViewById(R.id.playAgainButton);

        resetGameState();
    }

    public void onOptionClick(View view) {
        Button optionButton = (Button) view;
        int guess = (int) optionButton.getTag();

        if (currentQuestion.isCorrectAnswer(guess)) {
            scoreKeeper.recordCorrect();
            displayGuessCorrect(true);
        } else {
            scoreKeeper.recordIncorrect();
            displayGuessCorrect(false);
        }
        currentQuestion = questionGenerator.generateNewQuestion();

        updateScoreAndTime();
        setQuestionAndOptions();
    }

    public void onPlayAgainButtonClicked(View view) {
        resetGameState();
    }

    private void resetGameState() {
        scoreKeeper = new ScoreKeeper();
        questionGenerator = new QuestionGenerator();
        currentQuestion = questionGenerator.generateNewQuestion();
        timeLeft = TIME_ALLOWED_SECONDS;

        updateScoreAndTime();
        setQuestionAndOptions();
        setGameEndButtons();
        hideCorrectnessText();
        startCountDown();
    }

    private void updateScoreAndTime() {
        setTimeLeft();
        setCurrentScore();
    }

    private void setTimeLeft() {
        timeRemainingTextView.setText( String.format("%02ds", timeLeft));
    }

    private void setQuestionAndOptions() {
        questionTextView.setText(currentQuestion.getQuestionString());

        List<Integer> options =
                questionGenerator.generateAnswerOptions(currentQuestion, optionsLayout.getChildCount());

        for (int i = 0; i < options.size(); i++) {
            Button optionButton = (Button) optionsLayout.getChildAt(i);
            optionButton.setText(String.valueOf(options.get(i)));
            optionButton.setTag(options.get(i));
            optionButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // return true (and disables the button) when no time left
                    return timeLeft == 0;
                }
            });
        }
    }

    private void setCurrentScore() {
        scoreTextView.setText(scoreKeeper.getScore());
    }

    private void setGameEndButtons() {
        if (timeLeft != 0) {
            gameStateTextView.setVisibility(View.INVISIBLE);
            playAgainButton.setVisibility(View.INVISIBLE);
        } else {
            gameStateTextView.setVisibility(View.VISIBLE);
            playAgainButton.setVisibility(View.VISIBLE);
        }
    }

    private void hideCorrectnessText() {
        correctnessTextView.setVisibility(View.INVISIBLE);
    }

    private void displayGuessCorrect(boolean isCorrect) {
        correctnessTextView.setVisibility(View.VISIBLE);
        if (isCorrect) {
            correctnessTextView.setBackgroundColor(Color.GREEN);
            correctnessTextView.setText(R.string.is_correct);
        } else {
            correctnessTextView.setBackgroundColor(Color.RED);
            correctnessTextView.setText(R.string.is_incorrect);
        }
    }

    private void startCountDown() {
        timer = new CountDownTimer(timeLeft * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (int) millisUntilFinished / 1000;
                updateScoreAndTime();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                setGameEndButtons();
            }
        };
        timer.start();
    }
}
