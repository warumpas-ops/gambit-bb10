package com.gambit.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ChessBoardView.OnMoveAttemptListener {

    private ChessEngine engine;
    private ChessBoardView boardView;

    private TextView tvOpeningTitle;
    private TextView tvChapterSubtitle;
    private TextView tvStepProgress;
    private TextView tvMoveBadge;
    private TextView tvStepTitle;
    private TextView tvExplanation;
    private TextView tvWarning;
    private TextView tvFeedback;

    private Button btnModeToggle;
    private Button btnNextChapter;
    private Button btnReset;
    private Button btnHint;
    private Button btnNextStep;

    private List<OpeningModel.Course> courses = new ArrayList<OpeningModel.Course>();
    private int currentCourseIdx = 0;
    private int currentChapterIdx = 0;
    private int currentStepIdx = 0;

    private boolean isDrillMode = false; // false = Study Mode (arrows + text), true = Drill Mode (active recall)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        engine = new ChessEngine();
        boardView = (ChessBoardView) findViewById(R.id.chessBoardView);
        boardView.setEngine(engine);
        boardView.setOnMoveAttemptListener(this);

        tvOpeningTitle = (TextView) findViewById(R.id.tvOpeningTitle);
        tvChapterSubtitle = (TextView) findViewById(R.id.tvChapterSubtitle);
        tvStepProgress = (TextView) findViewById(R.id.tvStepProgress);
        tvMoveBadge = (TextView) findViewById(R.id.tvMoveBadge);
        tvStepTitle = (TextView) findViewById(R.id.tvStepTitle);
        tvExplanation = (TextView) findViewById(R.id.tvExplanation);
        tvWarning = (TextView) findViewById(R.id.tvWarning);
        tvFeedback = (TextView) findViewById(R.id.tvFeedback);

        btnModeToggle = (Button) findViewById(R.id.btnModeToggle);
        btnNextChapter = (Button) findViewById(R.id.btnNextChapter);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnHint = (Button) findViewById(R.id.btnHint);
        btnNextStep = (Button) findViewById(R.id.btnNextStep);

        // Load all courses from assets
        courses = CourseRepository.loadAllCourses(this);

        btnModeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMode();
            }
        });

        btnNextChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChapterSelector();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCurrentChapter();
            }
        });

        btnHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHint();
            }
        });

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceToNextStep();
            }
        });

        startChapter(0, 0);
    }

    private void toggleMode() {
        isDrillMode = !isDrillMode;
        if (isDrillMode) {
            btnModeToggle.setText("[DRILL]");
            btnModeToggle.setTextColor(Color.parseColor("#F59E0B")); // Amber
            Toast.makeText(this, "Drill Mode: Play moves from memory!", Toast.LENGTH_SHORT).show();
        } else {
            btnModeToggle.setText("[STUDY]");
            btnModeToggle.setTextColor(Color.parseColor("#10B981")); // Emerald
            Toast.makeText(this, "Study Mode: Interactive guided arrows enabled", Toast.LENGTH_SHORT).show();
        }
        resetCurrentChapter();
    }

    private void startChapter(int courseIdx, int chapterIdx) {
        if (courses.isEmpty()) return;
        currentCourseIdx = courseIdx % courses.size();
        OpeningModel.Course c = courses.get(currentCourseIdx);
        if (c.chapters.isEmpty()) return;
        currentChapterIdx = chapterIdx % c.chapters.size();
        currentStepIdx = 0;

        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);
        boardView.setFlipped("black".equalsIgnoreCase(ch.orientation));

        resetCurrentChapter();
    }

    private void resetCurrentChapter() {
        engine.resetToStartingPosition();
        currentStepIdx = 0;
        boardView.setLastMove(-1, -1);
        boardView.clearSelection();
        tvFeedback.setVisibility(View.GONE);
        renderCurrentStep();
    }

    private void renderCurrentStep() {
        if (courses.isEmpty()) return;
        OpeningModel.Course c = courses.get(currentCourseIdx);
        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);

        tvOpeningTitle.setText(c.name);
        tvChapterSubtitle.setText(ch.title);

        if (currentStepIdx >= ch.steps.size()) {
            // Chapter Complete!
            tvStepProgress.setText("Completed!");
            tvMoveBadge.setText("✓ DONE");
            tvStepTitle.setText("Chapter Mastered!");
            tvExplanation.setText("You have completed all moves in this line! Tap RESET to drill again, or CHAPTERS to train a new opening variation.");
            tvWarning.setVisibility(View.GONE);
            boardView.setArrows(null);
            boardView.setHighlightSquares(null);
            return;
        }

        OpeningModel.MoveStep step = ch.steps.get(currentStepIdx);
        tvStepProgress.setText("Step " + (currentStepIdx + 1) + "/" + ch.steps.size());
        tvMoveBadge.setText(step.moveSan);
        tvStepTitle.setText(step.title);

        if (isDrillMode) {
            // In Drill Mode: Hide arrows, hide direct move answer, ask student to play
            boardView.setArrows(null);
            boardView.setHighlightSquares(null);
            tvMoveBadge.setText("? ? ?");
            tvExplanation.setText("Your turn! What is the optimal move here to execute the opening plan?");
            tvWarning.setVisibility(View.GONE);
        } else {
            // In Study Mode: Show guide arrows and full pedagogical breakdown
            tvExplanation.setText(step.explanation);
            if (step.warning != null && !step.warning.isEmpty()) {
                tvWarning.setText(step.warning);
                tvWarning.setVisibility(View.VISIBLE);
            } else {
                tvWarning.setVisibility(View.GONE);
            }
            boardView.setArrows(step.arrows);
            boardView.setHighlightSquares(step.highlightSquares);
        }

        boardView.invalidate();
    }

    private void showHint() {
        if (courses.isEmpty()) return;
        OpeningModel.Course c = courses.get(currentCourseIdx);
        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);
        if (currentStepIdx >= ch.steps.size()) return;

        OpeningModel.MoveStep step = ch.steps.get(currentStepIdx);
        boardView.setArrows(step.arrows);
        boardView.setHighlightSquares(step.highlightSquares);
        tvExplanation.setText("💡 Hint: " + step.explanation);
        tvFeedback.setText("Arrow shown! Try playing it on the board.");
        tvFeedback.setTextColor(Color.parseColor("#FBBF24"));
        tvFeedback.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMoveAttempt(int fromSq, int toSq, String uci) {
        if (courses.isEmpty()) return;
        OpeningModel.Course c = courses.get(currentCourseIdx);
        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);
        if (currentStepIdx >= ch.steps.size()) return;

        OpeningModel.MoveStep expected = ch.steps.get(currentStepIdx);

        if (uci.equalsIgnoreCase(expected.uci)) {
            // Correct move!
            engine.makeMove(fromSq, toSq);
            boardView.setLastMove(fromSq, toSq);

            tvFeedback.setText("✓ PERFECT MOVE: " + expected.moveSan);
            tvFeedback.setTextColor(Color.parseColor("#34D399")); // Emerald
            tvFeedback.setVisibility(View.VISIBLE);

            currentStepIdx++;

            // If the next step is an automated opponent reply, execute it after brief delay
            checkAndPlayOpponentReply();
        } else {
            // Incorrect move / blunder!
            tvFeedback.setText("✗ Incorrect: Try again! (Press HINT if stuck)");
            tvFeedback.setTextColor(Color.parseColor("#F87171")); // Coral red
            tvFeedback.setVisibility(View.VISIBLE);
        }
    }

    private void checkAndPlayOpponentReply() {
        renderCurrentStep();

        OpeningModel.Course c = courses.get(currentCourseIdx);
        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);

        if (currentStepIdx < ch.steps.size()) {
            final OpeningModel.MoveStep nextStep = ch.steps.get(currentStepIdx);
            if (!nextStep.isUserMove) {
                // Automated opponent reply
                boardView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        engine.makeMoveUci(nextStep.uci);
                        int from = ChessEngine.parseSquare(nextStep.uci.substring(0, 2));
                        int to   = ChessEngine.parseSquare(nextStep.uci.substring(2, 4));
                        boardView.setLastMove(from, to);

                        currentStepIdx++;
                        renderCurrentStep();
                    }
                }, 400); // 400ms organic pause
            }
        }
    }

    private void advanceToNextStep() {
        if (courses.isEmpty()) return;
        OpeningModel.Course c = courses.get(currentCourseIdx);
        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);
        if (currentStepIdx >= ch.steps.size()) {
            // Next chapter
            if (currentChapterIdx + 1 < c.chapters.size()) {
                startChapter(currentCourseIdx, currentChapterIdx + 1);
            } else {
                startChapter((currentCourseIdx + 1) % courses.size(), 0);
            }
            return;
        }

        OpeningModel.MoveStep step = ch.steps.get(currentStepIdx);
        engine.makeMoveUci(step.uci);
        int from = ChessEngine.parseSquare(step.uci.substring(0, 2));
        int to   = ChessEngine.parseSquare(step.uci.substring(2, 4));
        boardView.setLastMove(from, to);

        currentStepIdx++;
        checkAndPlayOpponentReply();
    }

    private void showChapterSelector() {
        if (courses.isEmpty()) return;

        List<String> items = new ArrayList<String>();
        for (int i = 0; i < courses.size(); i++) {
            OpeningModel.Course c = courses.get(i);
            for (int j = 0; j < c.chapters.size(); j++) {
                items.add(c.name + " (" + c.color + ") - " + c.chapters.get(j).title);
            }
        }

        final CharSequence[] array = items.toArray(new CharSequence[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Opening & Chapter");
        builder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int count = 0;
                for (int i = 0; i < courses.size(); i++) {
                    OpeningModel.Course c = courses.get(i);
                    for (int j = 0; j < c.chapters.size(); j++) {
                        if (count == which) {
                            startChapter(i, j);
                            return;
                        }
                        count++;
                    }
                }
            }
        });
        builder.show();
    }

    // Physical BlackBerry QWERTY Keyboard Support
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            advanceToNextStep();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_R) {
            resetCurrentChapter();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_H) {
            showHint();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_M) {
            toggleMode();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_C) {
            showChapterSelector();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
