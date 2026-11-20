import json
import os
import sys

def verify_and_simulate(steps, course_name, ch_title):
    # Setup initial board
    board = {}
    files = 'abcdefgh'
    ranks = '12345678'
    
    # Empty board
    for f in files:
        for r in ranks:
            board[f + r] = '.'
            
    # White pieces
    board['a1'] = 'R'; board['b1'] = 'N'; board['c1'] = 'B'; board['d1'] = 'Q'
    board['e1'] = 'K'; board['f1'] = 'B'; board['g1'] = 'N'; board['h1'] = 'R'
    for f in files: board[f + '2'] = 'P'
    
    # Black pieces
    board['a8'] = 'r'; board['b8'] = 'n'; board['c8'] = 'b'; board['d8'] = 'q'
    board['e8'] = 'k'; board['f8'] = 'b'; board['g8'] = 'n'; board['h8'] = 'r'
    for f in files: board[f + '7'] = 'p'

    for idx, s in enumerate(steps):
        uci = s['uci']
        sq_from = uci[:2]
        sq_to = uci[2:4]
        promo = uci[4:] if len(uci) > 4 else ''
        
        piece = board[sq_from]
        if piece == '.':
            raise ValueError(f"Error in {course_name} -> {ch_title} step {idx} ({s['moveSan']}): From square {sq_from} is empty!")
            
        # Check castling
        if piece == 'K' and sq_from == 'e1' and sq_to == 'g1':
            board['e1'] = '.'; board['g1'] = 'K'; board['h1'] = '.'; board['f1'] = 'R'
        elif piece == 'K' and sq_from == 'e1' and sq_to == 'c1':
            board['e1'] = '.'; board['c1'] = 'K'; board['a1'] = '.'; board['d1'] = 'R'
        elif piece == 'k' and sq_from == 'e8' and sq_to == 'g8':
            board['e8'] = '.'; board['g8'] = 'k'; board['h8'] = '.'; board['f8'] = 'r'
        elif piece == 'k' and sq_from == 'e8' and sq_to == 'c8':
            board['e8'] = '.'; board['c8'] = 'k'; board['a8'] = '.'; board['d8'] = 'r'
        # En passant
        elif piece in ('P', 'p') and sq_from[0] != sq_to[0] and board[sq_to] == '.':
            board[sq_from] = '.'
            board[sq_to] = piece
            cap_rank = '5' if piece == 'P' else '4'
            board[sq_to[0] + cap_rank] = '.'
        else:
            board[sq_from] = '.'
            if promo:
                board[sq_to] = promo.upper() if piece.isupper() else promo.lower()
            elif piece == 'P' and sq_to[1] == '8':
                board[sq_to] = 'Q'
            elif piece == 'p' and sq_to[1] == '1':
                board[sq_to] = 'q'
            else:
                board[sq_to] = piece

print("Simulator initialized successfully.")
