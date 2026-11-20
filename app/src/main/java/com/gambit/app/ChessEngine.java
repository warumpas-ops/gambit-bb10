package com.gambit.app;

import java.util.ArrayList;
import java.util.List;

public class ChessEngine {
    // Piece definitions
    public static final char EMPTY = ' ';
    public static final char WHITE_PAWN = 'P';
    public static final char WHITE_KNIGHT = 'N';
    public static final char WHITE_BISHOP = 'B';
    public static final char WHITE_ROOK = 'R';
    public static final char WHITE_QUEEN = 'Q';
    public static final char WHITE_KING = 'K';

    public static final char BLACK_PAWN = 'p';
    public static final char BLACK_KNIGHT = 'n';
    public static final char BLACK_BISHOP = 'b';
    public static final char BLACK_ROOK = 'r';
    public static final char BLACK_QUEEN = 'q';
    public static final char BLACK_KING = 'k';

    // 8x8 Board: index 0 = a8, 7 = h8, 56 = a1, 63 = h1
    // row 0 is rank 8 (black side), row 7 is rank 1 (white side)
    private final char[] board = new char[64];
    private boolean whiteToMove = true;
    private boolean whiteCanCastleK = true;
    private boolean whiteCanCastleQ = true;
    private boolean blackCanCastleK = true;
    private boolean blackCanCastleQ = true;
    private int epSquare = -1; // -1 if no en passant

    public ChessEngine() {
        resetToStartingPosition();
    }

    public void resetToStartingPosition() {
        loadFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public void loadFen(String fen) {
        for (int i = 0; i < 64; i++) board[i] = EMPTY;
        String[] parts = fen.trim().split("\\s+");
        String pos = parts[0];

        int sq = 0;
        for (int i = 0; i < pos.length(); i++) {
            char c = pos.charAt(i);
            if (c == '/') continue;
            if (Character.isDigit(c)) {
                sq += (c - '0');
            } else {
                if (sq < 64) {
                    board[sq++] = c;
                }
            }
        }

        whiteToMove = parts.length < 2 || parts[1].equals("w");

        whiteCanCastleK = false;
        whiteCanCastleQ = false;
        blackCanCastleK = false;
        blackCanCastleQ = false;
        if (parts.length > 2) {
            String castling = parts[2];
            whiteCanCastleK = castling.contains("K");
            whiteCanCastleQ = castling.contains("Q");
            blackCanCastleK = castling.contains("k");
            blackCanCastleQ = castling.contains("q");
        }

        epSquare = -1;
        if (parts.length > 3 && !parts[3].equals("-")) {
            epSquare = parseSquare(parts[3]);
        }
    }

    public String toFen() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 8; r++) {
            int empty = 0;
            for (int f = 0; f < 8; f++) {
                char p = board[r * 8 + f];
                if (p == EMPTY) {
                    empty++;
                } else {
                    if (empty > 0) {
                        sb.append(empty);
                        empty = 0;
                    }
                    sb.append(p);
                }
            }
            if (empty > 0) sb.append(empty);
            if (r < 7) sb.append('/');
        }

        sb.append(whiteToMove ? " w " : " b ");
        StringBuilder castling = new StringBuilder();
        if (whiteCanCastleK) castling.append('K');
        if (whiteCanCastleQ) castling.append('Q');
        if (blackCanCastleK) castling.append('k');
        if (blackCanCastleQ) castling.append('q');
        if (castling.length() == 0) castling.append('-');
        sb.append(castling).append(" ");

        if (epSquare >= 0 && epSquare < 64) {
            sb.append(squareToString(epSquare));
        } else {
            sb.append("-");
        }
        sb.append(" 0 1");
        return sb.toString();
    }

    public char getPiece(int sq) {
        if (sq < 0 || sq >= 64) return EMPTY;
        return board[sq];
    }

    public char getPiece(int file, int rank) {
        int r = 7 - rank;
        int sq = r * 8 + file;
        return getPiece(sq);
    }

    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    public static boolean isWhite(char p) {
        return Character.isUpperCase(p);
    }

    public static boolean isBlack(char p) {
        return Character.isLowerCase(p);
    }

    public static int parseSquare(String s) {
        if (s == null || s.length() < 2) return -1;
        int file = s.charAt(0) - 'a';
        int rank = s.charAt(1) - '1';
        if (file < 0 || file > 7 || rank < 0 || rank > 7) return -1;
        return (7 - rank) * 8 + file;
    }

    public static String squareToString(int sq) {
        if (sq < 0 || sq >= 64) return "-";
        int r = sq / 8;
        int f = sq % 8;
        char file = (char) ('a' + f);
        char rank = (char) ('1' + (7 - r));
        return "" + file + rank;
    }

    /**
     * Executes a move given start square and end square (e.g., e2 to e4 -> 52 to 36).
     * Handles en passant, castling, and pawn promotion automatically.
     */
    public boolean makeMove(int from, int to) {
        if (from < 0 || from >= 64 || to < 0 || to >= 64) return false;
        char p = board[from];
        if (p == EMPTY) return false;

        // Verify side to move
        if (whiteToMove && !isWhite(p)) return false;
        if (!whiteToMove && !isBlack(p)) return false;

        // Handle Castling
        if (p == WHITE_KING) {
            if (from == 60 && to == 62) { // O-O
                board[63] = EMPTY;
                board[61] = WHITE_ROOK;
            } else if (from == 60 && to == 58) { // O-O-O
                board[56] = EMPTY;
                board[59] = WHITE_ROOK;
            }
            whiteCanCastleK = false;
            whiteCanCastleQ = false;
        } else if (p == BLACK_KING) {
            if (from == 4 && to == 6) { // O-O
                board[7] = EMPTY;
                board[5] = BLACK_ROOK;
            } else if (from == 4 && to == 2) { // O-O-O
                board[0] = EMPTY;
                board[3] = BLACK_ROOK;
            }
            blackCanCastleK = false;
            blackCanCastleQ = false;
        }

        // Rook movements void castling
        if (from == 56) whiteCanCastleQ = false;
        if (from == 63) whiteCanCastleK = false;
        if (from == 0)  blackCanCastleQ = false;
        if (from == 7)  blackCanCastleK = false;

        // Handle En Passant capture
        if (p == WHITE_PAWN && to == epSquare) {
            board[to + 8] = EMPTY; // remove black pawn
        } else if (p == BLACK_PAWN && to == epSquare) {
            board[to - 8] = EMPTY; // remove white pawn
        }

        // Update new En Passant target
        epSquare = -1;
        if (p == WHITE_PAWN && from - to == 16) {
            epSquare = from - 8;
        } else if (p == BLACK_PAWN && to - from == 16) {
            epSquare = from + 8;
        }

        // Promotion (auto-queen default)
        if (p == WHITE_PAWN && to < 8) {
            p = WHITE_QUEEN;
        } else if (p == BLACK_PAWN && to >= 56) {
            p = BLACK_QUEEN;
        }

        board[from] = EMPTY;
        board[to] = p;
        whiteToMove = !whiteToMove;
        return true;
    }

    public boolean makeMoveUci(String uci) {
        if (uci == null || uci.length() < 4) return false;
        int from = parseSquare(uci.substring(0, 2));
        int to = parseSquare(uci.substring(2, 4));
        return makeMove(from, to);
    }
}
