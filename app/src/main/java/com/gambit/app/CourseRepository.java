package com.gambit.app;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CourseRepository {

    public static List<OpeningModel.Course> loadAllCourses(Context context) {
        List<OpeningModel.Course> list = new ArrayList<OpeningModel.Course>();
        try {
            String[] files = context.getAssets().list("courses");
            if (files != null) {
                for (String file : files) {
                    if (file.endsWith(".json")) {
                        OpeningModel.Course course = loadCourseFromAsset(context, "courses/" + file);
                        if (course != null) list.add(course);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Sort strictly by Difficulty Level (Level 1 -> Level 2 -> Level 3), then alphabetically
        Collections.sort(list, new Comparator<OpeningModel.Course>() {
            @Override
            public int compare(OpeningModel.Course a, OpeningModel.Course b) {
                int rA = getLevelRank(a.level);
                int rB = getLevelRank(b.level);
                if (rA != rB) {
                    return rA - rB;
                }
                return a.name.compareToIgnoreCase(b.name);
            }
        });

        return list;
    }

    private static int getLevelRank(String level) {
        if (level == null) return 99;
        String l = level.toLowerCase();
        if (l.contains("level 1") || l.contains("fundamentals")) return 1;
        if (l.contains("level 2") || l.contains("tactical")) return 2;
        if (l.contains("level 3") || l.contains("strategic")) return 3;
        return 4;
    }

    public static OpeningModel.Course loadCourseFromAsset(Context context, String assetPath) {
        try {
            InputStream is = context.getAssets().open(assetPath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONObject root = new JSONObject(json);
            OpeningModel.Course c = new OpeningModel.Course();
            c.id = root.optString("id");
            c.name = root.optString("name");
            c.color = root.optString("color");
            c.level = root.optString("level", "Level 1: Fundamentals");
            c.description = root.optString("description");

            JSONArray chapters = root.optJSONArray("chapters");
            if (chapters != null) {
                for (int i = 0; i < chapters.length(); i++) {
                    JSONObject chObj = chapters.getJSONObject(i);
                    OpeningModel.Chapter ch = new OpeningModel.Chapter();
                    ch.id = chObj.optString("id");
                    ch.title = chObj.optString("title");
                    ch.subtitle = chObj.optString("subtitle");
                    ch.orientation = chObj.optString("orientation", "white");

                    JSONArray steps = chObj.optJSONArray("steps");
                    if (steps != null) {
                        for (int j = 0; j < steps.length(); j++) {
                            JSONObject sObj = steps.getJSONObject(j);
                            OpeningModel.MoveStep step = new OpeningModel.MoveStep();
                            step.moveSan = sObj.optString("moveSan");
                            step.uci = sObj.optString("uci");
                            step.isUserMove = sObj.optBoolean("isUserMove", true);
                            step.title = sObj.optString("title");
                            step.explanation = sObj.optString("explanation");
                            step.warning = sObj.optString("warning", "");

                            JSONArray arrows = sObj.optJSONArray("arrows");
                            if (arrows != null) {
                                for (int a = 0; a < arrows.length(); a++) {
                                    JSONObject aObj = arrows.getJSONObject(a);
                                    step.arrows.add(new OpeningModel.Arrow(
                                            aObj.optString("from"),
                                            aObj.optString("to"),
                                            aObj.optString("color", "green")
                                    ));
                                }
                            }

                            JSONArray highlights = sObj.optJSONArray("highlightSquares");
                            if (highlights != null) {
                                for (int h = 0; h < highlights.length(); h++) {
                                    step.highlightSquares.add(highlights.getString(h));
                                }
                            }

                            ch.steps.add(step);
                        }
                    }
                    c.chapters.add(ch);
                }
            }
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
