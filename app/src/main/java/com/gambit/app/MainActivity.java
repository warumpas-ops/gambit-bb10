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

    // Phase 1 = Guided Study (arrows + explanations)
    // Phase 2 = Memory Drill (no arrows, play from memory)
    // Phase 3 = Cumulative Gauntlet (review previous lines from memory)
    private boolean isStudyPhase = true;
    private boolean isGauntletPhase = false;
    private int gauntletChapterIdx = -1;
    private boolean hadMistakeInChapter = false;

    // Option C: Sparring Gym Mode
    private boolean isSparringMode = false;
    private int sparringScore = 0;
    private int sparringStreak = 0;

    private OpeningModel.Chapter getActiveChapter() {
        OpeningModel.Course c = courses.get(currentCourseIdx);
        if (isGauntletPhase && gauntletChapterIdx >= 0 && gauntletChapterIdx < c.chapters.size()) {
            return c.chapters.get(gauntletChapterIdx);
        }
        return c.chapters.get(currentChapterIdx);
    }

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
                // Allow user to manually jump straight to drill or back to study
                if (isStudyPhase) {
                    // Skip to drill early
                    transitionToDrillPhase(true);
                } else {
                    // Jump back to study phase
                    isStudyPhase = true;
                    resetCurrentChapterInPhase();
                }
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

        isSparringMode = getIntent().getBooleanExtra("isSparringMode", false);
        if (isSparringMode) {
            btnModeQuick.setVisibility(View.GONE);
            startSparringRound();
        } else {
            btnModeQuick.setVisibility(View.VISIBLE);
            startChapter(initCourseIdx, initChapterIdx);
        }

        // Apply Space Grotesk font across MainActivity
        FontHelper.apply(this);
    }

    private void startChapter(int courseIdx, int chapterIdx) {
        if (courses.isEmpty()) return;
        currentCourseIdx = courseIdx % courses.size();
        OpeningModel.Course c = courses.get(currentCourseIdx);
        if (c.chapters.isEmpty()) return;
        currentChapterIdx = chapterIdx % c.chapters.size();

        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);
        boolean playAsBlack = "black".equalsIgnoreCase(c.color) || "black".equalsIgnoreCase(ch.orientation);
        boardView.setFlipped(playAsBlack);

        // Always begin a chapter in Study phase
        isStudyPhase = true;
        isGauntletPhase = false;
        hadMistakeInChapter = false;
        resetCurrentChapterInPhase();
    }

    // Reset board state and restart whichever phase is currently active
    private void resetCurrentChapterInPhase() {
        engine.resetToStartingPosition();
        currentStepIdx = 0;
        hadMistakeInChapter = false;
        boardView.setLastMove(-1, -1);
        boardView.clearSelection();

        llTopBar.setVisibility(View.GONE);
        llCompletionCard.setVisibility(View.GONE);
        llCoachPill.setVisibility(View.VISIBLE);
        flExplainOverlay.setVisibility(View.GONE);

        updateModeButton();
        renderCurrentStep();
        checkAndPlayOpponentReply();
    }

    // Legacy alias: a plain chapter reset always restarts from Study phase (or restarts sparring round)
    private void resetCurrentChapter() {
        if (isSparringMode) {
            startSparringRound();
            return;
        }
        isStudyPhase = true;
        isGauntletPhase = false;
        resetCurrentChapterInPhase();
    }

    private void updateModeButton() {
        if (isSparringMode) {
            btnModeQuick.setVisibility(View.GONE);
            return;
        }
        btnModeQuick.setVisibility(View.VISIBLE);
        if (isStudyPhase) {
            btnModeQuick.setText("[STUDY]");
            btnModeQuick.setTextColor(Color.parseColor("#10B981"));
        } else if (isGauntletPhase) {
            btnModeQuick.setText("[BOSS]");
            btnModeQuick.setTextColor(Color.parseColor("#EF4444"));
        } else {
            btnModeQuick.setText("[DRILL]");
            btnModeQuick.setTextColor(Color.parseColor("#F59E0B"));
        }
    }

    // Called when study phase finishes (all moves played once with arrows).
    private void transitionToDrillPhase(boolean earlySkip) {
        if (courses.isEmpty()) return;
        OpeningModel.Chapter ch = courses.get(currentCourseIdx).chapters.get(currentChapterIdx);
        progressManager.markChapterStudied(ch.id);

        isStudyPhase = false;
        isGauntletPhase = false;
        hadMistakeInChapter = false;

        engine.resetToStartingPosition();
        currentStepIdx = 0;
        boardView.setLastMove(-1, -1);
        boardView.clearSelection();

        llTopBar.setVisibility(View.GONE);
        llCompletionCard.setVisibility(View.GONE);
        llCoachPill.setVisibility(View.VISIBLE);
        flExplainOverlay.setVisibility(View.GONE);

        updateModeButton();

        // Punchy banner
        tvCoachBadge.setText("DRILL");
        tvCoachBadge.setTextColor(Color.parseColor("#F59E0B"));
        tvCoachPrompt.setText("Now prove it — play the line from memory! No hints.");
        boardView.setArrows(null);
        boardView.setHighlightSquares(null);
        boardView.invalidate();

        // Brief pause so user reads the banner before engine auto-plays first opponent move
        boardView.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAndPlayOpponentReply();
            }
        }, 1200);
    }

    // Option A: Cumulative Gauntlet Phase (reviews a prior unlocked line from memory)
    private void transitionToGauntletPhase() {
        if (courses.isEmpty()) return;
        OpeningModel.Course c = courses.get(currentCourseIdx);
        if (currentChapterIdx <= 0) {
            onDrillComplete();
            return;
        }

        // Pick one prior variation from this opening
        gauntletChapterIdx = (int) (Math.random() * currentChapterIdx);
        OpeningModel.Chapter reviewCh = c.chapters.get(gauntletChapterIdx);

        isStudyPhase = false;
        isGauntletPhase = true;
        hadMistakeInChapter = false;

        engine.resetToStartingPosition();
        currentStepIdx = 0;
        boardView.setLastMove(-1, -1);
        boardView.clearSelection();

        boolean playAsBlack = "black".equalsIgnoreCase(c.color) || "black".equalsIgnoreCase(reviewCh.orientation);
        boardView.setFlipped(playAsBlack);

        llTopBar.setVisibility(View.GONE);
        llCompletionCard.setVisibility(View.GONE);
        llCoachPill.setVisibility(View.VISIBLE);
        flExplainOverlay.setVisibility(View.GONE);

        updateModeButton();

        tvCoachBadge.setText("🥊 GAUNTLET");
        tvCoachBadge.setTextColor(Color.parseColor("#EF4444"));
        tvCoachPrompt.setText("Cumulative Review: Play " + reviewCh.title + " from memory!");
        boardView.setArrows(null);
        boardView.setHighlightSquares(null);
        boardView.invalidate();

        Toast.makeText(this, "🥊 Cumulative Gauntlet: Prove your earlier line!", Toast.LENGTH_SHORT).show();

        boardView.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAndPlayOpponentReply();
            }
        }, 1300);
    }

    private void onGauntletComplete() {
        isGauntletPhase = false;
        onDrillComplete();
    }

    // Option C: Sparring Round
    private void startSparringRound() {
        if (courses.isEmpty()) return;
        OpeningModel.Course c = courses.get(currentCourseIdx);

        List<Integer> unlockedList = new ArrayList<Integer>();
        for (int i = 0; i < c.chapters.size(); i++) {
            if (progressManager.isChapterUnlocked(c, i)) {
                unlockedList.add(i);
            }
        }
        if (unlockedList.isEmpty()) {
            unlockedList.add(0);
        }

        int pick = unlockedList.get((int) (Math.random() * unlockedList.size()));
        currentChapterIdx = pick;
        currentStepIdx = 0;
        hadMistakeInChapter = false;
        isStudyPhase = false;
        isGauntletPhase = false;

        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);
        boolean playAsBlack = "black".equalsIgnoreCase(c.color) || "black".equalsIgnoreCase(ch.orientation);
        boardView.setFlipped(playAsBlack);

        engine.resetToStartingPosition();
        boardView.setLastMove(-1, -1);
        boardView.clearSelection();

        llTopBar.setVisibility(View.VISIBLE);
        tvTopTitle.setText("⚔️ " + c.name);
        tvTopStreak.setText("Score: " + sparringScore + " (🔥 " + sparringStreak + ")");

        llCompletionCard.setVisibility(View.GONE);
        llCoachPill.setVisibility(View.VISIBLE);
        flExplainOverlay.setVisibility(View.GONE);

        tvCoachBadge.setText("⚔️ #" + (sparringScore + 1));
        tvCoachBadge.setTextColor(Color.parseColor("#647C64"));
        tvCoachPrompt.setText("Antidote: " + ch.title + " \u2014 Play from memory!");

        boardView.setArrows(null);
        boardView.setHighlightSquares(null);
        boardView.invalidate();

        boardView.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkAndPlayOpponentReply();
            }
        }, 600);
    }

    private void renderCurrentStep() {
        if (courses.isEmpty()) return;
        OpeningModel.Chapter ch = getActiveChapter();

        if (currentStepIdx >= ch.steps.size()) {
            if (isSparringMode) {
                sparringScore++;
                if (!hadMistakeInChapter) {
                    sparringStreak++;
                } else {
                    sparringStreak = 0;
                }
                tvTopStreak.setText("Score: " + sparringScore + " (🔥 " + sparringStreak + ")");
                Toast.makeText(this, "\u2b50 Line Cleared! Next challenge...", Toast.LENGTH_SHORT).show();

                boardView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startSparringRound();
                    }
                }, 1000);
                return;
            }

            if (isStudyPhase) {
                // ── Study phase done → switch to Drill ──────────────────
                transitionToDrillPhase(false);
            } else if (isGauntletPhase) {
                // ── Gauntlet review line done → unlock variation ─────────
                onGauntletComplete();
            } else {
                // ── Drill of current chapter done ───────────────────────
                if (currentChapterIdx > 0) {
                    // Trigger Option A Cumulative Gauntlet!
                    transitionToGauntletPhase();
                } else {
                    onDrillComplete();
                }
            }
            return;
        }

        OpeningModel.MoveStep step = ch.steps.get(currentStepIdx);

        if (!isStudyPhase) {
            if (isGauntletPhase) {
                tvCoachBadge.setText("🥊 GAUNTLET");
                tvCoachBadge.setTextColor(Color.parseColor("#EF4444"));
                tvCoachPrompt.setText("Line: " + ch.title + " \u2014 From memory!");
            } else if (isSparringMode) {
                tvCoachBadge.setText("⚔️ #" + (sparringScore + 1));
                tvCoachBadge.setTextColor(Color.parseColor("#647C64"));
                tvCoachPrompt.setText("Antidote: " + ch.title + " \u2014 Play!");
            } else {
                tvCoachBadge.setText("DRILL");
                tvCoachBadge.setTextColor(Color.parseColor("#F59E0B"));
                tvCoachPrompt.setText("From memory — no hints!");
            }
            btnWhyExplain.setText("\uD83D\uDCA1 HINT");
            boardView.setArrows(null);
            boardView.setHighlightSquares(null);
        } else {
            // STUDY phase: show arrows and explanations
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
        OpeningModel.Chapter ch = getActiveChapter();
        if (currentStepIdx >= ch.steps.size()) return;

        OpeningModel.MoveStep step = ch.steps.get(currentStepIdx);

        if (!isStudyPhase) {
            // In Drill / Gauntlet / Sparring mode: show arrow as hint but mark mistake
            hadMistakeInChapter = true;
            boardView.setArrows(step.arrows);
            boardView.setHighlightSquares(step.highlightSquares);
            boardView.invalidate();
            Toast.makeText(this, "Hint used — arrow shown!", Toast.LENGTH_SHORT).show();
            return;
        }

        // In Study phase, popup modal shows the "Why" and Warnings
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
        OpeningModel.Chapter ch = getActiveChapter();
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
            // Wrong move — always retry, never advance
            hadMistakeInChapter = true;
            if (isGauntletPhase) {
                Toast.makeText(this, "\u274C Gauntlet review mistake! Try again.", Toast.LENGTH_SHORT).show();
            } else if (!isStudyPhase) {
                Toast.makeText(this, "\u274C Wrong! Try again.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Incorrect move! Try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Called when the Drill phase finishes: records progress, shows completion card
    private void onDrillComplete() {
        OpeningModel.Course c = courses.get(currentCourseIdx);
        OpeningModel.Chapter ch = c.chapters.get(currentChapterIdx);

        boolean perfectDrill = !hadMistakeInChapter;
        progressManager.markChapterDrillPassed(ch.id);
        progressManager.recordChapterAttempt(ch.id, perfectDrill);

        llTopBar.setVisibility(View.VISIBLE);
        tvTopTitle.setText(c.name + " \u2014 " + ch.title);
        tvTopStreak.setText("\uD83D\uDD25 " + progressManager.getStreak() + "d");

        llCoachPill.setVisibility(View.GONE);
        llCompletionCard.setVisibility(View.VISIBLE);

        int lvl = progressManager.getChapterMasteryLevel(ch.id);
        StringBuilder stars = new StringBuilder();
        for (int s = 0; s < 5; s++) stars.append(s < lvl ? "\u2605" : "\u2606");

        int rate = progressManager.getChapterSuccessRate(ch.id);
        if (currentChapterIdx > 0) {
            tvCompletionTitle.setText("🏆 CUMULATIVE GAUNTLET CLEARED!");
            tvCompletionStats.setText(ch.title + " & Review passed! \u2022 Mastery: " + stars.toString() + " \u2022 Moves: " + progressManager.getTotalMovesTrained());
        } else {
            String drillResult = perfectDrill ? "\u2b50 PERFECT DRILL!" : "\u2705 DRILL PASSED";
            tvCompletionTitle.setText(drillResult + " \u2014 " + ch.title.toUpperCase());
            tvCompletionStats.setText("Mastery: " + stars.toString() + " \u2022 Accuracy: " + rate + "% \u2022 Moves: " + progressManager.getTotalMovesTrained());
        }

        // Label next-line button appropriately
        if (currentChapterIdx + 1 < c.chapters.size()) {
            btnNextLine.setText("START NEXT VARIATION \u2794");
        } else {
            btnNextLine.setText("NEXT OPENING \u2794");
        }

        boardView.setArrows(null);
        boardView.setHighlightSquares(null);
        boardView.invalidate();
    }

    private void checkAndPlayOpponentReply() {
        renderCurrentStep();

        OpeningModel.Chapter ch = getActiveChapter();

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
        } else if (keyCode == KeyEvent.KEYCODE_C) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
