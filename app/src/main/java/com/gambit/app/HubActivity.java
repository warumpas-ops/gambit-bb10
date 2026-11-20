package com.gambit.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HubActivity extends Activity {

    private ProgressManager progressManager;
    private List<OpeningModel.Course> courses;
    private LinearLayout container;
    private TextView tvHubStreak;
    private Button btnTabWhite;
    private Button btnTabBlack;

    private boolean showWhite = true; // true = White, false = Black
    private final Set<String> expandedCourseIds = new HashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        progressManager = new ProgressManager(this);
        container = (LinearLayout) findViewById(R.id.llCoursesContainer);
        tvHubStreak = (TextView) findViewById(R.id.tvHubStreak);
        btnTabWhite = (Button) findViewById(R.id.btnTabWhite);
        btnTabBlack = (Button) findViewById(R.id.btnTabBlack);

        courses = CourseRepository.loadAllCourses(this);

        btnTabWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!showWhite) {
                    showWhite = true;
                    updateTabs();
                    renderCourses();
                }
            }
        });

        btnTabBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showWhite) {
                    showWhite = false;
                    updateTabs();
                    renderCourses();
                }
            }
        });

        // Clickable Streak Badge -> opens Grind Stats Modal!
        tvHubStreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StreakDialog.show(HubActivity.this, progressManager);
            }
        });

        updateTabs();
        renderCourses();

        // Apply Space Grotesk font across the entire Hub UI
        FontHelper.apply(this);
    }

    private void updateTabs() {
        if (showWhite) {
            btnTabWhite.setBackgroundColor(Color.parseColor("#647C64")); // Active Sage Green
            btnTabWhite.setTextColor(Color.WHITE);
            btnTabBlack.setBackgroundColor(Color.parseColor("#18181E")); // Inactive
            btnTabBlack.setTextColor(Color.parseColor("#9CA3AF"));
        } else {
            btnTabBlack.setBackgroundColor(Color.parseColor("#647C64")); // Active Sage Green
            btnTabBlack.setTextColor(Color.WHITE);
            btnTabWhite.setBackgroundColor(Color.parseColor("#18181E")); // Inactive
            btnTabWhite.setTextColor(Color.parseColor("#9CA3AF"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvHubStreak.setText("\uD83D\uDD25 " + progressManager.getStreak() + "d");
        renderCourses();
        FontHelper.apply(this);
    }

    private void renderCourses() {
        container.removeAllViews();
        String lastLevel = "";

        for (int cIdx = 0; cIdx < courses.size(); cIdx++) {
            final int courseIdx = cIdx;
            final OpeningModel.Course course = courses.get(courseIdx);

            boolean isWhiteCourse = "white".equalsIgnoreCase(course.color);
            if (isWhiteCourse != showWhite) {
                continue; // Filter by chosen tab!
            }

            // Level / Category Header (grouped without repeating)
            if (course.level != null && !course.level.equalsIgnoreCase(lastLevel)) {
                lastLevel = course.level;
                TextView tvLevel = new TextView(this);
                tvLevel.setText(course.level.toUpperCase());
                tvLevel.setTextColor(Color.parseColor("#94A3B8"));
                tvLevel.setTextSize(11f);
                tvLevel.setPadding(4, 16, 4, 4);
                container.addView(tvLevel);
            }

            // Stats for summary badge
            final int totalVariations = course.chapters.size();
            int totalStars = 0;
            int completedCount = 0;
            for (int i = 0; i < totalVariations; i++) {
                OpeningModel.Chapter ch = course.chapters.get(i);
                totalStars += progressManager.getChapterMasteryLevel(ch.id);
                if (progressManager.isChapterCompleted(ch.id)) completedCount++;
            }
            int maxStars = totalVariations * 5;

            final boolean isExpanded = expandedCourseIds.contains(course.id);

            // Course Outer Box Container
            LinearLayout courseCard = new LinearLayout(this);
            courseCard.setOrientation(LinearLayout.VERTICAL);
            courseCard.setBackgroundColor(Color.parseColor("#121216"));
            LinearLayout.LayoutParams cardLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            cardLp.setMargins(0, 4, 0, 8);
            courseCard.setLayoutParams(cardLp);

            // 1. Collapsible Title Header Card (Always Visible)
            LinearLayout headerRow = new LinearLayout(this);
            headerRow.setOrientation(LinearLayout.HORIZONTAL);
            headerRow.setBackgroundColor(Color.parseColor("#181822"));
            headerRow.setPadding(12, 10, 12, 10);
            headerRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
            LinearLayout.LayoutParams headerLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            headerRow.setLayoutParams(headerLp);

            LinearLayout headerTextCol = new LinearLayout(this);
            headerTextCol.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams headerColLp = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            headerTextCol.setLayoutParams(headerColLp);

            TextView tvName = new TextView(this);
            tvName.setText((isWhiteCourse ? "\u26AA " : "\u26AB ") + course.name);
            tvName.setTextColor(Color.WHITE);
            tvName.setTextSize(14f);
            tvName.getPaint().setFakeBoldText(true);
            headerTextCol.addView(tvName);

            TextView tvSummary = new TextView(this);
            String summary = totalVariations + " Variations \u2022 " + totalStars + "/" + maxStars + " \u2605";
            if (completedCount >= totalVariations) {
                summary += " \u2022 \u2714 Mastered";
            }
            tvSummary.setText(summary);
            tvSummary.setTextColor(Color.parseColor("#94A3B8"));
            tvSummary.setTextSize(11f);
            tvSummary.setPadding(0, 2, 0, 0);
            headerTextCol.addView(tvSummary);

            headerRow.addView(headerTextCol);

            final TextView tvChevron = new TextView(this);
            tvChevron.setText(isExpanded ? "\u25BC" : "\u25B6");
            tvChevron.setTextColor(Color.parseColor("#647C64"));
            tvChevron.setTextSize(13f);
            tvChevron.getPaint().setFakeBoldText(true);
            tvChevron.setPadding(8, 0, 4, 0);
            headerRow.addView(tvChevron);

            courseCard.addView(headerRow);

            // 2. Expandable Content Container (Description + Sequential Variations)
            final LinearLayout contentContainer = new LinearLayout(this);
            contentContainer.setOrientation(LinearLayout.VERTICAL);
            contentContainer.setBackgroundColor(Color.parseColor("#101016"));
            contentContainer.setPadding(10, 6, 10, 10);
            contentContainer.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            TextView tvDesc = new TextView(this);
            tvDesc.setText(course.description);
            tvDesc.setTextColor(Color.parseColor("#9CA3AF"));
            tvDesc.setTextSize(11f);
            tvDesc.setPadding(2, 4, 2, 8);
            contentContainer.addView(tvDesc);

            // Variations List with Sequential Progression
            for (int chIdx = 0; chIdx < course.chapters.size(); chIdx++) {
                final int chapterIdx = chIdx;
                final OpeningModel.Chapter chapter = course.chapters.get(chapterIdx);
                final boolean isUnlocked = progressManager.isChapterUnlocked(course, chapterIdx);

                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setGravity(android.view.Gravity.CENTER_VERTICAL);
                row.setPadding(10, 8, 10, 8);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 3, 0, 3);
                row.setLayoutParams(lp);

                LinearLayout textCol = new LinearLayout(this);
                textCol.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams colLp = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                textCol.setLayoutParams(colLp);

                if (isUnlocked) {
                    row.setBackgroundColor(Color.parseColor("#1E1E2C"));

                    TextView tvChTitle = new TextView(this);
                    tvChTitle.setText((chapterIdx + 1) + ". " + chapter.title);
                    tvChTitle.setTextColor(Color.parseColor("#F3F4F6"));
                    tvChTitle.setTextSize(12f);
                    tvChTitle.getPaint().setFakeBoldText(true);
                    textCol.addView(tvChTitle);

                    TextView tvChSubtitle = new TextView(this);
                    tvChSubtitle.setText(chapter.subtitle);
                    tvChSubtitle.setTextColor(Color.parseColor("#94A3B8"));
                    tvChSubtitle.setTextSize(10f);
                    textCol.addView(tvChSubtitle);

                    row.addView(textCol);

                    int level = progressManager.getChapterMasteryLevel(chapter.id);
                    StringBuilder stars = new StringBuilder();
                    for (int s = 0; s < 5; s++) stars.append(s < level ? "\u2605" : "\u2606");

                    TextView tvStars = new TextView(this);
                    tvStars.setText(stars.toString());
                    tvStars.setTextColor(Color.parseColor("#FBBF24"));
                    tvStars.setTextSize(12f);
                    row.addView(tvStars);

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            expandedCourseIds.add(course.id);
                            Intent intent = new Intent(HubActivity.this, MainActivity.class);
                            intent.putExtra("courseIdx", courseIdx);
                            intent.putExtra("chapterIdx", chapterIdx);
                            startActivity(intent);
                        }
                    });
                } else {
                    // Locked Variation: Greyed out with lock badge
                    row.setBackgroundColor(Color.parseColor("#14141A"));

                    TextView tvChTitle = new TextView(this);
                    tvChTitle.setText("\uD83D\uDD12 " + (chapterIdx + 1) + ". " + chapter.title);
                    tvChTitle.setTextColor(Color.parseColor("#64748B"));
                    tvChTitle.setTextSize(12f);
                    textCol.addView(tvChTitle);

                    TextView tvChSubtitle = new TextView(this);
                    OpeningModel.Chapter prevChapter = course.chapters.get(chapterIdx - 1);
                    tvChSubtitle.setText("Locked \u2014 Complete Chapter " + chapterIdx + " (" + prevChapter.title + ") first");
                    tvChSubtitle.setTextColor(Color.parseColor("#475569"));
                    tvChSubtitle.setTextSize(10f);
                    textCol.addView(tvChSubtitle);

                    row.addView(textCol);

                    TextView tvLockBadge = new TextView(this);
                    tvLockBadge.setText("\uD83D\uDD12 LOCKED");
                    tvLockBadge.setTextColor(Color.parseColor("#475569"));
                    tvLockBadge.setTextSize(10f);
                    tvLockBadge.getPaint().setFakeBoldText(true);
                    row.addView(tvLockBadge);

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OpeningModel.Chapter prev = course.chapters.get(chapterIdx - 1);
                            Toast.makeText(HubActivity.this, "\uD83D\uDD12 Complete " + prev.title + " first!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                contentContainer.addView(row);
            }

            courseCard.addView(contentContainer);

            // Tap header row to toggle expand/collapse
            headerRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandedCourseIds.contains(course.id)) {
                        expandedCourseIds.remove(course.id);
                        contentContainer.setVisibility(View.GONE);
                        tvChevron.setText("\u25B6");
                    } else {
                        expandedCourseIds.add(course.id);
                        contentContainer.setVisibility(View.VISIBLE);
                        tvChevron.setText("\u25BC");
                        FontHelper.apply(contentContainer, HubActivity.this);
                    }
                }
            });

            container.addView(courseCard);
        }

        // Apply Space Grotesk font to dynamically created course views
        FontHelper.apply(container, this);
    }
}
