# Openings 1 to 5: Italian Game, French Defense, Caro-Kann, Sicilian Defense, Queen's Gambit

def step(san, uci, is_user, title, expl, warn="", arrows=None, sqs=None):
    return {
        "moveSan": san,
        "uci": uci,
        "isUserMove": is_user,
        "title": title,
        "explanation": expl,
        "warning": warn,
        "arrows": arrows or [],
        "highlightSquares": sqs or []
    }

def get_part1_courses():
    courses = []

    # ==========================================
    # 1. THE ITALIAN GAME (WHITE)
    # ==========================================
    courses.append({
        "id": "italian_game_white",
        "name": "The Italian Game",
        "color": "White",
        "level": "Level 1: Fundamentals",
        "description": "The open, aggressive classical opening for White. Rapid development, central pressure, and targeting the vulnerable f7 square.",
        "chapters": [
            {
                "id": "it_w_giuoco_piano",
                "title": "Giuoco Piano: Center Steamroller",
                "subtitle": "Build the ideal c3 + d4 pawn center",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "Stake the Center", "Control d5 and open diagonals for your Queen and Bishop.", "Don't play passive moves like a3 or h3.", [{"from":"e2","to":"e4","color":"green"}], ["d5","f5"]),
                    step("1... e5", "e7e5", False, "Black Responds", "Black contests the center symmetrically.", "", [{"from":"e7","to":"e5","color":"blue"}]),
                    step("2. Nf3", "g1f3", True, "Develop with Attack", "Develop your knight toward the center while immediately threatening Black's e5 pawn.", "Never block your f-pawn unnecessarily with early queen sorties.", [{"from":"g1","to":"f3","color":"green"},{"from":"f3","to":"e5","color":"red"}], ["e5"]),
                    step("2... Nc6", "b8c6", False, "Black Defends e5", "Black guards e5 and develops naturally.", "", [{"from":"b8","to":"c6","color":"blue"}]),
                    step("3. Bc4", "f1c4", True, "The Italian Bishop", "Aim directly at Black's weakest square, f7, which is only protected by their King.", "Be careful not to trap this bishop later.", [{"from":"f1","to":"c4","color":"green"},{"from":"c4","to":"f7","color":"red"}], ["f7"]),
                    step("3... Bc5", "f8c5", False, "Giuoco Piano Mainline", "Black develops their bishop actively before closing the diagonal.", "", [{"from":"f8","to":"c5","color":"blue"}]),
                    step("4. c3", "c2c3", True, "Prepare the Classical Center", "Support the upcoming d2-d4 push to conquer the full center.", "Don't rush with 4. d4 immediately without preparation.", [{"from":"c2","to":"c3","color":"green"},{"from":"c3","to":"d4","color":"blue"}], ["d4"]),
                    step("4... Nf6", "g8f6", False, "Black Hits e4", "Black develops their knight and attacks your e4 pawn.", "", [{"from":"g8","to":"f6","color":"blue"},{"from":"f6","to":"e4","color":"red"}]),
                    step("5. d4", "d2d4", True, "Central Breakthrough", "Strike in the center! Attack Black's bishop and force concessions.", "Never hesitate here; hesitation gives Black the initiative.", [{"from":"d2","to":"d4","color":"green"},{"from":"d4","to":"c5","color":"red"}], ["c5","e5"]),
                    step("5... exd4", "e5d4", False, "Black Exchanges", "Black is forced to capture to relieve pressure.", "", [{"from":"e5","to":"d4","color":"blue"}]),
                    step("6. cxd4", "c3d4", True, "Recapture & Expand", "Re-establish the dream pawn center (e4 + d4) while attacking the bishop again.", "Don't recapture with the knight; keep pawns unified.", [{"from":"c3","to":"d4","color":"green"},{"from":"d4","to":"c5","color":"red"}], ["e4","d4"]),
                    step("6... Bb4+", "c5b4", False, "Black Checks", "Black gives check to gain tempo for escape.", "", [{"from":"c5","to":"b4","color":"blue"}]),
                    step("7. Bd2", "c1d2", True, "Block with the Bishop", "Block the check and challenge Black's active piece.", "7. Nc3 is the sharp Möller Attack, but 7. Bd2 is the cleanest positional refutation.", [{"from":"c1","to":"d2","color":"green"}], ["b4"])
                ]
            },
            {
                "id": "it_w_fried_liver",
                "title": "Two Knights: Fried Liver Attack",
                "subtitle": "Punish 3... Nf6 with 4. Ng5! and 6. Nxf7!",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "Pawn to e4", "Establish your central presence.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... e5", "e7e5", False, "Black Plays e5", "Symmetrical response.", "", [{"from":"e7","to":"e5","color":"blue"}]),
                    step("2. Nf3", "g1f3", True, "Knight to f3", "Develop and attack e5.", "", [{"from":"g1","to":"f3","color":"green"}]),
                    step("2... Nc6", "b8c6", False, "Knight to c6", "Guarding e5.", "", [{"from":"b8","to":"c6","color":"blue"}]),
                    step("3. Bc4", "f1c4", True, "Bishop to c4", "Target the vulnerable f7 square.", "", [{"from":"f1","to":"c4","color":"green"}]),
                    step("3... Nf6", "g8f6", False, "Two Knights Defense", "Black counters against e4 instead of defending f7.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("4. Ng5", "f3g5", True, "Double Attack on f7", "Coordinate Knight and Bishop to create an irresistible threat on f7.", "Only play this against 3... Nf6, not 3... Bc5!", [{"from":"f3","to":"g5","color":"green"},{"from":"g5","to":"f7","color":"red"},{"from":"c4","to":"f7","color":"red"}], ["f7"]),
                    step("4... d5", "d7d5", False, "The Only Defense", "Black must block your bishop diagonal.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("5. exd5", "e4d5", True, "Pawn Takes d5", "Eliminate Black's central shield.", "Never take with the bishop!", [{"from":"e4","to":"d5","color":"green"}]),
                    step("5... Nxd5", "f6d5", False, "Black Blunders into Fried Liver", "Black recaptures with the knight, walking directly into your trap!", "", [{"from":"f6","to":"d5","color":"blue"}]),
                    step("6. Nxf7", "g5f7", True, "The Fried Liver Sacrifice!", "Sacrifice the Knight to rip open Black's King and fork Queen + Rook!", "This is a forced winning attack against 5... Nxd5.", [{"from":"g5","to":"f7","color":"green"},{"from":"f7","to":"d8","color":"red"},{"from":"f7","to":"h8","color":"red"}], ["f7"]),
                    step("6... Kxf7", "e8f7", False, "King Takes", "Black must capture your knight.", "", [{"from":"e8","to":"f7","color":"blue"}]),
                    step("7. Qf3+", "d1f3", True, "Queen Forks King & Knight", "Check the exposed king while piling up fatal pressure on the pinned d5 knight.", "Black's king is forced into the center with 7... Ke6.", [{"from":"d1","to":"f3","color":"green"},{"from":"f3","to":"f7","color":"red"},{"from":"f3","to":"d5","color":"red"}], ["f7","d5"])
                ]
            }
        ]
    })

    # ==========================================
    # 1. THE ITALIAN GAME (BLACK)
    # ==========================================
    courses.append({
        "id": "italian_game_black",
        "name": "The Italian Game",
        "color": "Black",
        "level": "Level 1: Fundamentals",
        "description": "Black's complete defenses against 1. e4 e5 2. Nf3 Nc6 3. Bc4. Neutralize the Fried Liver with 5... Na5! and achieve rock-solid equality in the Giuoco Pianissimo.",
        "chapters": [
            {
                "id": "it_b_two_knights",
                "title": "Two Knights: The 5... Na5! Refutation",
                "subtitle": "Punish White's premature 4. Ng5 attack",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Stakes Center", "White opens e4.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... e5", "e7e5", True, "Claim Symmetrical Center", "Stake our claim on the center and free our pieces.", "Avoid passive responses like e6 or c6 if aiming for open classical play.", [{"from":"e7","to":"e5","color":"green"}], ["d4","f4"]),
                    step("2. Nf3", "g1f3", False, "White Attacks e5", "White develops toward the center.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("2... Nc6", "b8c6", True, "Defend e5", "Develop the knight to defend e5 naturally.", "Don't defend with 2... d6 (Philidor) which blocks the dark-squared bishop.", [{"from":"b8","to":"c6","color":"green"}], ["e5"]),
                    step("3. Bc4", "f1c4", False, "Italian Bishop", "White targets our f7 square.", "", [{"from":"f1","to":"c4","color":"blue"}]),
                    step("3... Nf6", "g8f6", True, "Two Knights Counterattack", "Counterattack White's e4 pawn immediately!", "Stay sharp: White can jump to g5 next.", [{"from":"g8","to":"f6","color":"green"},{"from":"f6","to":"e4","color":"red"}], ["e4"]),
                    step("4. Ng5", "f3g5", False, "White Traps f7", "White goes for the Fried Liver battery.", "", [{"from":"f3","to":"g5","color":"blue"}]),
                    step("4... d5", "d7d5", True, "Block the Diagonal!", "The only correct move: shut down the bishop's line of sight to f7.", "Never play 4... Bc5? or 4... h6? which lose instantly.", [{"from":"d7","to":"d5","color":"green"}], ["d5"]),
                    step("5. exd5", "e4d5", False, "White Takes", "White opens the e-file.", "", [{"from":"e4","to":"d5","color":"blue"}]),
                    step("5... Na5!", "c6a5", True, "The Master Refutation: 5... Na5!", "Attack White's dangerous bishop and push White backward!", "FATAL BLUNDER: Never play 5... Nxd5? which allows 6. Nxf7! Fried Liver!", [{"from":"c6","to":"a5","color":"green"},{"from":"a5","to":"c4","color":"red"}], ["c4"]),
                    step("6. Bb5+", "c4b5", False, "White Checks", "White tries to hold the bishop with tempo.", "", [{"from":"c4","to":"b5","color":"blue"}]),
                    step("6... c6", "c7c6", True, "Interpose with Pawn", "Sacrifice a flank pawn to win total piece activity and bishop pair.", "Don't block with Bd7; 6... c6 is the dynamic powerhouse.", [{"from":"c7","to":"c6","color":"green"}], ["b5"]),
                    step("7. dxc6", "d5c6", False, "White Takes c6", "White grabs a pawn.", "", [{"from":"d5","to":"c6","color":"blue"}]),
                    step("7... bxc6", "b7c6", True, "Recapture & Attack Bishop", "Recapture with pawn! You now dominate the center and open the b-file.", "White's bishop must retreat in sorrow.", [{"from":"b7","to":"c6","color":"green"}], ["b5"])
                ]
            },
            {
                "id": "it_b_pianissimo",
                "title": "Giuoco Pianissimo: Solid Setup",
                "subtitle": "Safe, symmetrical equality with 3... Bc5",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Plays e4", "White claims the center.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... e5", "e7e5", True, "Black Equalizes", "Stake central equality.", "", [{"from":"e7","to":"e5","color":"green"}]),
                    step("2. Nf3", "g1f3", False, "Knight to f3", "Attacking e5.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("2... Nc6", "b8c6", True, "Defend e5", "Solid piece development.", "", [{"from":"b8","to":"c6","color":"green"}]),
                    step("3. Bc4", "f1c4", False, "Bishop to c4", "Targeting f7.", "", [{"from":"f1","to":"c4","color":"blue"}]),
                    step("3... Bc5", "f8c5", True, "Quiet Bishop Development", "Mirror White's bishop and control the critical d4 square.", "Always develop the bishop before playing d6.", [{"from":"f8","to":"c5","color":"green"}], ["d4"]),
                    step("4. d3", "d2d3", False, "Quiet Italian (Pianissimo)", "White chooses slow maneuvering.", "", [{"from":"d2","to":"d3","color":"blue"}]),
                    step("4... Nf6", "g8f6", True, "Develop Knight to f6", "Bring the kingside knight into action and prepare castling.", "Don't neglect king safety.", [{"from":"g8","to":"f6","color":"green"}], ["e4"]),
                    step("5. O-O", "e1g1", False, "White Castles", "White safeguards king.", "", [{"from":"e1","to":"g1","color":"blue"}]),
                    step("5... d6", "d7d6", True, "Anchor Center & Free Bishop", "Solidify e5 and open the diagonal for Black's light-squared bishop.", "Black achieves complete harmony and equal chances.", [{"from":"d7","to":"d6","color":"green"}], ["e5"])
                ]
            }
        ]
    })

    # ==========================================
    # 2. THE FRENCH DEFENSE (WHITE)
    # ==========================================
    courses.append({
        "id": "french_defense_white",
        "name": "The French Defense",
        "color": "White",
        "level": "Level 2: Tactical & Aggressive",
        "description": "Master White's most suffocating setups against the resilient French. Cramp Black with the Advance Variation (3. e5) and dominate the center.",
        "chapters": [
            {
                "id": "fr_w_advance",
                "title": "Advance Variation: The Space Wedge",
                "subtitle": "Lock Black's light-squared bishop with 3. e5",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "King's Pawn Advance", "Claim central territory.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... e6", "e7e6", False, "French Defense", "Black prepares to strike d5 while protecting their pawn chain.", "", [{"from":"e7","to":"e6","color":"blue"}]),
                    step("2. d4", "d2d4", True, "Seize the Broad Center", "Take full central control with two pawns on e4 and d4.", "Always occupy the center when given the opportunity.", [{"from":"d2","to":"d4","color":"green"}], ["d4","e4"]),
                    step("2... d5", "d7d5", False, "Black Strikes d4/e4", "Black challenges e4 directly.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("3. e5", "e4e5", True, "The Advance Wedge", "Push e5! Gain a space advantage on the kingside and trap Black's light-squared bishop.", "Be ready for Black to counterattack your d4 base with c5.", [{"from":"e4","to":"e5","color":"green"}], ["e5","f6"]),
                    step("3... c5", "c7c5", False, "Black Strikes the Base", "Black attacks d4 to undermine your pawn chain.", "", [{"from":"c7","to":"c5","color":"blue"},{"from":"c5","to":"d4","color":"red"}]),
                    step("4. c3", "c2c3", True, "Fortify the Center Chain", "Cement d4 with c3. Your pawn chain b2-c3-d4-e5 is rock solid.", "Never let Black capture on d4 for free.", [{"from":"c2","to":"c3","color":"green"}], ["d4"]),
                    step("4... Nc6", "b8c6", False, "Black Piles Pressure", "Adding a second attacker to d4.", "", [{"from":"b8","to":"c6","color":"blue"},{"from":"c6","to":"d4","color":"red"}]),
                    step("5. Nf3", "g1f3", True, "Develop & Defend", "Defend d4 with your knight and prepare kingside development.", "Keep your focus on guarding d4.", [{"from":"g1","to":"f3","color":"green"}], ["d4"]),
                    step("5... Qb6", "d8b6", False, "Triple Threat on d4 & b2", "Black adds queen pressure on d4 and glances at b2.", "", [{"from":"d8","to":"b6","color":"blue"}]),
                    step("6. a3", "a2a3", True, "Prepare b4 Space Expansion", "Prevent Bb4+ pins and prepare b4 to kick Black's c5 pawn away.", "A modern masterclass move safeguarding your pawn chain.", [{"from":"a2","to":"a3","color":"green"},{"from":"a3","to":"b4","color":"blue"}], ["b4"])
                ]
            },
            {
                "id": "fr_w_tarrasch",
                "title": "Tarrasch Variation: Harmonic Flow",
                "subtitle": "Play 3. Nd2 to avoid pins and keep c3 open",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "Pawn to e4", "Open the board.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... e6", "e7e6", False, "French Setup", "Solid stance.", "", [{"from":"e7","to":"e6","color":"blue"}]),
                    step("2. d4", "d2d4", True, "Pawn to d4", "Take the full center.", "", [{"from":"d2","to":"d4","color":"green"}]),
                    step("2... d5", "d7d5", False, "Black Counters d5", "Attacking e4.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("3. Nd2", "b1d2", True, "The Tarrasch Knight", "Develop the knight to d2! This avoids the Bb4 pin and leaves c3 available for a pawn.", "Don't play 3. Nc3 if you dislike facing the sharp Winawer 3... Bb4.", [{"from":"b1","to":"d2","color":"green"}], ["c3"]),
                    step("3... Nf6", "g8f6", False, "Black Hits e4", "Pressuring your e4 pawn.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("4. e5", "e4e5", True, "Lock the Kingside", "Force Black's knight to retreat to d7 and win space.", "Push forward with tempo.", [{"from":"e4","to":"e5","color":"green"}], ["f6"]),
                    step("4... Nfd7", "f6d7", False, "Knight Retreats", "Black reorganizes.", "", [{"from":"f6","to":"d7","color":"blue"}]),
                    step("5. Bd3", "f1d3", True, "Aggressive Bishop Post", "Point your bishop toward h7 and prepare kingside castling.", "White holds a harmonious, lasting advantage.", [{"from":"f1","to":"d3","color":"green"},{"from":"d3","to":"h7","color":"red"}], ["h7"])
                ]
            }
        ]
    })

    # ==========================================
    # 2. THE FRENCH DEFENSE (BLACK)
    # ==========================================
    courses.append({
        "id": "french_defense_black",
        "name": "The French Defense",
        "color": "Black",
        "level": "Level 2: Tactical & Aggressive",
        "description": "Black's premier counter-attacking defense against 1. e4. Master the siege of White's d4 center in the Advance Variation and dynamic queen counterstrikes.",
        "chapters": [
            {
                "id": "fr_b_advance_siege",
                "title": "Advance Variation: The d4 Siege",
                "subtitle": "Assault White's foundation with c5, Nc6, and Qb6",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Takes Center", "White plays 1. e4.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... e6", "e7e6", True, "The French Defense", "Prepare 2... d5 to challenge White's center without allowing open e-file tactics.", "Accept that your c8 bishop is temporarily enclosed.", [{"from":"e7","to":"e6","color":"green"}], ["d5"]),
                    step("2. d4", "d2d4", False, "White Occupies Center", "White takes d4.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("2... d5", "d7d5", True, "Strike the Center!", "Hit White's e4 pawn directly and establish our immovable foothold.", "Never exchange early; force White to make a commitment.", [{"from":"d7","to":"d5","color":"green"}], ["e4"]),
                    step("3. e5", "e4e5", False, "White Advances", "White chooses 3. e5.", "", [{"from":"e4","to":"e5","color":"blue"}]),
                    step("3... c5", "c7c5", True, "Assault the d4 Base", "Immediately chip away at the base of White's pawn chain!", "Always attack the base of the chain (d4), not the head (e5).", [{"from":"c7","to":"c5","color":"green"},{"from":"c5","to":"d4","color":"red"}], ["d4"]),
                    step("4. c3", "c2c3", False, "White Cements Chain", "White plays c3.", "", [{"from":"c2","to":"c3","color":"blue"}]),
                    step("4... Nc6", "b8c6", True, "Add the Second Attacker", "Develop the knight and increase pressure on d4.", "Don't block the c-pawn; it's already on c5 doing heavy work.", [{"from":"b8","to":"c6","color":"green"},{"from":"c6","to":"d4","color":"red"}], ["d4"]),
                    step("5. Nf3", "g1f3", False, "White Defends", "White develops knight.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("5... Qb6", "d8b6", True, "Triple Battery on d4 & b2", "The trademark French move! Pressure d4 to maximum intensity and tie White down.", "Watch out for White's Bd3 traps (Milner-Barry Gambit).", [{"from":"d8","to":"b6","color":"green"},{"from":"b6","to":"d4","color":"red"},{"from":"b6","to":"b2","color":"blue"}], ["d4","b2"]),
                    step("6. a3", "a2a3", False, "White Prepares b4", "White wants to expand on the flank.", "", [{"from":"a2","to":"a3","color":"blue"}]),
                    step("6... Nh6!", "g8h6", True, "Knight Outpost Maneuver", "Develop the knight to h6 en route to f5 to bring a fourth attacker against d4!", "Don't fear Bxh6; gxh6 gives Black the open g-file and bishop pair.", [{"from":"g8","to":"h6","color":"green"},{"from":"h6","to":"f5","color":"blue"}], ["f5","d4"])
                ]
            },
            {
                "id": "fr_b_rubinstein",
                "title": "Rubinstein Variation: Effortless Equality",
                "subtitle": "Release tension with 3... dxe4 and free piece flow",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Plays e4", "Opening move.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... e6", "e7e6", True, "French Setup", "Preparing d5.", "", [{"from":"e7","to":"e6","color":"green"}]),
                    step("2. d4", "d2d4", False, "White Plays d4", "Center pawns.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("2... d5", "d7d5", True, "Pawn to d5", "Striking the center.", "", [{"from":"d7","to":"d5","color":"green"}]),
                    step("3. Nc3", "b1c3", False, "White Develops Nc3", "Guarding e4.", "", [{"from":"b1","to":"c3","color":"blue"}]),
                    step("3... dxe4", "d5e4", True, "The Rubinstein Capture", "Eliminate central tension and avoid cramped locked positions.", "Safe, reliable, and played by world champions.", [{"from":"d5","to":"e4","color":"green"}], ["e4"]),
                    step("4. Nxe4", "c3e4", False, "White Recaptures", "Centralized knight.", "", [{"from":"c3","to":"e4","color":"blue"}]),
                    step("4... Nd7", "b8d7", True, "Prepare Knight Exchange", "Develop knight to d7 to prepare Ngf6 without getting doubled pawns.", "Never play an unprepared Ngf6 allowing Nxf6+ with structure damage.", [{"from":"b8","to":"d7","color":"green"}], ["f6"]),
                    step("5. Nf3", "g1f3", False, "White Develops", "Normal flow.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("5... Ngf6", "g8f6", True, "Challenge the Knight", "Offer exchange and achieve complete equality.", "Black has no weaknesses and easy development.", [{"from":"g8","to":"f6","color":"green"}], ["e4"])
                ]
            }
        ]
    })

    # ==========================================
    # 3. THE CARO-KANN DEFENSE (WHITE)
    # ==========================================
    courses.append({
        "id": "caro_kann_white",
        "name": "The Caro-Kann Defense",
        "color": "White",
        "level": "Level 2: Tactical & Aggressive",
        "description": "Crush Black's Caro-Kann fortress. Tear open the position with the dynamic Panov-Botvinnik Attack or harass Black's bishop in the Advance Variation.",
        "chapters": [
            {
                "id": "ck_w_panov",
                "title": "Panov-Botvinnik Attack: Dynamic IQP",
                "subtitle": "Blast open the center with 4. c4!",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "King's Pawn", "Take space.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... c6", "c7c6", False, "Caro-Kann Defense", "Black prepares 2... d5.", "", [{"from":"c7","to":"c6","color":"blue"}]),
                    step("2. d4", "d2d4", True, "Full Pawn Center", "Occupy the center.", "", [{"from":"d2","to":"d4","color":"green"}]),
                    step("2... d5", "d7d5", False, "Black Strikes d5", "Contesting e4.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("3. exd5", "e4d5", True, "Open the Position", "Exchange on d5 to prepare our flank strike.", "Removes Black's solid pawn chain.", [{"from":"e4","to":"d5","color":"green"}]),
                    step("3... cxd5", "c6d5", False, "Black Recaptures", "Maintaining pawn symmetry.", "", [{"from":"c6","to":"d5","color":"blue"}]),
                    step("4. c4!", "c2c4", True, "The Panov Pawn Lever", "Blast open the c-file and challenge Black's d5 anchor!", "This transforms the game into a razor-sharp attacking contest.", [{"from":"c2","to":"c4","color":"green"},{"from":"c4","to":"d5","color":"red"}], ["d5","c6"]),
                    step("4... Nf6", "g8f6", False, "Black Develops", "Guarding d5.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("5. Nc3", "b1c3", True, "Piling Pressure on d5", "Develop the knight with direct pressure on d5.", "Keep active pressure on Black's center.", [{"from":"b1","to":"c3","color":"green"},{"from":"c3","to":"d5","color":"red"}], ["d5"]),
                    step("5... e6", "e7e6", False, "Black Fortifies", "Closing the bishop inside.", "", [{"from":"e7","to":"e6","color":"blue"}]),
                    step("6. Nf3", "g1f3", True, "Harmonious Piece Outpost", "Develop and maintain central dominance.", "White holds the initiative and rapid attacking potential.", [{"from":"g1","to":"f3","color":"green"}], ["e5"])
                ]
            },
            {
                "id": "ck_w_advance",
                "title": "Advance Variation: Tal Caveman Attack",
                "subtitle": "Play 3. e5 and harass Black's bishop with 4. h4!",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "Pawn to e4", "Standard opening.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... c6", "c7c6", False, "Caro-Kann", "Solid preparation.", "", [{"from":"c7","to":"c6","color":"blue"}]),
                    step("2. d4", "d2d4", True, "Pawn to d4", "Take the center.", "", [{"from":"d2","to":"d4","color":"green"}]),
                    step("2... d5", "d7d5", False, "Black Plays d5", "Direct challenge.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("3. e5", "e4e5", True, "The Advance Push", "Gain space and force Black's bishop out to f5.", "Locking the center allows White to attack freely on the wings.", [{"from":"e4","to":"e5","color":"green"}], ["e5"]),
                    step("3... Bf5", "c8f5", False, "Bishop to f5", "Black develops their light-squared bishop outside the chain.", "", [{"from":"c8f5","color":"blue"}]),
                    step("4. h4!", "h2h4", True, "The Tal Bishop Hunt!", "Threaten g4 and h5 to trap and suffocating Black's active bishop!", "Black must spend a move on 4... h5 or 4... h6 to survive.", [{"from":"h2","to":"h4","color":"green"},{"from":"h4","to":"h5","color":"blue"}], ["g4","h5"])
                ]
            }
        ]
    })

    # ==========================================
    # 3. THE CARO-KANN DEFENSE (BLACK)
    # ==========================================
    courses.append({
        "id": "caro_kann_black",
        "name": "The Caro-Kann Defense",
        "color": "Black",
        "level": "Level 1: Fundamentals",
        "description": "Black's most rock-solid defensive shield against 1. e4. Develop the light-squared bishop freely before locking the center, leaving zero weaknesses.",
        "chapters": [
            {
                "id": "ck_b_classical",
                "title": "Classical Mainline: The Capablanca Wall",
                "subtitle": "Solid development with 3... dxe4 and 4... Bf5",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Seizes Center", "White opens e4.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... c6", "c7c6", True, "The Caro-Kann Pawn", "Prepare to strike 2... d5 with solid pawn reinforcement.", "Unlike the French, our light-squared bishop will not be trapped!", [{"from":"c7","to":"c6","color":"green"}], ["d5"]),
                    step("2. d4", "d2d4", False, "White Occupies Center", "Full pawn duo.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("2... d5", "d7d5", True, "Strike the Center!", "Challenge White's e4 pawn directly.", "Our pawn structure remains healthy and compact.", [{"from":"d7","to":"d5","color":"green"}], ["e4"]),
                    step("3. Nc3", "b1c3", False, "White Protects e4", "Mainline development.", "", [{"from":"b1","to":"c3","color":"blue"}]),
                    step("3... dxe4", "d5e4", True, "Exchange Central Pawns", "Liquidate White's central pawn to open squares for our minor pieces.", "A cornerstone of Capablanca's style.", [{"from":"d5","to":"e4","color":"green"}], ["e4"]),
                    step("4. Nxe4", "c3e4", False, "White Recaptures", "Knight centralized.", "", [{"from":"c3","to":"e4","color":"blue"}]),
                    step("4... Bf5", "c8f5", True, "Activate the Bishop!", "Develop the bishop outside the pawn chain before playing e6!", "NEVER play 4... e6 first — you would lock the bishop in!", [{"from":"c8","to":"f5","color":"green"},{"from":"f5","to":"e4","color":"red"}], ["e4"]),
                    step("5. Ng3", "e4g3", False, "Knight Hits Bishop", "White knight retreats with attack.", "", [{"from":"e4","to":"g3","color":"blue"}]),
                    step("5... Bg6", "f5g6", True, "Tuck Bishop Safely on g6", "Preserve the bishop on the comfortable h7-b1 diagonal.", "Be ready to create an escape hatch on h7 if White plays h4.", [{"from":"f5","to":"g6","color":"green"}], ["g6"]),
                    step("6. h4", "h2h4", False, "White Threatens Bishop Trap", "White threatens h5.", "", [{"from":"h2","to":"h4","color":"blue"}]),
                    step("6... h6", "h7h6", True, "Create Escape Loft on h7", "Give the bishop a safe haven on h7 against h5.", "Essential move: prevents White from trapping your proud bishop.", [{"from":"h7","to":"h6","color":"green"},{"from":"g6","to":"h7","color":"blue"}], ["h7"])
                ]
            },
            {
                "id": "ck_b_advance",
                "title": "Advance Variation: The c5 Counter",
                "subtitle": "Play 3... Bf5 and dismantle White's center with c5!",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Plays e4", "King's pawn.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... c6", "c7c6", True, "Caro-Kann", "Solid foundation.", "", [{"from":"c7","to":"c6","color":"green"}]),
                    step("2. d4", "d2d4", False, "White Plays d4", "Center.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("2... d5", "d7d5", True, "Pawn to d5", "Challenge center.", "", [{"from":"d7","to":"d5","color":"green"}]),
                    step("3. e5", "e4e5", False, "White Advances", "Space wedge.", "", [{"from":"e4","to":"e5","color":"blue"}]),
                    step("3... Bf5", "c8f5", True, "Bishop Active on f5", "Place the bishop outside the chain before solidifying.", "Don't delay this move.", [{"from":"c8","to":"f5","color":"green"}], ["f5"]),
                    step("4. Nf3", "g1f3", False, "White Develops", "Developing move.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("4... e6", "e7e6", True, "Lock the Granite Chain", "Seal the pawn chain. The bishop is already free outside!", "Black's structure is bulletproof.", [{"from":"e7","to":"e6","color":"green"}], ["d5","e6"]),
                    step("5. Be2", "f1e2", False, "White Develops Bishop", "Quiet approach.", "", [{"from":"f1","to":"e2","color":"blue"}]),
                    step("5... c5", "c6c5", True, "Break the Center with c5!", "Strike directly at White's d4 foundation!", "Black achieves an ideal active French position without the bad bishop.", [{"from":"c6","to":"c5","color":"green"},{"from":"c5","to":"d4","color":"red"}], ["d4"])
                ]
            }
        ]
    })

    # ==========================================
    # 4. THE SICILIAN DEFENSE (WHITE)
    # ==========================================
    courses.append({
        "id": "sicilian_defense_white",
        "name": "The Sicilian Defense",
        "color": "White",
        "level": "Level 2: Tactical & Aggressive",
        "description": "White's lethal weapons against 1... c5. Master the blistering kingside attack of the Grand Prix (2. Nc3 + 3. f4) and the classical Open Sicilian Yugoslav Attack.",
        "chapters": [
            {
                "id": "sic_w_grand_prix",
                "title": "Grand Prix Attack: Direct King Hunt",
                "subtitle": "Sidestep deep theory with an aggressive 3. f4 launch",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "Stake Center", "Start with e4.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... c5", "c7c5", False, "Sicilian Defense", "Black plays for an asymmetrical fight.", "", [{"from":"c7","to":"c5","color":"blue"}]),
                    step("2. Nc3", "b1c3", True, "Closed Sicilian Prep", "Control d5 and avoid immediate d4 exchanges.", "Flexible knight placement.", [{"from":"b1","to":"c3","color":"green"}], ["d5"]),
                    step("2... Nc6", "b8c6", False, "Black Develops Knight", "Fighting for central control.", "", [{"from":"b8","to":"c6","color":"blue"}]),
                    step("3. f4", "f2f4", True, "The Grand Prix Battery", "Push the f-pawn to prepare an explosive kingside assault!", "Target the f7 square and open lines for your rook.", [{"from":"f2","to":"f4","color":"green"},{"from":"f4","to":"f5","color":"blue"}], ["f5"]),
                    step("3... g6", "g7g6", False, "Black Fianchettos", "Preparing Bg7.", "", [{"from":"g7","to":"g6","color":"blue"}]),
                    step("4. Nf3", "g1f3", True, "Develop Knight", "Control e5 and prepare kingside castling.", "Coordinate pieces harmoniously.", [{"from":"g1","to":"f3","color":"green"}], ["e5"]),
                    step("4... Bg7", "f8g7", False, "Bishop to g7", "Long diagonal pressure.", "", [{"from":"f8","to":"g7","color":"blue"}]),
                    step("5. Bc4", "f1c4", True, "Point at f7", "Place the bishop on the dangerous a2-g8 diagonal aiming at Black's king.", "Watch out for Black playing e6 and d5 later; keep control.", [{"from":"f1","to":"c4","color":"green"},{"from":"c4","to":"f7","color":"red"}], ["f7"])
                ]
            },
            {
                "id": "sic_w_open_yugoslav",
                "title": "Open Sicilian: Yugoslav Storm vs Dragon",
                "subtitle": "Castle queenside and launch the h-pawn battering ram",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "King's Pawn", "Take center.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... c5", "c7c5", False, "Sicilian", "Asymmetrical reply.", "", [{"from":"c7","to":"c5","color":"blue"}]),
                    step("2. Nf3", "g1f3", True, "Knight to f3", "Prepare d4.", "", [{"from":"g1","to":"f3","color":"green"}]),
                    step("2... d6", "d7d6", False, "Pawn to d6", "Guards e5.", "", [{"from":"d7","to":"d6","color":"blue"}]),
                    step("3. d4", "d2d4", True, "Open the Center!", "Break open the position to give your pieces maximum mobility.", "The hallmark of the Open Sicilian.", [{"from":"d2","to":"d4","color":"green"}], ["d4"]),
                    step("3... cxd4", "c5d4", False, "Black Captures", "Exchanging flank pawn for center pawn.", "", [{"from":"c5","to":"d4","color":"blue"}]),
                    step("4. Nxd4", "f3d4", True, "Recapture in the Center", "Dominant knight post on d4.", "", [{"from":"f3","to":"d4","color":"green"}], ["d4"]),
                    step("4... Nf6", "g8f6", False, "Black Attacks e4", "Developing knight.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("5. Nc3", "b1c3", True, "Defend e4", "Solid knight post.", "", [{"from":"b1","to":"c3","color":"green"}], ["e4"]),
                    step("5... g6", "g7g6", False, "Dragon Setup", "Black chooses the Dragon.", "", [{"from":"g7","to":"g6","color":"blue"}]),
                    step("6. Be3", "c1e3", True, "The Yugoslav Setup (Be3 & f3)", "Anchor the center, support Qd2, and prepare opposite-side castling with an all-out kingside pawn storm!", "White is ready to crush Black's king.", [{"from":"c1","to":"e3","color":"green"},{"from":"e3","to":"d4","color":"blue"}], ["d4"])
                ]
            }
        ]
    })

    # ==========================================
    # 4. THE SICILIAN DEFENSE (BLACK)
    # ==========================================
    courses.append({
        "id": "sicilian_defense_black",
        "name": "The Sicilian Defense",
        "color": "Black",
        "level": "Level 3: Strategic & Deep",
        "description": "Black's most feared and combative weapon against 1. e4. Master the fire-breathing Dragon Variation and the legendary Kasparov-style counter-attacks.",
        "chapters": [
            {
                "id": "sic_b_dragon",
                "title": "The Sicilian Dragon: Fire-Breathing Counter",
                "subtitle": "Fianchetto the dark-squared bishop to dominate the long diagonal",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Plays e4", "Standard opening.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... c5", "c7c5", True, "The Sicilian Defense", "Fight for the d4 square from the flank and play for a dynamic imbalance.", "Most fighting response to 1. e4 in chess.", [{"from":"c7","to":"c5","color":"green"}], ["d4"]),
                    step("2. Nf3", "g1f3", False, "White Develops Knight", "Preparing d4.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("2... d6", "d7d6", True, "Anchor the e5 Outpost", "Prevent White's e5 push and prepare Nf6.", "Keep your options open for both the Dragon and Najdorf.", [{"from":"d7","to":"d6","color":"green"}], ["e5"]),
                    step("3. d4", "d2d4", False, "White Strikes Center", "White opens the center.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("3... cxd4", "c5d4", True, "Trade Flank for Center Pawn", "Win the central pawn majority (2 vs 1 in the center) and open the c-file for counterplay.", "Always exchange on d4 in the Open Sicilian.", [{"from":"c5","to":"d4","color":"green"}], ["c-file"]),
                    step("4. Nxd4", "f3d4", False, "White Recaptures", "Knight on d4.", "", [{"from":"f3","to":"d4","color":"blue"}]),
                    step("4... Nf6", "g8f6", True, "Develop with Attack", "Develop the knight and attack White's e4 pawn, forcing White to defend.", "Never play passive moves; attack e4 immediately.", [{"from":"g8","to":"f6","color":"green"},{"from":"f6","to":"e4","color":"red"}], ["e4"]),
                    step("5. Nc3", "b1c3", False, "White Guards e4", "White defends e4.", "", [{"from":"b1","to":"c3","color":"blue"}]),
                    step("5... g6!", "g7g6", True, "Enter the Dragon!", "The dragon awakens! Prepare to fianchetto your bishop to g7 to blast down the long diagonal.", "Be mentally prepared for White's opposite-side castling attack.", [{"from":"g7","to":"g6","color":"green"},{"from":"f8","to":"g7","color":"blue"}], ["g7","h8-a1"]),
                    step("6. Be3", "c1e3", False, "White Prepares Yugoslav", "White plays Be3.", "", [{"from":"c1","to":"e3","color":"blue"}]),
                    step("6... Bg7", "f8g7", True, "The Dragon Bishop Roars", "Occupy the longest diagonal on the board! Pressure d4, b2, and White's queenside.", "Never trade this bishop lightly — it is your King's chief defender.", [{"from":"f8","to":"g7","color":"green"},{"from":"g7","to":"a1","color":"blue"}], ["d4","b2","a1"]),
                    step("7. f3", "f2f3", False, "White Fortifies e4", "Preventing Ng4.", "", [{"from":"f2","to":"f3","color":"blue"}]),
                    step("7... O-O", "e8g8", True, "Tuck King in Safety", "Castle kingside! Your king is shielded behind the dark-square fortress.", "Now prepare counterplay down the open c-file with Nc6, Bd7, and Rc8.", [{"from":"e8","to":"g8","color":"green"}], ["g8"])
                ]
            },
            {
                "id": "sic_b_scheveningen",
                "title": "Classical Sicilian: Solid Center (e6 + d6)",
                "subtitle": "The flexible small-center setup favored by Kasparov",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Opens e4", "Standard move.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... c5", "c7c5", True, "Sicilian Defense", "Flank control.", "", [{"from":"c7","to":"c5","color":"green"}]),
                    step("2. Nf3", "g1f3", False, "White Prepares d4", "Knight to f3.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("2... e6", "e7e6", True, "The Flexible e6 Setup", "Open diagonal for dark-squared bishop and control d5.", "Can transpose into many flexible counter-attacking lines.", [{"from":"e7","to":"e6","color":"green"}], ["d5"]),
                    step("3. d4", "d2d4", False, "White Plays d4", "Opening center.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("3... cxd4", "c5d4", True, "Trade on d4", "Win center majority.", "", [{"from":"c5","to":"d4","color":"green"}]),
                    step("4. Nxd4", "f3d4", False, "White Recaptures", "Knight centralized.", "", [{"from":"f3","to":"d4","color":"blue"}]),
                    step("4... Nc6", "b8c6", True, "Challenge White's Knight", "Develop knight and immediately dispute White's d4 outpost.", "Keep Black's piece coordination active.", [{"from":"b8","to":"c6","color":"green"},{"from":"c6","to":"d4","color":"red"}], ["d4"]),
                    step("5. Nc3", "b1c3", False, "White Develops", "Guards e4.", "", [{"from":"b1","to":"c3","color":"blue"}]),
                    step("5... d6", "d7d6", True, "The Small Center Pawn Wall", "Establish the famous 'little center' (d6 + e6). White cannot break through.", "A favorite fortress of world champions.", [{"from":"d7","to":"d6","color":"green"}], ["d6","e6"])
                ]
            }
        ]
    })

    # ==========================================
    # 5. THE QUEEN'S GAMBIT (WHITE)
    # ==========================================
    courses.append({
        "id": "queens_gambit_white",
        "name": "The Queen's Gambit",
        "color": "White",
        "level": "Level 1: Fundamentals",
        "description": "The gold standard of classical chess. Offer a flank pawn on move 2 to achieve complete central domination and torture Black with positional pressure.",
        "chapters": [
            {
                "id": "qg_w_exchange",
                "title": "QGD: Exchange Variation & Pin",
                "subtitle": "Pin Black's knight with Bg5 and dismantle their structure",
                "orientation": "white",
                "steps": [
                    step("1. d4", "d2d4", True, "Queen's Pawn Opening", "Stake the center and open lines for Queen and Bishop.", "The d4 push avoids open tactical gambits like 1... e5.", [{"from":"d2","to":"d4","color":"green"}], ["e5","c5"]),
                    step("1... d5", "d7d5", False, "Black Equalizes", "Direct symmetrical contest.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("2. c4", "c2c4", True, "The Queen's Gambit!", "Offer the c4 pawn! If Black takes, we gain the entire center with e4.", "This is not a real gambit because White easily wins the pawn back.", [{"from":"c2","to":"c4","color":"green"},{"from":"c4","to":"d5","color":"red"}], ["d5"]),
                    step("2... e6", "e7e6", False, "Queen's Gambit Declined (QGD)", "Black declines the pawn and fortifies d5.", "", [{"from":"e7","to":"e6","color":"blue"}]),
                    step("3. Nc3", "b1c3", True, "Develop with Central Pressure", "Develop the knight to add a second attacker to d5 and support e4.", "Don't block the c-pawn with an early Nc3 before playing c4!", [{"from":"b1","to":"c3","color":"green"},{"from":"c3","to":"d5","color":"red"}], ["d5","e4"]),
                    step("3... Nf6", "g8f6", False, "Black Guards d5", "Black develops their kingside knight.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("4. cxd5", "c4d5", True, "The Carlsbad Exchange", "Fix Black's pawn structure into the classical Carlsbad formation.", "Gives White a clear strategic plan: the Minority Attack (b4-b5).", [{"from":"c4","to":"d5","color":"green"}], ["d5"]),
                    step("4... exd5", "e6d5", False, "Black Recaptures", "Black opens their light-squared bishop diagonal.", "", [{"from":"e6","to":"d5","color":"blue"}]),
                    step("5. Bg5", "c1g5", True, "The Deadly Pin", "Pin Black's knight to their queen and remove their defense of d5!", "A devastating positional pin.", [{"from":"c1","to":"g5","color":"green"},{"from":"g5","to":"d8","color":"red"}], ["f6","d8"]),
                    step("5... Be7", "f8e7", False, "Black Breaks Pin", "Black shields their queen.", "", [{"from":"f8","to":"e7","color":"blue"}]),
                    step("6. e3", "e1e3", True, "Solidify Central Bastion", "Support d4 and open lines for the light-squared bishop. White is in full control.", "Complete harmony with zero structural defects.", [{"from":"e2","to":"e3","color":"green"}], ["d4","f4"])
                ]
            },
            {
                "id": "qg_w_accepted",
                "title": "Queen's Gambit Accepted: Punish the Grab",
                "subtitle": "Punish Black for greedily capturing on c4",
                "orientation": "white",
                "steps": [
                    step("1. d4", "d2d4", True, "Pawn to d4", "Take center.", "", [{"from":"d2","to":"d4","color":"green"}]),
                    step("1... d5", "d7d5", False, "Black Plays d5", "Symmetrical.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("2. c4", "c2c4", True, "Queen's Gambit", "Offer c4.", "", [{"from":"c2","to":"c4","color":"green"}]),
                    step("2... dxc4", "d5c4", False, "Queen's Gambit Accepted (QGA)", "Black grabs the pawn.", "", [{"from":"d5","to":"c4","color":"blue"}]),
                    step("3. Nf3", "g1f3", True, "Prevent e5 Counterstrike", "Develop the knight to f3 to stop Black from playing an early e5 break.", "Crucial precision move before recapturing.", [{"from":"g1","to":"f3","color":"green"}], ["e5"]),
                    step("3... Nf6", "g8f6", False, "Black Develops", "Normal flow.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("4. e3", "e2e3", True, "Unleash Bishop on c4", "Open the f1 bishop's diagonal to reclaim c4 with interest.", "If Black tries to defend with 4... b5?, White blows it open with a4! and wins.", [{"from":"e2","to":"e3","color":"green"},{"from":"f1","to":"c4","color":"red"}], ["c4"]),
                    step("4... e6", "e7e6", False, "Black Develops Solidly", "Opening dark bishop.", "", [{"from":"e7","to":"e6","color":"blue"}]),
                    step("5. Bxc4", "f1c4", True, "Recapture with Dominance", "Win back the gambit pawn with a massive lead in development.", "White stands completely superior.", [{"from":"f1","to":"c4","color":"green"}], ["c4"])
                ]
            }
        ]
    })

    # ==========================================
    # 5. THE QUEEN'S GAMBIT (BLACK)
    # ==========================================
    courses.append({
        "id": "queens_gambit_black",
        "name": "The Queen's Gambit",
        "color": "Black",
        "level": "Level 1: Fundamentals",
        "description": "Master Black's most resilient defenses against 1. d4. Solve Black's bad bishop with the Tartakower/Capablanca freeing maneuvers and catch White in the Lasker Trap.",
        "chapters": [
            {
                "id": "qg_b_tartakower",
                "title": "QGD: Tartakower Freeing Maneuver",
                "subtitle": "Solve the light-squared bishop problem with ...b6 & ...Bb7",
                "orientation": "black",
                "steps": [
                    step("1. d4", "d2d4", False, "White Opens d4", "Queen's pawn.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("1... d5", "d7d5", True, "Claim Symmetrical Center", "Stake our claim on the center and prevent White from rolling forward with e4.", "Solid, principled response to 1. d4.", [{"from":"d7","to":"d5","color":"green"}], ["e4"]),
                    step("2. c4", "c2c4", False, "White Plays c4 Gambit", "Challenging d5.", "", [{"from":"c2","to":"c4","color":"blue"}]),
                    step("2... e6", "e7e6", True, "Decline the Gambit", "Fortify the d5 bastion with e6. Our central pawn chain is unbreakable.", "Don't play 2... c6 yet if aiming for Tartakower lines.", [{"from":"e7","to":"e6","color":"green"}], ["d5"]),
                    step("3. Nc3", "b1c3", False, "White Develops Nc3", "Piling on d5.", "", [{"from":"b1","to":"c3","color":"blue"}]),
                    step("3... Nf6", "g8f6", True, "Defend d5 Naturally", "Develop the kingside knight and guard the critical d5 and e4 squares.", "Avoid passive defense.", [{"from":"g8","to":"f6","color":"green"}], ["d5","e4"]),
                    step("4. Bg5", "c1g5", False, "White Pins Knight", "Pinning knight to queen.", "", [{"from":"c1","to":"g5","color":"blue"}]),
                    step("4... Be7", "f8e7", True, "Neutralize the Pin", "Interpose with the bishop to break the pin on your queen and prepare castling.", "Always break this pin before embarking on ambitious pawn breaks.", [{"from":"f8","to":"e7","color":"green"}], ["d8"]),
                    step("5. e3", "e2e3", False, "White Fortifies d4", "Closing bishop inside.", "", [{"from":"e2","to":"e3","color":"blue"}]),
                    step("5... O-O", "e8g8", True, "Safeguard King", "Castle into absolute safety.", "King safety first.", [{"from":"e8","to":"g8","color":"green"}], ["g8"]),
                    step("6. Nf3", "g1f3", False, "White Develops", "Standard knight move.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("6... h6", "h7h6", True, "Ask White's Bishop the Question", "Challenge the bishop! Force White to either trade or retreat to h4.", "Gives your king an essential luft square on h7.", [{"from":"h7","to":"h6","color":"green"},{"from":"h6","to":"g5","color":"red"}], ["g5"]),
                    step("7. Bh4", "g5h4", False, "White Retreats", "Maintaining the pin line.", "", [{"from":"g5","to":"h4","color":"blue"}]),
                    step("7... b6!", "b7b6", True, "The Tartakower Liberation!", "Prepare Bb7! This breathes life into Black's famous 'bad bishop' and achieves lasting equality.", "The bishop becomes a powerhouse on the long diagonal.", [{"from":"b7","to":"b6","color":"green"},{"from":"c8","to":"b7","color":"blue"}], ["b7","a8-h1"])
                ]
            },
            {
                "id": "qg_b_albin_lasker",
                "title": "The Albin Counter-Gambit: Lasker Trap",
                "subtitle": "Shock White with 2... e5! and win their Queen on move 8!",
                "orientation": "black",
                "steps": [
                    step("1. d4", "d2d4", False, "White Plays d4", "Standard opening.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("1... d5", "d7d5", True, "Black Plays d5", "Claim center.", "", [{"from":"d7","to":"d5","color":"green"}]),
                    step("2. c4", "c2c4", False, "Queen's Gambit", "White offers c4.", "", [{"from":"c2","to":"c4","color":"blue"}]),
                    step("2... e5!", "e7e5", True, "The Albin Counter-Gambit!", "Counterattack White's center immediately with 2... e5! Sacrificing a pawn for dynamic initiative.", "Demands exact tactical play from White.", [{"from":"e7","to":"e5","color":"green"}], ["d4"]),
                    step("3. dxe5", "d4e5", False, "White Takes e5", "Accepting the gambit.", "", [{"from":"d4","to":"e5","color":"blue"}]),
                    step("3... d4!", "d5d4", True, "The Wedge Pawn!", "Push d4! This pawn is a thorn in White's side, depriving their knight of the c3 square.", "Never capture on e5; push to d4!", [{"from":"d5","to":"d4","color":"green"}], ["c3","e3"]),
                    step("4. e3?", "e2e3", False, "White Makes the Natural Blunder", "Trying to eliminate your pawn.", "", [{"from":"e2","to":"e3","color":"blue"}]),
                    step("4... Bb4+", "f8b4", True, "Check the King!", "Force White's bishop to block on d2.", "White falls deeper into the web.", [{"from":"f8","to":"b4","color":"green"}], ["e1"]),
                    step("5. Bd2", "c1d2", False, "White Blocks", "Thinking they are safe.", "", [{"from":"c1","to":"d2","color":"blue"}]),
                    step("5... dxe3!", "d4e3", True, "The Queen Sacrifice Bait!", "Take on e3! If White greedily plays 6. Bxb4??, they walk into instant doom!", "A historic tactical brilliancy by Lasker.", [{"from":"d4","to":"e3","color":"green"},{"from":"e3","to":"f2","color":"red"}], ["f2"]),
                    step("6. Bxb4?", "d2b4", False, "White Bites the Poison", "Grabbing the bishop.", "", [{"from":"d2","to":"b4","color":"blue"}]),
                    step("6... exf2+", "e3f2", True, "Strip White's Castling", "Check the king! White cannot take with the king because their Queen on d1 hangs!", "Forced King move to e2.", [{"from":"e3","to":"f2","color":"green"}], ["e1"]),
                    step("7. Ke2", "e1e2", False, "King Flees", "Forced escape.", "", [{"from":"e1","to":"e2","color":"blue"}]),
                    step("7... fxg1=N+!", "f2g1n", True, "The Knight Underpromotion!", "Underpromote to a KNIGHT with check! If you promoted to a Queen, White plays Qxd8+ and trades off.", "World-famous underpromotion winning White's Queen on the next move!", [{"from":"f2","to":"g1","color":"green"},{"from":"g1","to":"e2","color":"red"}], ["e2"]),
                    step("8. Rxg1", "h1g1", False, "Rook Takes Knight", "Forced capture.", "", [{"from":"h1","to":"g1","color":"blue"}]),
                    step("8... Bg4+", "c8g4", True, "Skewer White's Queen!", "Bishop pins and skewers White's Queen to the King! White loses their Queen and resigns.", "Absolute tactical perfection.", [{"from":"c8","to":"g4","color":"green"},{"from":"g4","to":"d1","color":"red"}], ["d1"])
                ]
            }
        ]
    })

    return courses
