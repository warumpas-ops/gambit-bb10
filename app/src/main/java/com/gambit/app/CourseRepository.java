package com.gambit.app;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
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
        return list;
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
