package com.gambit.app;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements ChessBoardView.OnMoveAttemptListener {

    private ChessEngine engine;
    private ChessBoardView boardView;
    private ProgressManager progressManager;

    // Top Bar (hidden during training, visible when finished)
    private LinearLayout llTopBar;
    private TextView tvTopTitle;
    private TextView tvTopStreak;
    private Button btnBackToLibrary;

    // Floating Coach Pill (Under Board)
    private LinearLayout llCoachPill;
    private TextView tvCoachBadge;
    private TextView tvCoachPrompt;
    private Button btnWhyExplain;
    private Button btnModeQuick;

    // Overlay Card for Explanations
    private FrameLayout flExplainOverlay;
    private TextView tvOverlayBadge;
    private TextView tvOverlayTitle;
    private TextView tvOverlayBody;
    private TextView tvOverlayWarning;
    private TextView btnCloseOverlay;
    private Button btnGotIt;

    // Completion Card
    private LinearLayout llCompletionCard;
    private TextView tvCompletionTitle;
    private TextView tvCompletionStats;
    private Button btnDrillAgain;
    private Button btnNextLine;

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

        // Top Bar
        llTopBar = (LinearLayout) findViewById(R.id.llTopBar);
        tvTopTitle = (TextView) findViewById(R.id.tvTopTitle);
        tvTopStreak = (TextView) findViewById(R.id.tvTopStreak);
        btnBackToLibrary = (Button) findViewById(R.id.btnBackToLibrary);

        // Coach Pill
        llCoachPill = (LinearLayout) findViewById(R.id.llCoachPill);
        tvCoachBadge = (TextView) findViewById(R.id.tvCoachBadge);
        tvCoachPrompt = (TextView) findViewById(R.id.tvCoachPrompt);
        btnWhyExplain = (Button) findViewById(R.id.btnWhyExplain);
        btnModeQuick = (Button) findViewById(R.id.btnModeQuick);

        // Overlay Modal
        flExplainOverlay = (FrameLayout) findViewById(R.id.flExplainOverlay);
        tvOverlayBadge = (TextView) findViewById(R.id.tvOverlayBadge);
        tvOverlayTitle = (TextView) findViewById(R.id.tvOverlayTitle);
        tvOverlayBody = (TextView) findViewById(R.id.tvOverlayBody);
        tvOverlayWarning = (TextView) findViewById(R.id.tvOverlayWarning);
        btnCloseOverlay = (TextView) findViewById(R.id.btnCloseOverlay);
        btnGotIt = (Button) findViewById(R.id.btnGotIt);

        // Completion Card
        llCompletionCard = (LinearLayout) findViewById(R.id.llCompletionCard);
        tvCompletionTitle = (TextView) findViewById(R.id.tvCompletionTitle);
        tvCompletionStats = (TextView) findViewById(R.id.tvCompletionStats);
        btnDrillAgain = (Button) findViewById(R.id.btnDrillAgain);
        btnNextLine = (Button) findViewById(R.id.btnNextLine);

        courses = CourseRepository.loadAllCourses(this);

        int initCourseIdx = getIntent().getIntExtra("courseIdx", 0);
        int initChapterIdx = getIntent().getIntExtra("chapterIdx", 0);

        btnBackToLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnModeQuick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMode();
            }
        });

        btnWhyExplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWhyDialog();
            }
        });

        View.OnClickListener closeOverlayListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flExplainOverlay.setVisibility(View.GONE);
            }
        };
        btnCloseOverlay.setOnClickListener(closeOverlayListener);
        btnGotIt.setOnClickListener(closeOverlayListener);
        flExplainOverlay.setOnClickListener(closeOverlayListener);

        btnDrillAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCurrentChapter();
            }
        });

        btnNextLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceToNextChapter();
            }
        });

        // Clickable Streak Badge in Top Bar
        tvTopStreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StreakDialog.show(MainActivity.this, progressManager);
            }
        });

        startChapter(initCourseIdx, initChapterIdx);

        // Apply Space Grotesk font across MainActivity
        FontHelper.apply(this);
    }

    private void toggleMode() {
        isDrillMode = !isDrillMode;
        if (isDrillMode) {
            btnModeQuick.setText("[DRILL]");
            btnModeQuick.setTextColor(Color.parseColor("#F59E0B"));
            Toast.makeText(this, "Drill Mode: Recall moves from memory!", Toast.LENGTH_SHORT).show();
        } else {
            btnModeQuick.setText("[STUDY]");
            btnModeQuick.setTextColor(Color.parseColor("#10B981"));
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

        // While training: HIDE TOP BAR, MAXIMIZE BOARD, HIDE COMPLETION CARD
        llTopBar.setVisibility(View.GONE);
        llCompletionCard.setVisibility(View.GONE);
        llCoachPill.setVisibility(View.VISIBLE);
        flExplainOverlay.setVisibility(View.GONE);

        renderCurrentStep();
        checkAndPlayOpponentReply();
    }

    private void renderCurrentStep() {
        if (courses.isEmpty()) return;
        OpeningModel.Course c = courses.get(currentCourseIdx);
        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);

        if (currentStepIdx >= ch.steps.size()) {
            // CHAPTER COMPLETED: Show Top Bar, Show Completion Card!
            progressManager.recordChapterAttempt(ch.id, !hadMistakeInChapter);

            llTopBar.setVisibility(View.VISIBLE);
            tvTopTitle.setText(c.name + " (" + ch.title + ")");
            tvTopStreak.setText("\uD83D\uDD25 " + progressManager.getStreak() + "d");

            llCoachPill.setVisibility(View.GONE);
            llCompletionCard.setVisibility(View.VISIBLE);

            int lvl = progressManager.getChapterMasteryLevel(ch.id);
            StringBuilder stars = new StringBuilder();
            for (int s = 0; s < 5; s++) stars.append(s < lvl ? "\u2605" : "\u2606");

            int rate = progressManager.getChapterSuccessRate(ch.id);
            tvCompletionTitle.setText("\u2605 " + ch.title.toUpperCase() + " COMPLETE!");
            tvCompletionStats.setText("Mastery: " + stars.toString() + " \u2022 Accuracy: " + rate + "% \u2022 Total Moves: " + progressManager.getTotalMovesTrained());

            if (currentChapterIdx + 1 < c.chapters.size()) {
                btnNextLine.setText("NEXT VARIATION \u2794");
            } else {
                btnNextLine.setText("NEXT OPENING \u2794");
            }

            boardView.setArrows(null);
            boardView.setHighlightSquares(null);
            return;
        }

        OpeningModel.MoveStep step = ch.steps.get(currentStepIdx);

        if (isDrillMode) {
            tvCoachBadge.setText("? ? ?");
            tvCoachBadge.setTextColor(Color.parseColor("#F59E0B"));
            tvCoachPrompt.setText("Your Turn \u2014 Play from memory");
            btnWhyExplain.setText("\uD83D\uDCA1 HINT");
            boardView.setArrows(null);
            boardView.setHighlightSquares(null);
        } else {
            tvCoachBadge.setText(step.moveSan);
            tvCoachBadge.setTextColor(Color.parseColor("#10B981"));
            tvCoachPrompt.setText(step.title);
            btnWhyExplain.setText("\uD83D\uDCA1 WHY?");
            boardView.setArrows(step.arrows);
            boardView.setHighlightSquares(step.highlightSquares);
        }

        boardView.invalidate();
    }

    private void showWhyDialog() {
        if (courses.isEmpty()) return;
        OpeningModel.Course c = courses.get(currentCourseIdx);
        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);
        if (currentStepIdx >= ch.steps.size()) return;

        OpeningModel.MoveStep step = ch.steps.get(currentStepIdx);

        if (isDrillMode) {
            // In Drill Mode, button gives hint arrows
            boardView.setArrows(step.arrows);
            boardView.setHighlightSquares(step.highlightSquares);
            boardView.invalidate();
            Toast.makeText(this, "Hint: Arrow shown on board!", Toast.LENGTH_SHORT).show();
            return;
        }

        // In Study Mode, popup modal shows the "Why" and Warnings
        tvOverlayBadge.setText(step.moveSan);
        tvOverlayTitle.setText(step.title);
        tvOverlayBody.setText(step.explanation);

        if (step.warning != null && !step.warning.isEmpty()) {
            tvOverlayWarning.setText(step.warning);
            tvOverlayWarning.setVisibility(View.VISIBLE);
        } else {
            tvOverlayWarning.setVisibility(View.GONE);
        }

        flExplainOverlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMoveAttempt(int fromSq, int toSq, String uci) {
        if (courses.isEmpty()) return;
        OpeningModel.Course c = courses.get(currentCourseIdx);
        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);
        if (currentStepIdx >= ch.steps.size()) return;

        OpeningModel.MoveStep expected = ch.steps.get(currentStepIdx);

        boolean isMatch = uci.equalsIgnoreCase(expected.uci) ||
                (expected.uci.length() > 4 && expected.uci.substring(0, 4).equalsIgnoreCase(uci));

        if (isMatch) {
            // Correct Move!
            char promo = expected.uci.length() > 4 ? expected.uci.charAt(4) : 0;
            engine.makeMove(fromSq, toSq, promo);
            boardView.setLastMove(fromSq, toSq);

            progressManager.incrementMovesTrained();

            currentStepIdx++;
            checkAndPlayOpponentReply();
        } else {
            hadMistakeInChapter = true;
            Toast.makeText(this, "Incorrect move! Try again.", Toast.LENGTH_SHORT).show();
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
                }, 400);
            }
        }
    }

    private void advanceToNextChapter() {
        OpeningModel.Course c = courses.get(currentCourseIdx);
        if (currentChapterIdx + 1 < c.chapters.size()) {
            startChapter(currentCourseIdx, currentChapterIdx + 1);
        } else {
            // Advance to next course of same color (White stays White, Black stays Black)
            int next = (currentCourseIdx + 1) % courses.size();
            for (int i = 0; i < courses.size(); i++) {
                int candidate = (currentCourseIdx + 1 + i) % courses.size();
                if (courses.get(candidate).color.equalsIgnoreCase(c.color)) {
                    next = candidate;
                    break;
                }
            }
            startChapter(next, 0);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_R) {
            resetCurrentChapter();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_H || keyCode == KeyEvent.KEYCODE_W) {
            showWhyDialog();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_M) {
            toggleMode();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_C) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
