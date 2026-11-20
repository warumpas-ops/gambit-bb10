package com.gambit.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class ChessBoardView extends View {

    public interface OnMoveAttemptListener {
        void onMoveAttempt(int fromSq, int toSq, String uci);
    }

    private ChessEngine engine;
    private OnMoveAttemptListener moveListener;
    private boolean flipped = false; // true if Black is at the bottom

    // High contrast modern OLED colors
    private int lightSquareColor = Color.parseColor("#E0E0E0"); // Crisp off-white
    private int darkSquareColor  = Color.parseColor("#4A4D54"); // Slate graphite
    private int selectedColor    = Color.parseColor("#66F59E0B"); // Translucent amber glow
    private int lastMoveColor    = Color.parseColor("#553B82F6"); // Translucent blue glow
    private int highlightColor   = Color.parseColor("#55E11D48"); // Translucent ruby red glow

    // Arrow and label paints
    private Paint lightSquarePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint darkSquarePaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint selectedPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint lastMovePaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint highlightPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint coordPaint       = new Paint(Paint.ANTI_ALIAS_FLAG);

    // Arrow Paints
    private Paint greenArrowPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint blueArrowPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint redArrowPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint textPiecePaint   = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int selectedSq = -1;
    private int lastFromSq = -1;
    private int lastToSq   = -1;

    private List<OpeningModel.Arrow> activeArrows = new ArrayList<OpeningModel.Arrow>();
    private List<Integer> activeHighlights = new ArrayList<Integer>();

    private float squareSize = 0f;

    public ChessBoardView(Context context) {
        super(context);
        init();
    }

    public ChessBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        lightSquarePaint.setColor(lightSquareColor);
        darkSquarePaint.setColor(darkSquareColor);
        selectedPaint.setColor(selectedColor);
        lastMovePaint.setColor(lastMoveColor);
        highlightPaint.setColor(highlightColor);

        coordPaint.setColor(Color.parseColor("#8E8E93"));
        coordPaint.setTextSize(18f);

        // Green arrow: best move
        greenArrowPaint.setColor(Color.parseColor("#22C55E"));
        greenArrowPaint.setStrokeWidth(10f);
        greenArrowPaint.setStyle(Paint.Style.STROKE);
        greenArrowPaint.setStrokeCap(Paint.Cap.ROUND);

        // Blue arrow: strategic plan
        blueArrowPaint.setColor(Color.parseColor("#38BDF8"));
        blueArrowPaint.setStrokeWidth(10f);
        blueArrowPaint.setStyle(Paint.Style.STROKE);
        blueArrowPaint.setStrokeCap(Paint.Cap.ROUND);

        // Red arrow: threat or blunder
        redArrowPaint.setColor(Color.parseColor("#EF4444"));
        redArrowPaint.setStrokeWidth(10f);
        redArrowPaint.setStyle(Paint.Style.STROKE);
        redArrowPaint.setStrokeCap(Paint.Cap.ROUND);

        textPiecePaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setEngine(ChessEngine engine) {
        this.engine = engine;
        invalidate();
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
        invalidate();
    }

    public void setOnMoveAttemptListener(OnMoveAttemptListener listener) {
        this.moveListener = listener;
    }

    public void setLastMove(int from, int to) {
        this.lastFromSq = from;
        this.lastToSq = to;
        invalidate();
    }

    public void setArrows(List<OpeningModel.Arrow> arrows) {
        this.activeArrows.clear();
        if (arrows != null) this.activeArrows.addAll(arrows);
        invalidate();
    }

    public void setHighlightSquares(List<String> squares) {
        this.activeHighlights.clear();
        if (squares != null) {
            for (String s : squares) {
                int sq = ChessEngine.parseSquare(s);
                if (sq >= 0) activeHighlights.add(sq);
            }
        }
        invalidate();
    }

    public void clearSelection() {
        this.selectedSq = -1;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Enforce exact square dimensions for the 8x8 chessboard
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        if (w == 0 || engine == null) return;
        squareSize = w / 8.0f;

        // 1. Draw Squares & Highlights
        for (int r = 0; r < 8; r++) {
            for (int f = 0; f < 8; f++) {
                int displayRow = flipped ? (7 - r) : r;
                int displayCol = flipped ? (7 - f) : f;
                int sq = displayRow * 8 + displayCol;

                float left = f * squareSize;
                float top  = r * squareSize;
                float right = left + squareSize;
                float bottom = top + squareSize;

                boolean isDark = (displayRow + displayCol) % 2 != 0;
                canvas.drawRect(left, top, right, bottom, isDark ? darkSquarePaint : lightSquarePaint);

                // Highlight last move
                if (sq == lastFromSq || sq == lastToSq) {
                    canvas.drawRect(left, top, right, bottom, lastMovePaint);
                }

                // Highlight selected square
                if (sq == selectedSq) {
                    canvas.drawRect(left, top, right, bottom, selectedPaint);
                }

                // Highlight custom strategic/target squares
                if (activeHighlights.contains(sq)) {
                    canvas.drawRect(left, top, right, bottom, highlightPaint);
                }
            }
        }

        // 2. Draw Coordinates (Ranks on left, Files on bottom)
        for (int i = 0; i < 8; i++) {
            char fileChar = (char) (flipped ? ('h' - i) : ('a' + i));
            char rankChar = (char) (flipped ? ('1' + i) : ('8' - i));

            // Rank label on left edge
            canvas.drawText(String.valueOf(rankChar), 4f, i * squareSize + 22f, coordPaint);
            // File label on bottom edge
            canvas.drawText(String.valueOf(fileChar), (i + 1) * squareSize - 16f, w - 6f, coordPaint);
        }

        // 3. Draw Pieces
        textPiecePaint.setTextSize(squareSize * 0.76f);
        Paint.FontMetrics fm = textPiecePaint.getFontMetrics();
        float yOffset = ((fm.descent - fm.ascent) / 2) - fm.descent;

        for (int r = 0; r < 8; r++) {
            for (int f = 0; f < 8; f++) {
                int displayRow = flipped ? (7 - r) : r;
                int displayCol = flipped ? (7 - f) : f;
                int sq = displayRow * 8 + displayCol;

                char piece = engine.getPiece(sq);
                if (piece != ChessEngine.EMPTY) {
                    float cx = f * squareSize + (squareSize / 2f);
                    float cy = r * squareSize + (squareSize / 2f) + yOffset;
                    String glyph = getPieceGlyph(piece);

                    // Drop shadow for crisp contrast against any square
                    textPiecePaint.setColor(ChessEngine.isWhite(piece) ? Color.WHITE : Color.parseColor("#121214"));
                    textPiecePaint.setShadowLayer(5.0f, 0f, 2.0f, Color.parseColor("#99000000"));
                    canvas.drawText(glyph, cx, cy, textPiecePaint);
                    textPiecePaint.clearShadowLayer();
                }
            }
        }

        // 4. Draw Tactical Arrows
        for (OpeningModel.Arrow arrow : activeArrows) {
            drawArrow(canvas, arrow);
        }
    }

    private void drawArrow(Canvas canvas, OpeningModel.Arrow arrow) {
        int from = ChessEngine.parseSquare(arrow.from);
        int to   = ChessEngine.parseSquare(arrow.to);
        if (from < 0 || to < 0) return;

        int fromR = from / 8; int fromF = from % 8;
        int toR   = to / 8;   int toF   = to % 8;

        if (flipped) {
            fromR = 7 - fromR; fromF = 7 - fromF;
            toR   = 7 - toR;   toF   = 7 - toF;
        }

        float x1 = fromF * squareSize + (squareSize / 2f);
        float y1 = fromR * squareSize + (squareSize / 2f);
        float x2 = toF   * squareSize + (squareSize / 2f);
        float y2 = toR   * squareSize + (squareSize / 2f);

        Paint paint = greenArrowPaint;
        if ("blue".equalsIgnoreCase(arrow.color)) paint = blueArrowPaint;
        else if ("red".equalsIgnoreCase(arrow.color)) paint = redArrowPaint;

        // Draw arrow shaft
        canvas.drawLine(x1, y1, x2, y2, paint);

        // Draw arrow head
        float angle = (float) Math.atan2(y2 - y1, x2 - x1);
        float arrowHeadLen = squareSize * 0.32f;
        float angleOffset = (float) Math.toRadians(25.0);

        Path headPath = new Path();
        headPath.moveTo(x2, y2);
        headPath.lineTo(
                x2 - arrowHeadLen * (float) Math.cos(angle - angleOffset),
                y2 - arrowHeadLen * (float) Math.sin(angle - angleOffset)
        );
        headPath.lineTo(
                x2 - (arrowHeadLen * 0.7f) * (float) Math.cos(angle),
                y2 - (arrowHeadLen * 0.7f) * (float) Math.sin(angle)
        );
        headPath.lineTo(
                x2 - arrowHeadLen * (float) Math.cos(angle + angleOffset),
                y2 - arrowHeadLen * (float) Math.sin(angle + angleOffset)
        );
        headPath.close();

        Paint fillPaint = new Paint(paint);
        fillPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(headPath, fillPaint);
    }

    private String getPieceGlyph(char p) {
        switch (p) {
            case ChessEngine.WHITE_KING:   return "♔";
            case ChessEngine.WHITE_QUEEN:  return "♕";
            case ChessEngine.WHITE_ROOK:   return "♖";
            case ChessEngine.WHITE_BISHOP: return "♗";
            case ChessEngine.WHITE_KNIGHT: return "♘";
            case ChessEngine.WHITE_PAWN:   return "♙";
            case ChessEngine.BLACK_KING:   return "♚";
            case ChessEngine.BLACK_QUEEN:  return "♛";
            case ChessEngine.BLACK_ROOK:   return "♜";
            case ChessEngine.BLACK_BISHOP: return "♝";
            case ChessEngine.BLACK_KNIGHT: return "♞";
            case ChessEngine.BLACK_PAWN:   return "♟";
            default: return "";
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (squareSize <= 0) return false;
            int f = (int) (event.getX() / squareSize);
            int r = (int) (event.getY() / squareSize);
            if (f < 0 || f > 7 || r < 0 || r > 7) return false;

            int clickedDisplayRow = flipped ? (7 - r) : r;
            int clickedDisplayCol = flipped ? (7 - f) : f;
            int clickedSq = clickedDisplayRow * 8 + clickedDisplayCol;

            if (selectedSq == -1) {
                // First click: select piece if owned by side to move
                char piece = engine.getPiece(clickedSq);
                if (piece != ChessEngine.EMPTY) {
                    boolean pieceIsWhite = ChessEngine.isWhite(piece);
                    if (pieceIsWhite == engine.isWhiteToMove()) {
                        selectedSq = clickedSq;
                        invalidate();
                    }
                }
            } else {
                // Second click: attempt move from selectedSq to clickedSq
                int from = selectedSq;
                int to = clickedSq;
                selectedSq = -1;
                invalidate();

                if (from != to && moveListener != null) {
                    String uci = ChessEngine.squareToString(from) + ChessEngine.squareToString(to);
                    moveListener.onMoveAttempt(from, to, uci);
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
}
