package com.gambit.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class HubActivity extends Activity {

    private ProgressManager progressManager;
    private List<OpeningModel.Course> courses;
    private LinearLayout container;
    private TextView tvHubStreak;
    private Button btnTabWhite;
    private Button btnTabBlack;

    private boolean showWhite = true; // true = White, false = Black

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

        updateTabs();
        renderCourses();
    }

    private void updateTabs() {
        if (showWhite) {
            btnTabWhite.setBackgroundColor(Color.parseColor("#2563EB")); // Active Blue
            btnTabWhite.setTextColor(Color.WHITE);
            btnTabBlack.setBackgroundColor(Color.parseColor("#18181E")); // Inactive
            btnTabBlack.setTextColor(Color.parseColor("#9CA3AF"));
        } else {
            btnTabBlack.setBackgroundColor(Color.parseColor("#2563EB")); // Active Blue
            btnTabBlack.setTextColor(Color.WHITE);
            btnTabWhite.setBackgroundColor(Color.parseColor("#18181E")); // Inactive
            btnTabWhite.setTextColor(Color.parseColor("#9CA3AF"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvHubStreak.setText("🔥 " + progressManager.getStreak() + "d");
        renderCourses();
    }

    private void renderCourses() {
        container.removeAllViews();

        for (int cIdx = 0; cIdx < courses.size(); cIdx++) {
            final int courseIdx = cIdx;
            final OpeningModel.Course course = courses.get(courseIdx);

            boolean isWhiteCourse = "white".equalsIgnoreCase(course.color);
            if (isWhiteCourse != showWhite) {
                continue; // Filter by chosen tab!
            }

            // Level / Category Header
            TextView tvLevel = new TextView(this);
            tvLevel.setText(course.level.toUpperCase());
            tvLevel.setTextColor(Color.parseColor("#94A3B8"));
            tvLevel.setTextSize(11f);
            tvLevel.setPadding(4, 12, 4, 4);
            container.addView(tvLevel);

            // Course Box Container
            LinearLayout courseCard = new LinearLayout(this);
            courseCard.setOrientation(LinearLayout.VERTICAL);
            courseCard.setBackgroundColor(Color.parseColor("#121216"));
            courseCard.setPadding(12, 12, 12, 12);

            TextView tvName = new TextView(this);
            tvName.setText((isWhiteCourse ? "⚪ " : "⚫ ") + course.name);
            tvName.setTextColor(Color.WHITE);
            tvName.setTextSize(15f);
            tvName.getPaint().setFakeBoldText(true);
            courseCard.addView(tvName);

            TextView tvDesc = new TextView(this);
            tvDesc.setText(course.description);
            tvDesc.setTextColor(Color.parseColor("#9CA3AF"));
            tvDesc.setTextSize(11f);
            tvDesc.setPadding(0, 2, 0, 8);
            courseCard.addView(tvDesc);

            // Chapters List
            for (int chIdx = 0; chIdx < course.chapters.size(); chIdx++) {
                final int chapterIdx = chIdx;
                final OpeningModel.Chapter chapter = course.chapters.get(chapterIdx);

                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setBackgroundColor(Color.parseColor("#1A1A22"));
                row.setPadding(10, 8, 10, 8);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 4, 0, 4);
                row.setLayoutParams(lp);

                LinearLayout textCol = new LinearLayout(this);
                textCol.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams colLp = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                textCol.setLayoutParams(colLp);

                TextView tvChTitle = new TextView(this);
                tvChTitle.setText(chapter.title);
                tvChTitle.setTextColor(Color.parseColor("#F3F4F6"));
                tvChTitle.setTextSize(12f);
                tvChTitle.getPaint().setFakeBoldText(true);
                textCol.addView(tvChTitle);

                TextView tvChSubtitle = new TextView(this);
                tvChSubtitle.setText(chapter.subtitle);
                tvChSubtitle.setTextColor(Color.parseColor("#64748B"));
                tvChSubtitle.setTextSize(10f);
                textCol.addView(tvChSubtitle);

                row.addView(textCol);

                int level = progressManager.getChapterMasteryLevel(chapter.id);
                StringBuilder stars = new StringBuilder();
                for (int s = 0; s < 5; s++) stars.append(s < level ? "★" : "☆");

                TextView tvStars = new TextView(this);
                tvStars.setText(stars.toString());
                tvStars.setTextColor(Color.parseColor("#FBBF24"));
                tvStars.setTextSize(12f);
                row.addView(tvStars);

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HubActivity.this, MainActivity.class);
                        intent.putExtra("courseIdx", courseIdx);
                        intent.putExtra("chapterIdx", chapterIdx);
                        startActivity(intent);
                    }
                });

                courseCard.addView(row);
            }

            container.addView(courseCard);
        }
    }
}
