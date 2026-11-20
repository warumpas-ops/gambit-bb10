package com.gambit.app;

import android.app.Activity;
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
    private ProgressManager progressManager;

    private TextView tvOpeningTitle;
    private TextView tvStreakBadge;
    private TextView tvChapterSubtitle;
    private TextView tvMasteryStars;
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

    private boolean isDrillMode = false;
    private boolean hadMistakeInChapter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        engine = new ChessEngine();
        progressManager = new ProgressManager(this);

        boardView = (ChessBoardView) findViewById(R.id.chessBoardView);
        boardView.setEngine(engine);
        boardView.setOnMoveAttemptListener(this);

        tvOpeningTitle = (TextView) findViewById(R.id.tvOpeningTitle);
        tvStreakBadge = (TextView) findViewById(R.id.tvStreakBadge);
        tvChapterSubtitle = (TextView) findViewById(R.id.tvChapterSubtitle);
        tvMasteryStars = (TextView) findViewById(R.id.tvMasteryStars);
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

        courses = CourseRepository.loadAllCourses(this);

        // Read chosen course & chapter from intent
        int initCourseIdx = getIntent().getIntExtra("courseIdx", 0);
        int initChapterIdx = getIntent().getIntExtra("chapterIdx", 0);

        btnModeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMode();
            }
        });

        // "LIBRARY" button returns to Hub
        btnNextChapter.setText("☷ LIBRARY");
        btnNextChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Return to Hub
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

        updateStreakDisplay();
        startChapter(initCourseIdx, initChapterIdx);
    }

    private void updateStreakDisplay() {
        int streak = progressManager.getStreak();
        tvStreakBadge.setText("🔥 " + streak + "d");
    }

    private void updateMasteryStars(String chapterId) {
        int level = progressManager.getChapterMasteryLevel(chapterId);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i < level ? "★" : "☆");
        }
        tvMasteryStars.setText(sb.toString());
    }

    private void toggleMode() {
        isDrillMode = !isDrillMode;
        if (isDrillMode) {
            btnModeToggle.setText("[DRILL]");
            btnModeToggle.setTextColor(Color.parseColor("#F59E0B"));
            Toast.makeText(this, "Drill Mode: Recall moves without hints!", Toast.LENGTH_SHORT).show();
        } else {
            btnModeToggle.setText("[STUDY]");
            btnModeToggle.setTextColor(Color.parseColor("#10B981"));
            Toast.makeText(this, "Study Mode: Guided arrows enabled", Toast.LENGTH_SHORT).show();
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

        // AUTOMATIC BOARD PERSPECTIVE:
        // If course is for Black (e.g. Caro-Kann, Sicilian) -> FLIP so Black is at the bottom!
        // If course is for White (e.g. Italian Game) -> White is at the bottom!
        boolean playAsBlack = "black".equalsIgnoreCase(c.color) || "black".equalsIgnoreCase(ch.orientation);
        boardView.setFlipped(playAsBlack);

        resetCurrentChapter();
    }

    private void resetCurrentChapter() {
        engine.resetToStartingPosition();
        currentStepIdx = 0;
        hadMistakeInChapter = false;
        boardView.setLastMove(-1, -1);
        boardView.clearSelection();
        tvFeedback.setVisibility(View.GONE);

        if (!courses.isEmpty()) {
            OpeningModel.Course c = courses.get(currentCourseIdx);
            OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);
            updateMasteryStars(ch.id);
        }

        renderCurrentStep();

        // If the very first move is an automated opponent move (e.g. when playing Black against 1. e4), play it!
        checkAndPlayOpponentReply();
    }

    private void renderCurrentStep() {
        if (courses.isEmpty()) return;
        OpeningModel.Course c = courses.get(currentCourseIdx);
        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);

        tvOpeningTitle.setText(c.name);
        tvChapterSubtitle.setText(ch.title);

        if (currentStepIdx >= ch.steps.size()) {
            progressManager.recordChapterAttempt(ch.id, !hadMistakeInChapter);
            updateMasteryStars(ch.id);

            tvStepProgress.setText("Completed!");
            tvMoveBadge.setText("✓ MASTERED");
            tvStepTitle.setText("Chapter Complete!");
            int rate = progressManager.getChapterSuccessRate(ch.id);
            tvExplanation.setText("Great session! Success rate: " + rate + "%. Total moves trained: " + progressManager.getTotalMovesTrained() + ". Tap NEXT for the next line or LIBRARY to choose another opening.");
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
            boardView.setArrows(null);
            boardView.setHighlightSquares(null);
            tvMoveBadge.setText("? ? ?");
            tvExplanation.setText("Your turn! Play the expected repertoire move from memory.");
            tvWarning.setVisibility(View.GONE);
        } else {
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
            engine.makeMove(fromSq, toSq);
            boardView.setLastMove(fromSq, toSq);

            progressManager.incrementMovesTrained();

            tvFeedback.setText("✓ PERFECT MOVE: " + expected.moveSan);
            tvFeedback.setTextColor(Color.parseColor("#34D399"));
            tvFeedback.setVisibility(View.VISIBLE);

            currentStepIdx++;
            checkAndPlayOpponentReply();
        } else {
            hadMistakeInChapter = true;
            tvFeedback.setText("✗ Incorrect: Try again! (Press HINT if stuck)");
            tvFeedback.setTextColor(Color.parseColor("#F87171"));
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
                }, 450);
            }
        }
    }

    private void advanceToNextStep() {
        if (courses.isEmpty()) return;
        OpeningModel.Course c = courses.get(currentCourseIdx);
        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);
        if (currentStepIdx >= ch.steps.size()) {
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
            finish(); // Return to Library
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
