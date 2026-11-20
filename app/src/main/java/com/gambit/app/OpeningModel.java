package com.gambit.app;

import java.util.ArrayList;
import java.util.List;

public class OpeningModel {

    public static class Arrow {
        public String from; // e.g. "e2"
        public String to;   // e.g. "e4"
        public String color; // "green" (best move), "blue" (plan), "red" (threat/mistake)

        public Arrow(String from, String to, String color) {
            this.from = from;
            this.to = to;
            this.color = color;
        }
    }

    public static class MoveStep {
        public String moveSan;     // e.g. "1. e4", "1... c6"
        public String uci;         // e.g. "e2e4"
        public boolean isUserMove; // true if student plays it
        public String title;       // e.g. "Control the Center"
        public String explanation; // The human rationale explaining the "why"
        public String warning;     // Optional common mistake / trap warning
        public List<Arrow> arrows = new ArrayList<Arrow>();
        public List<String> highlightSquares = new ArrayList<String>(); // e.g. ["e4", "d5"]
    }

    public static class Chapter {
        public String id;
        public String title;       // e.g. "Main Line: Two Knights Defense"
        public String subtitle;    // e.g. "Countering 3... Nf6"
        public String orientation; // "white" or "black"
        public List<MoveStep> steps = new ArrayList<MoveStep>();
    }

    public static class Course {
        public String id;
        public String name;        // e.g. "The Italian Game"
        public String color;       // "White" or "Black"
        public String description; // Overview of the opening
        public List<Chapter> chapters = new ArrayList<Chapter>();
    }
}
