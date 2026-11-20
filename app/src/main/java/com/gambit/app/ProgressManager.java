package com.gambit.app;

import android.content.Context;
import android.content.SharedPreferences;

public class ProgressManager {

    private static final String PREF_NAME = "gambit_progress";
    private static final String KEY_STREAK = "daily_streak";
    private static final String KEY_LAST_DAY = "last_active_day";
    private static final String KEY_TOTAL_MOVES_TRAINED = "total_moves_trained";
    private static final String KEY_TOTAL_CHAPTERS_MASTERED = "total_chapters_mastered";

    private final SharedPreferences prefs;

    public ProgressManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        updateStreakIfNeeded();
    }

    private void updateStreakIfNeeded() {
        long now = System.currentTimeMillis();
        long currentDay = now / (1000 * 60 * 60 * 24);
        long lastDay = prefs.getLong(KEY_LAST_DAY, 0);

        if (lastDay == 0) {
            prefs.edit()
                 .putLong(KEY_LAST_DAY, currentDay)
                 .putInt(KEY_STREAK, 1)
                 .apply();
        } else if (currentDay == lastDay + 1) {
            // Next consecutive day
            int currentStreak = prefs.getInt(KEY_STREAK, 0);
            prefs.edit()
                 .putLong(KEY_LAST_DAY, currentDay)
                 .putInt(KEY_STREAK, currentStreak + 1)
                 .apply();
        } else if (currentDay > lastDay + 1) {
            // Broken streak
            prefs.edit()
                 .putLong(KEY_LAST_DAY, currentDay)
                 .putInt(KEY_STREAK, 1)
                 .apply();
        }
    }

    public int getStreak() {
        return prefs.getInt(KEY_STREAK, 1);
    }

    public void incrementMovesTrained() {
        int current = prefs.getInt(KEY_TOTAL_MOVES_TRAINED, 0);
        prefs.edit().putInt(KEY_TOTAL_MOVES_TRAINED, current + 1).apply();
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

    public int getTotalMasteredChapters() {
        return prefs.getInt(KEY_TOTAL_CHAPTERS_MASTERED, 0);
    }
}
