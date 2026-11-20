package com.gambit.app;

import java.util.ArrayList;
import java.util.List;

public class OpeningModel {

    public static class Arrow {
        public String from;
        public String to;
        public String color;

        public Arrow(String from, String to, String color) {
            this.from = from;
            this.to = to;
            this.color = color;
        }
    }

    public static class MoveStep {
        public String moveSan;
        public String uci;
        public boolean isUserMove;
        public String title;
        public String explanation;
        public String warning;
        public List<Arrow> arrows = new ArrayList<Arrow>();
        public List<String> highlightSquares = new ArrayList<String>();
    }

    public static class Chapter {
        public String id;
        public String title;
        public String subtitle;
        public String orientation; // "white" or "black"
        public List<MoveStep> steps = new ArrayList<MoveStep>();
    }

    public static class Course {
        public String id;
        public String name;
        public String color; // "White" or "Black"
        public String level; // e.g. "Level 1: Beginner Essentials"
        public String description;
        public List<Chapter> chapters = new ArrayList<Chapter>();
    }
}
