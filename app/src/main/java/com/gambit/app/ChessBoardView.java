package com.gambit.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessBoardView extends View {

    public interface OnMoveAttemptListener {
        void onMoveAttempt(int fromSq, int toSq, String uci);
    }

    private ChessEngine engine;
    private OnMoveAttemptListener moveListener;
    private boolean flipped = false;

    // Exact Chess.com Colors
    private int lightSquareColor = Color.parseColor("#EBECD0");
    private int darkSquareColor  = Color.parseColor("#739552");
    private int lastMoveColor    = Color.parseColor("#80F5F682");
    private int selectedColor    = Color.parseColor("#80F7E752");
    private int highlightColor   = Color.parseColor("#66EF4444");

    private Paint lightSquarePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint darkSquarePaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint selectedPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint lastMovePaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint highlightPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint lightCoordPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint darkCoordPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint greenArrowPaint  = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint blueArrowPaint   = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint redArrowPaint    = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint bitmapPaint      = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    private int selectedSq = -1;
    private int lastFromSq = -1;
    private int lastToSq   = -1;

    private List<OpeningModel.Arrow> activeArrows = new ArrayList<OpeningModel.Arrow>();
    private List<Integer> activeHighlights = new ArrayList<Integer>();

    private float squareSize = 0f;

    // Chess.com Neo Piece Bitmaps
    private final Map<Character, Bitmap> pieceBitmaps = new HashMap<Character, Bitmap>();
    private final Rect srcRect = new Rect();
    private final RectF dstRect = new RectF();

    public ChessBoardView(Context context) {
        super(context);
        init(context);
    }

    public ChessBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        lightSquarePaint.setColor(lightSquareColor);
        darkSquarePaint.setColor(darkSquareColor);
        selectedPaint.setColor(selectedColor);
        lastMovePaint.setColor(lastMoveColor);
        highlightPaint.setColor(highlightColor);

        lightCoordPaint.setColor(darkSquareColor);
        lightCoordPaint.setTextSize(14f);
        lightCoordPaint.setFakeBoldText(true);

        darkCoordPaint.setColor(lightSquareColor);
        darkCoordPaint.setTextSize(14f);
        darkCoordPaint.setFakeBoldText(true);

        greenArrowPaint.setColor(Color.parseColor("#7FA650"));
        greenArrowPaint.setStrokeWidth(10f);
        greenArrowPaint.setStyle(Paint.Style.STROKE);
        greenArrowPaint.setStrokeCap(Paint.Cap.ROUND);

        blueArrowPaint.setColor(Color.parseColor("#52B2BF"));
        blueArrowPaint.setStrokeWidth(10f);
        blueArrowPaint.setStyle(Paint.Style.STROKE);
        blueArrowPaint.setStrokeCap(Paint.Cap.ROUND);

        redArrowPaint.setColor(Color.parseColor("#E05353"));
        redArrowPaint.setStrokeWidth(10f);
        redArrowPaint.setStyle(Paint.Style.STROKE);
        redArrowPaint.setStrokeCap(Paint.Cap.ROUND);

        loadPieceBitmaps(context);
    }

    private void loadPieceBitmaps(Context context) {
        pieceBitmaps.put(ChessEngine.WHITE_KING,   BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_wk));
        pieceBitmaps.put(ChessEngine.WHITE_QUEEN,  BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_wq));
        pieceBitmaps.put(ChessEngine.WHITE_ROOK,   BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_wr));
        pieceBitmaps.put(ChessEngine.WHITE_BISHOP, BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_wb));
        pieceBitmaps.put(ChessEngine.WHITE_KNIGHT, BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_wn));
        pieceBitmaps.put(ChessEngine.WHITE_PAWN,   BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_wp));

        pieceBitmaps.put(ChessEngine.BLACK_KING,   BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_bk));
        pieceBitmaps.put(ChessEngine.BLACK_QUEEN,  BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_bq));
        pieceBitmaps.put(ChessEngine.BLACK_ROOK,   BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_br));
        pieceBitmaps.put(ChessEngine.BLACK_BISHOP, BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_bb));
        pieceBitmaps.put(ChessEngine.BLACK_KNIGHT, BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_bn));
        pieceBitmaps.put(ChessEngine.BLACK_PAWN,   BitmapFactory.decodeResource(context.getResources(), R.drawable.piece_bp));
    }

    public void setEngine(ChessEngine engine) {
        this.engine = engine;
        invalidate();
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
        invalidate();
    }

    public boolean isFlipped() {
        return flipped;
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

                if (sq == lastFromSq || sq == lastToSq) {
                    canvas.drawRect(left, top, right, bottom, lastMovePaint);
                }

                if (sq == selectedSq) {
                    canvas.drawRect(left, top, right, bottom, selectedPaint);
                }

                if (activeHighlights.contains(sq)) {
                    canvas.drawRect(left, top, right, bottom, highlightPaint);
                }

                // Chess.com Corner Coordinates
                if (r == 7) {
                    char fileChar = (char) (flipped ? ('h' - f) : ('a' + f));
                    Paint cp = isDark ? darkCoordPaint : lightCoordPaint;
                    canvas.drawText(String.valueOf(fileChar), right - 13f, bottom - 5f, cp);
                }
                if (f == 0) {
                    char rankChar = (char) (flipped ? ('1' + r) : ('8' - r));
                    Paint cp = isDark ? darkCoordPaint : lightCoordPaint;
                    canvas.drawText(String.valueOf(rankChar), left + 4f, top + 15f, cp);
                }
            }
        }

        // 2. Draw Official Chess.com Neo Pieces
        for (int r = 0; r < 8; r++) {
            for (int f = 0; f < 8; f++) {
                int displayRow = flipped ? (7 - r) : r;
                int displayCol = flipped ? (7 - f) : f;
                int sq = displayRow * 8 + displayCol;

                char piece = engine.getPiece(sq);
                if (piece != ChessEngine.EMPTY) {
                    Bitmap bmp = pieceBitmaps.get(piece);
                    if (bmp != null) {
                        float left = f * squareSize;
                        float top  = r * squareSize;
                        dstRect.set(left, top, left + squareSize, top + squareSize);
                        srcRect.set(0, 0, bmp.getWidth(), bmp.getHeight());
                        canvas.drawBitmap(bmp, srcRect, dstRect, bitmapPaint);
                    }
                }
            }
        }

        // 3. Draw Tactical Arrows
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

        canvas.drawLine(x1, y1, x2, y2, paint);

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
                char piece = engine.getPiece(clickedSq);
                if (piece != ChessEngine.EMPTY) {
                    boolean pieceIsWhite = ChessEngine.isWhite(piece);
                    if (pieceIsWhite == engine.isWhiteToMove()) {
                        selectedSq = clickedSq;
                        invalidate();
                    }
                }
            } else {
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
