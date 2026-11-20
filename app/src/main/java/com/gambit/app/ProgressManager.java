package com.gambit.app;

import android.content.Context;
import android.content.SharedPreferences;

public class ProgressManager {

    private static final String PREF_NAME = "gambit_progress";
    private static final String KEY_STREAK = "daily_streak";
    private static final String KEY_BEST_STREAK = "best_daily_streak";
    private static final String KEY_TOTAL_DAYS = "total_days_active";
    private static final String KEY_LAST_DAY = "last_active_day";
    private static final String KEY_FIRST_ACTIVE_TIMESTAMP = "first_active_timestamp";
    private static final String KEY_TOTAL_MOVES_TRAINED = "total_moves_trained";
    private static final String KEY_TOTAL_CHAPTERS_MASTERED = "total_chapters_mastered";

    private final SharedPreferences prefs;

    public ProgressManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        // If testing/fresh install where no moves were trained, ensure streak is 0
        if (prefs.getInt(KEY_TOTAL_MOVES_TRAINED, 0) == 0) {
            prefs.edit()
                 .putInt(KEY_STREAK, 0)
                 .putInt(KEY_BEST_STREAK, 0)
                 .putInt(KEY_TOTAL_DAYS, 0)
                 .putLong(KEY_LAST_DAY, 0)
                 .apply();
        }
        checkLapsedStreak();
    }

    private void checkLapsedStreak() {
        long currentDay = System.currentTimeMillis() / (1000 * 60 * 60 * 24);
        long lastDay = prefs.getLong(KEY_LAST_DAY, 0);

        if (lastDay == 0 || prefs.getInt(KEY_TOTAL_MOVES_TRAINED, 0) == 0) {
            return;
        }

        // Reset streak to 0 if more than 1 day passed without training
        if (currentDay > lastDay + 1) {
            prefs.edit().putInt(KEY_STREAK, 0).apply();
        }
    }

    private void recordDailyTrainingActivity() {
        long now = System.currentTimeMillis();
        long currentDay = now / (1000 * 60 * 60 * 24);
        long lastDay = prefs.getLong(KEY_LAST_DAY, 0);

        if (prefs.getLong(KEY_FIRST_ACTIVE_TIMESTAMP, 0) == 0) {
            prefs.edit().putLong(KEY_FIRST_ACTIVE_TIMESTAMP, now).apply();
        }

        if (lastDay == 0) {
            // First time training: Ignite streak at 1!
            prefs.edit()
                 .putLong(KEY_LAST_DAY, currentDay)
                 .putInt(KEY_STREAK, 1)
                 .putInt(KEY_BEST_STREAK, 1)
                 .putInt(KEY_TOTAL_DAYS, 1)
                 .apply();
        } else if (currentDay == lastDay) {
            // Already trained today: ensure streak is active (at least 1)
            int cur = Math.max(1, prefs.getInt(KEY_STREAK, 1));
            int best = Math.max(cur, prefs.getInt(KEY_BEST_STREAK, 1));
            prefs.edit().putInt(KEY_STREAK, cur).putInt(KEY_BEST_STREAK, best).apply();
        } else if (currentDay == lastDay + 1) {
            // Consecutive day training: Increment streak!
            int cur = prefs.getInt(KEY_STREAK, 0) + 1;
            int best = Math.max(cur, prefs.getInt(KEY_BEST_STREAK, 1));
            int total = prefs.getInt(KEY_TOTAL_DAYS, 1) + 1;
            prefs.edit()
                 .putLong(KEY_LAST_DAY, currentDay)
                 .putInt(KEY_STREAK, cur)
                 .putInt(KEY_BEST_STREAK, best)
                 .putInt(KEY_TOTAL_DAYS, total)
                 .apply();
        } else if (currentDay > lastDay + 1) {
            // Restarting streak after missed days
            int total = prefs.getInt(KEY_TOTAL_DAYS, 1) + 1;
            prefs.edit()
                 .putLong(KEY_LAST_DAY, currentDay)
                 .putInt(KEY_STREAK, 1)
                 .putInt(KEY_TOTAL_DAYS, total)
                 .apply();
        }
    }

    public int getStreak() {
        if (getTotalMovesTrained() == 0) return 0;
        return prefs.getInt(KEY_STREAK, 0);
    }

    public int getBestStreak() {
        if (getTotalMovesTrained() == 0) return 0;
        return prefs.getInt(KEY_BEST_STREAK, 0);
    }

    public int getTotalDaysActive() {
        if (getTotalMovesTrained() == 0) return 0;
        return prefs.getInt(KEY_TOTAL_DAYS, 0);
    }

    public void incrementMovesTrained() {
        int current = prefs.getInt(KEY_TOTAL_MOVES_TRAINED, 0);
        prefs.edit().putInt(KEY_TOTAL_MOVES_TRAINED, current + 1).apply();
        recordDailyTrainingActivity();
    }

    public int getTotalMovesTrained() {
        return prefs.getInt(KEY_TOTAL_MOVES_TRAINED, 0);
    }

    public void recordChapterAttempt(String chapterId, boolean passedWithoutError) {
        String keyAttempts = "ch_" + chapterId + "_attempts";
        String keySuccess = "ch_" + chapterId + "_success";
        String keyLevel = "ch_" + chapterId + "_mastery_level";

        int attempts = prefs.getInt(keyAttempts, 0) + 1;
        int successes = prefs.getInt(keySuccess, 0) + (passedWithoutError ? 1 : 0);
        int currentLevel = prefs.getInt(keyLevel, 0);

        if (passedWithoutError) {
            currentLevel = Math.min(5, currentLevel + 1);
        } else {
            currentLevel = Math.max(0, currentLevel - 1);
        }

        SharedPreferences.Editor ed = prefs.edit();
        ed.putInt(keyAttempts, attempts);
        ed.putInt(keySuccess, successes);
        ed.putInt(keyLevel, currentLevel);
        ed.putLong("ch_" + chapterId + "_last_trained", System.currentTimeMillis());

        if (currentLevel >= 3) {
            String masteredKey = "ch_" + chapterId + "_mastered";
            if (!prefs.getBoolean(masteredKey, false)) {
                ed.putBoolean(masteredKey, true);
                int masteredTotal = prefs.getInt(KEY_TOTAL_CHAPTERS_MASTERED, 0);
                ed.putInt(KEY_TOTAL_CHAPTERS_MASTERED, masteredTotal + 1);
            }
        }
        ed.apply();
        recordDailyTrainingActivity();
    }

    public int getChapterMasteryLevel(String chapterId) {
        return prefs.getInt("ch_" + chapterId + "_mastery_level", 0);
    }

    public int getChapterSuccessRate(String chapterId) {
        int attempts = prefs.getInt("ch_" + chapterId + "_attempts", 0);
        if (attempts == 0) return 0;
        int successes = prefs.getInt("ch_" + chapterId + "_success", 0);
        return (int) ((successes * 100.0f) / attempts);
    }

    public boolean isChapterCompleted(String chapterId) {
        if (chapterId == null) return false;
        return prefs.getInt("ch_" + chapterId + "_attempts", 0) > 0 ||
               prefs.getInt("ch_" + chapterId + "_mastery_level", 0) > 0;
    }

    public boolean isChapterUnlocked(OpeningModel.Course course, int chapterIndex) {
        if (chapterIndex <= 0) return true; // First variation is always unlocked
        if (course == null || course.chapters == null || chapterIndex >= course.chapters.size()) return false;
        OpeningModel.Chapter prevChapter = course.chapters.get(chapterIndex - 1);
        return isChapterCompleted(prevChapter.id);
    }

    public int getTotalMasteredChapters() {
        return prefs.getInt(KEY_TOTAL_CHAPTERS_MASTERED, 0);
    }
}
