package com.gambit.app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StreakDialog {

    public static void show(Context context, ProgressManager pm) {
        if (context == null || pm == null) return;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#121216"));
        layout.setPadding(20, 20, 20, 20);

        // Header
        TextView tvHeader = new TextView(context);
        tvHeader.setText("\uD83D\uDD25 CHESS GRIND STATS");
        tvHeader.setTextColor(Color.WHITE);
        tvHeader.setTextSize(16f);
        tvHeader.setGravity(Gravity.CENTER);
        tvHeader.getPaint().setFakeBoldText(true);
        tvHeader.setPadding(0, 4, 0, 14);
        layout.addView(tvHeader);

        // Big Streak Banner
        LinearLayout banner = new LinearLayout(context);
        banner.setOrientation(LinearLayout.VERTICAL);
        banner.setBackgroundColor(Color.parseColor("#1F1F28"));
        banner.setPadding(16, 12, 16, 12);
        banner.setGravity(Gravity.CENTER);

        TextView tvBigStreak = new TextView(context);
        tvBigStreak.setText("\uD83D\uDD25 " + pm.getStreak() + " DAYS");
        tvBigStreak.setTextColor(Color.parseColor("#F59E0B")); // Amber Gold
        tvBigStreak.setTextSize(24f);
        tvBigStreak.getPaint().setFakeBoldText(true);
        tvBigStreak.setGravity(Gravity.CENTER);
        banner.addView(tvBigStreak);

        TextView tvStreakSub = new TextView(context);
        tvStreakSub.setText("CURRENT ACTIVE STREAK");
        tvStreakSub.setTextColor(Color.parseColor("#9CA3AF"));
        tvStreakSub.setTextSize(10f);
        tvStreakSub.setGravity(Gravity.CENTER);
        banner.addView(tvStreakSub);

        layout.addView(banner);

        // Detailed Stats Rows Container
        LinearLayout statsContainer = new LinearLayout(context);
        statsContainer.setOrientation(LinearLayout.VERTICAL);
        statsContainer.setPadding(4, 12, 4, 12);

        addStatRow(context, statsContainer, "Personal Best Streak", pm.getBestStreak() + " days");
        addStatRow(context, statsContainer, "Total Days Grinding", pm.getTotalDaysActive() + " days");
        addStatRow(context, statsContainer, "Total Moves Trained", pm.getTotalMovesTrained() + " moves");
        addStatRow(context, statsContainer, "Chapters Mastered", pm.getTotalMasteredChapters() + " / 40");

        layout.addView(statsContainer);

        // Motivational Quote / Tier
        TextView tvQuote = new TextView(context);
        int streak = pm.getStreak();
        String quote;
        if (streak >= 30) {
            quote = "\"Grandmaster Discipline \u2014 Unstoppable Daily Focus!\"";
        } else if (streak >= 14) {
            quote = "\"Tactical Prodigy \u2014 Two Full Weeks of Relentless Study!\"";
        } else if (streak >= 7) {
            quote = "\"Opening Specialist \u2014 One Solid Week of Daily Chess!\"";
        } else if (streak >= 3) {
            quote = "\"Building Momentum \u2014 Daily Consistency is the Secret!\"";
        } else {
            quote = "\"The Grind Begins \u2014 Train Daily to Forge Muscle Memory!\"";
        }
        tvQuote.setText(quote);
        tvQuote.setTextColor(Color.parseColor("#10B981")); // Emerald Green
        tvQuote.setTextSize(11f);
        tvQuote.setGravity(Gravity.CENTER);
        tvQuote.setPadding(8, 4, 8, 16);
        layout.addView(tvQuote);

        // Dismiss Button
        Button btnClose = new Button(context);
        btnClose.setText("KEEP GRINDING");
        btnClose.setTextColor(Color.WHITE);
        btnClose.setBackgroundColor(Color.parseColor("#2563EB")); // Deep Royal Blue
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layout.addView(btnClose);

        // Apply Space Grotesk Font to entire modal!
        FontHelper.apply(layout, context);

        dialog.setContentView(layout);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
    }

    private static void addStatRow(Context context, LinearLayout container, String label, String value) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(4, 4, 4, 4);

        TextView tvLabel = new TextView(context);
        tvLabel.setText(label);
        tvLabel.setTextColor(Color.parseColor("#94A3B8"));
        tvLabel.setTextSize(12f);
        LinearLayout.LayoutParams lpLabel = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        tvLabel.setLayoutParams(lpLabel);
        row.addView(tvLabel);

        TextView tvVal = new TextView(context);
        tvVal.setText(value);
        tvVal.setTextColor(Color.WHITE);
        tvVal.setTextSize(12f);
        tvVal.getPaint().setFakeBoldText(true);
        row.addView(tvVal);

        container.addView(row);
    }
}
