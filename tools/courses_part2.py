# Openings 6 to 10: London System, Vienna Game, King's Indian, Scandinavian, Ruy Lopez

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

def get_part2_courses():
    courses = []

    # ==========================================
    # 6. THE LONDON SYSTEM (WHITE)
    # ==========================================
    courses.append({
        "id": "london_system_white",
        "name": "The London System",
        "color": "White",
        "level": "Level 1: Fundamentals",
        "description": "White's universal, bulletproof opening setup. Build the unbreakable golden pyramid (d4, c3, e3, Bf4, Nf3) and dominate without memorizing sharp theory.",
        "chapters": [
            {
                "id": "lon_w_classical",
                "title": "The Golden London Pyramid vs 1... d5",
                "subtitle": "Complete setup with 2. Bf4, 3. e3, and 4. c3",
                "orientation": "white",
                "steps": [
                    step("1. d4", "d2d4", True, "The Foundation Stone", "Stake central space and claim e5 and c5.", "Avoid reckless deviations; keep your pawn firmly on d4.", [{"from":"d2","to":"d4","color":"green"}], ["e5","c5"]),
                    step("1... d5", "d7d5", False, "Black Responds", "Mirroring central stake.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("2. Bf4", "c1f4", True, "Develop Bishop Outside Chain", "The signature London move! Bring the dark-squared bishop outside the pawn chain before playing e3.", "CRITICAL: Always play Bf4 BEFORE playing e3!", [{"from":"c1","to":"f4","color":"green"}], ["f4"]),
                    step("2... Nf6", "g8f6", False, "Black Develops", "Guarding e4 and d5.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("3. e3", "e2e3", True, "Solidify Bishop & Center", "Lock the e3 pawn. The bishop is already free outside, so your structure has no bad pieces.", "Opens the diagonal for your light-squared bishop.", [{"from":"e2","to":"e3","color":"green"}], ["d4","f4"]),
                    step("3... c5", "c7c5", False, "Black Strikes Flank", "Testing White's d4 center.", "", [{"from":"c7","to":"c5","color":"blue"},{"from":"c5","to":"d4","color":"red"}]),
                    step("4. c3", "c2c3", True, "Complete the Golden Pyramid!", "The unbreakable c3-d4-e3 pyramid is completed! Even if Black takes on d4, recapture with c-pawn.", "Never leave d4 undefended.", [{"from":"c2","to":"c3","color":"green"}], ["c3","d4","e3"]),
                    step("4... Nc6", "b8c6", False, "Black Pressures Center", "Adding knight attacker.", "", [{"from":"b8","to":"c6","color":"blue"}]),
                    step("5. Nd2", "b1d2", True, "Harmonious Knight Post", "Develop the knight to d2 to support an eventual e4 push and prevent Ne4 outposts.", "Leaves the c-file free and maintains pyramid cohesion.", [{"from":"b1","to":"d2","color":"green"}], ["e4"])
                ]
            },
            {
                "id": "lon_w_jobava",
                "title": "Jobava London: Aggressive 2. Nc3 Attack",
                "subtitle": "Sharp modern attack with 1. d4, 2. Nc3, and 3. Bf4",
                "orientation": "white",
                "steps": [
                    step("1. d4", "d2d4", True, "Pawn to d4", "Central foundation.", "", [{"from":"d2","to":"d4","color":"green"}]),
                    step("1... d5", "d7d5", False, "Black Plays d5", "Symmetrical.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("2. Nc3", "b1c3", True, "The Jobava Knight", "Develop the knight directly to c3, threatening an immediate e4 central blast!", "An aggressive modern alternative to classical c3 lines.", [{"from":"b1","to":"c3","color":"green"},{"from":"c3","to":"e4","color":"blue"}], ["e4"]),
                    step("2... Nf6", "g8f6", False, "Black Stops e4", "Guarding e4.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("3. Bf4", "c1f4", True, "The Active London Bishop", "Place the bishop on f4 with direct sight on c7 and e5.", "Prepare Nb5 threats if Black is careless.", [{"from":"c1","to":"f4","color":"green"},{"from":"f4","to":"c7","color":"red"}], ["c7"]),
                    step("3... c6", "c7c6", False, "Black Guards c7", "Solidifying c7.", "", [{"from":"c7","to":"c6","color":"blue"}]),
                    step("4. e3", "e2e3", True, "Support Bishop & Prepare Bd3", "Cement the center and prepare active kingside piece play.", "White holds great attacking prospects.", [{"from":"e2","to":"e3","color":"green"}], ["d4"])
                ]
            }
        ]
    })

    # ==========================================
    # 6. THE LONDON SYSTEM (BLACK)
    # ==========================================
    courses.append({
        "id": "london_system_black",
        "name": "The London System",
        "color": "Black",
        "level": "Level 2: Tactical & Aggressive",
        "description": "Black's active, dynamic refutations against the popular London System. Punish White's early bishop sorties with Qb6, c4, and kingside counterplay.",
        "chapters": [
            {
                "id": "lon_b_queen_infil",
                "title": "Anti-London: Queen Infiltration (Qb6 & c4)",
                "subtitle": "Punish 2. Bf4 by attacking the abandoned b2 square",
                "orientation": "black",
                "steps": [
                    step("1. d4", "d2d4", False, "White Starts d4", "Queen's pawn.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("1... d5", "d7d5", True, "Stake the Center", "Control e4 and c4.", "", [{"from":"d7","to":"d5","color":"green"}]),
                    step("2. Bf4", "c1f4", False, "White Plays London Bishop", "Bishop outside pawn chain.", "", [{"from":"c1","to":"f4","color":"blue"}]),
                    step("2... c5", "c7c5", True, "Immediate Flank Challenge", "Strike at White's d4 pawn before White can build their pyramid with c3!", "Always challenge d4 immediately against the London.", [{"from":"c7","to":"c5","color":"green"},{"from":"c5","to":"d4","color":"red"}], ["d4"]),
                    step("3. e3", "e2e3", False, "White Defends with e3", "White closes chain.", "", [{"from":"e2","to":"e3","color":"blue"}]),
                    step("3... Nc6", "b8c6", True, "Develop Knight with Pressure", "Add pressure against d4.", "Natural piece coordination.", [{"from":"b8","to":"c6","color":"green"},{"from":"c6","to":"d4","color":"red"}], ["d4"]),
                    step("4. c3", "c2c3", False, "White Cements Pyramid", "White plays c3.", "", [{"from":"c2","to":"c3","color":"blue"}]),
                    step("4... Qb6!", "d8b6", True, "Target the Abandoned b2 Pawn!", "White's bishop left b2 completely unguarded! Force White's queen or rook to defend passively.", "A classic refutation punishing the bishop's absence.", [{"from":"d8","to":"b6","color":"green"},{"from":"b6","to":"b2","color":"red"}], ["b2"]),
                    step("5. Qb3", "d1b3", False, "White Queen Defends", "White offers queen trade.", "", [{"from":"d1","to":"b3","color":"blue"}]),
                    step("5... c4!", "c5c4", True, "Lock the Queen & Kick it Away!", "Advance c4 with tempo! Kick White's queen and seize immense queenside space.", "Do NOT trade queens on b3; push c4!", [{"from":"c5","to":"c4","color":"green"},{"from":"c4","to":"b3","color":"red"}], ["b3"]),
                    step("6. Qc2", "b3c2", False, "Queen Retreats to c2", "Forced retreat.", "", [{"from":"b3","to":"c2","color":"blue"}]),
                    step("6... Bf5!", "c8f5", True, "Tactical Bishop Deflection!", "Develop your bishop with tempo! If White plays 7. Qxf5??, you play Qxb2! trapping the rook on a1!", "Brilliant tactical resource cementing Black's complete initiative.", [{"from":"c8","to":"f5","color":"green"},{"from":"f5","to":"c2","color":"red"}], ["c2","b2"])
                ]
            },
            {
                "id": "lon_b_kid_setup",
                "title": "King's Indian Counter vs London",
                "subtitle": "Fianchetto the dark bishop to disarm the London pyramid",
                "orientation": "black",
                "steps": [
                    step("1. d4", "d2d4", False, "White Plays d4", "Opening pawn.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("1... Nf6", "g8f6", True, "Flexible Knight Move", "Control e4 and d5 flexibly.", "", [{"from":"g8","to":"f6","color":"green"}]),
                    step("2. Bf4", "c1f4", False, "White Plays London", "Bishop on f4.", "", [{"from":"c1","to":"f4","color":"blue"}]),
                    step("2... g6", "g7g6", True, "Fianchetto Preparation", "Neutralize White's dark-squared bishop by preparing Bg7.", "The bishop on g7 bites on granite.", [{"from":"g7","to":"g6","color":"green"}], ["g7"]),
                    step("3. e3", "e2e3", False, "White Plays e3", "Closing chain.", "", [{"from":"e2","to":"e3","color":"blue"}]),
                    step("3... Bg7", "f8g7", True, "Occupy Long Diagonal", "Develop the bishop and control e5.", "Solid fortress.", [{"from":"f8","to":"g7","color":"green"}], ["e5"]),
                    step("4. Nf3", "g1f3", False, "White Develops", "Knight move.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("4... O-O", "e8g8", True, "Castle Safely", "King is shielded.", "", [{"from":"e8","to":"g8","color":"green"}], ["g8"]),
                    step("5. Be2", "f1e2", False, "White Develops Bishop", "Quiet move.", "", [{"from":"f1","to":"e2","color":"blue"}]),
                    step("5... d6", "d7d6", True, "Prepare e5 Strike", "Prepare the e7-e5 central break to blast White's bishop away.", "Black achieves effortless counterplay.", [{"from":"d7","to":"d6","color":"green"},{"from":"d6","to":"e5","color":"blue"}], ["e5"])
                ]
            }
        ]
    })

    # ==========================================
    # 7. THE VIENNA GAME & GAMBIT (WHITE)
    # ==========================================
    courses.append({
        "id": "vienna_game_white",
        "name": "The Vienna Game & Gambit",
        "color": "White",
        "level": "Level 2: Tactical & Aggressive",
        "description": "White's deadly dynamic weapon against 1... e5. Play 2. Nc3 followed by 3. f4! to rip open the f-file and launch unstoppable king attacks.",
        "chapters": [
            {
                "id": "vn_w_gambit_main",
                "title": "Vienna Gambit Mainline vs 2... Nf6",
                "subtitle": "Play 3. f4! and crush Black with 5. Qf3!",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "King's Pawn", "Take center.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... e5", "e7e5", False, "Black Responds e5", "Symmetrical.", "", [{"from":"e7","to":"e5","color":"blue"}]),
                    step("2. Nc3", "b1c3", True, "The Vienna Knight", "Develop the queen's knight first. This defends e4 and controls d5 while keeping the f-pawn unblocked!", "Better than 2. Nf3 if you want to play the aggressive f4 thrust.", [{"from":"b1","to":"c3","color":"green"}], ["d5","e4"]),
                    step("2... Nf6", "g8f6", False, "Black Develops Knight", "Attacking e4.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("3. f4!", "f2f4", True, "The Vienna Gambit!", "Strike in the center on move 3! Offer the f4 pawn to blow open the f-file toward Black's king.", "Never accept passivity; blast open lines.", [{"from":"f2","to":"f4","color":"green"},{"from":"f4","to":"e5","color":"red"}], ["e5","f-file"]),
                    step("3... d5", "d7d5", False, "Black Counterstrikes", "The best theoretical response.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("4. fxe5", "f4e5", True, "Capture & Kick Knight", "Capture toward the center and drive Black's knight to e4.", "Keep the initiative.", [{"from":"f4","to":"e5","color":"green"},{"from":"e5","to":"f6","color":"red"}], ["e5"]),
                    step("4... Nxe4", "f6e4", False, "Knight to e4", "Black's knight invades.", "", [{"from":"f6","to":"e4","color":"blue"}]),
                    step("5. Qf3!", "d1f3", True, "The Steinitz Queen Battery", "Attack Black's centralized knight immediately! If 5... Nxc3, recapture with 6. bxc3 maintaining a ferocious center and open f-file.", "White enjoys a dominant kingside attacking position.", [{"from":"d1","to":"f3","color":"green"},{"from":"f3","to":"e4","color":"red"}], ["e4","f7"])
                ]
            },
            {
                "id": "vn_w_frankenstein",
                "title": "Frankenstein-Dracula Attack vs 2... Nc6",
                "subtitle": "Play 3. Bc4 and dominate the d5 outpost",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "Pawn to e4", "Open center.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... e5", "e7e5", False, "Black Plays e5", "Mirror move.", "", [{"from":"e7","to":"e5","color":"blue"}]),
                    step("2. Nc3", "b1c3", True, "Knight to c3", "Vienna knight.", "", [{"from":"b1","to":"c3","color":"green"}]),
                    step("2... Nc6", "b8c6", False, "Black Plays Nc6", "Symmetrical knight.", "", [{"from":"b8","to":"c6","color":"blue"}]),
                    step("3. Bc4", "f1c4", True, "Active Bishop to c4", "Target the vulnerable f7 square and dominate the center.", "Develops with great attacking pressure.", [{"from":"f1","to":"c4","color":"green"},{"from":"c4","to":"f7","color":"red"}], ["f7"]),
                    step("3... Nf6", "g8f6", False, "Black Develops", "Guards d5.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("4. d3", "d2d3", True, "Solidify Central Base", "Reinforce e4 and open diagonal for the dark-squared bishop.", "White holds a flexible attacking platform.", [{"from":"d2","to":"d3","color":"green"}], ["e4"])
                ]
            }
        ]
    })

    # ==========================================
    # 7. THE VIENNA GAME & GAMBIT (BLACK)
    # ==========================================
    courses.append({
        "id": "vienna_game_black",
        "name": "The Vienna Game & Gambit",
        "color": "Black",
        "level": "Level 2: Tactical & Aggressive",
        "description": "Black's complete refutation of White's 2. Nc3 tricks. Counter White's 3. f4 gambit with the powerful 3... d5! central blow.",
        "chapters": [
            {
                "id": "vn_b_gambit_counter",
                "title": "Vienna Gambit: 3... d5! Central Strike",
                "subtitle": "Refuse the bait and counterattack White's king",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Starts e4", "King's pawn.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... e5", "e7e5", True, "Stake the Center", "Classical equality.", "", [{"from":"e7","to":"e5","color":"green"}]),
                    step("2. Nc3", "b1c3", False, "White Plays Vienna Nc3", "Keeping f-pawn free.", "", [{"from":"b1","to":"c3","color":"blue"}]),
                    step("2... Nf6", "g8f6", True, "Develop with Central Pressure", "Develop the kingside knight and attack e4.", "The most principled challenge.", [{"from":"g8","to":"f6","color":"green"}], ["e4"]),
                    step("3. f4", "f2f4", False, "White Plays Vienna Gambit", "Attacking e5.", "", [{"from":"f2","to":"f4","color":"blue"}]),
                    step("3... d5!", "d7d5", True, "The Central Counter-Blow!", "DO NOT take on f4! Strike in the center immediately with 3... d5!", "Taking on f4 allows White 4. e5 kicking your knight. 3... d5 gives Black full equality.", [{"from":"d7","to":"d5","color":"green"},{"from":"d5","to":"e4","color":"red"}], ["e4"]),
                    step("4. fxe5", "f4e5", False, "White Captures", "Attacking knight.", "", [{"from":"f4","to":"e5","color":"blue"}]),
                    step("4... Nxe4", "f6e4", True, "Centralize the Knight", "Invade White's position on e4! White has no comfortable way to dislodge this knight.", "Strongest piece on the board.", [{"from":"f6","to":"e4","color":"green"}], ["e4"]),
                    step("5. Qf3", "d1f3", False, "White Attacks Knight", "White plays Qf3.", "", [{"from":"d1","to":"f3","color":"blue"}]),
                    step("5... f5!", "f7f5", True, "Anchor the Knight!", "Support the knight with the f-pawn. Black has equal space and easy development.", "Black has completely disarmed White's gambit.", [{"from":"f7","to":"f5","color":"green"}], ["e4"])
                ]
            },
            {
                "id": "vn_b_symmetrical",
                "title": "Solid Equalizer vs 3. Bc4",
                "subtitle": "Safe development with 3... Bc5 and kingside castling",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Opens e4", "Standard opening.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... e5", "e7e5", True, "Stake the Center", "Equalizing move.", "", [{"from":"e7","to":"e5","color":"green"}]),
                    step("2. Nc3", "b1c3", False, "White Plays Nc3", "Vienna knight.", "", [{"from":"b1","to":"c3","color":"blue"}]),
                    step("2... Nc6", "b8c6", True, "Symmetrical Knight", "Defending e5.", "", [{"from":"b8","to":"c6","color":"green"}]),
                    step("3. Bc4", "f1c4", False, "White Plays Bc4", "Targeting f7.", "", [{"from":"f1","to":"c4","color":"blue"}]),
                    step("3... Bc5", "f8c5", True, "Active Bishop", "Control d4 and match White's bishop.", "Active piece play.", [{"from":"f8","to":"c5","color":"green"}], ["d4"]),
                    step("4. d3", "d2d3", False, "White Plays d3", "Quiet setup.", "", [{"from":"d2","to":"d3","color":"blue"}]),
                    step("4... Nf6", "g8f6", True, "Develop Knight to f6", "Prepare castling and control e4.", "Black achieves complete equality.", [{"from":"g8","to":"f6","color":"green"}], ["e4"])
                ]
            }
        ]
    })

    # ==========================================
    # 8. THE KING'S INDIAN DEFENSE (WHITE)
    # ==========================================
    courses.append({
        "id": "kings_indian_white",
        "name": "The King's Indian Defense",
        "color": "White",
        "level": "Level 3: Strategic & Deep",
        "description": "White's masterclass strategies against Black's hypermodern KID. Crush Black's queenside in the Classical Mar del Plata or lock down the center with the Sämisch.",
        "chapters": [
            {
                "id": "kid_w_classical",
                "title": "Classical Mar del Plata: Queenside Steamroller",
                "subtitle": "Break open Black's queenside with c5 while containing f5",
                "orientation": "white",
                "steps": [
                    step("1. d4", "d2d4", True, "Queen's Pawn", "Claim center.", "", [{"from":"d2","to":"d4","color":"green"}]),
                    step("1... Nf6", "g8f6", False, "KID Setup", "Controlling e4.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("2. c4", "c2c4", True, "Pawn to c4", "Take space.", "", [{"from":"c2","to":"c4","color":"green"}]),
                    step("2... g6", "g7g6", False, "Fianchetto Prep", "Black prepares Bg7.", "", [{"from":"g7","to":"g6","color":"blue"}]),
                    step("3. Nc3", "b1c3", True, "Knight to c3", "Prepare e4.", "", [{"from":"b1","to":"c3","color":"green"}]),
                    step("3... Bg7", "f8g7", False, "Bishop to g7", "Long diagonal.", "", [{"from":"f8","to":"g7","color":"blue"}]),
                    step("4. e4", "e2e4", True, "Full Classical Center", "Occupy the entire center with d4, c4, and e4.", "White enjoys a massive space advantage.", [{"from":"e2","to":"e4","color":"green"}], ["d4","e4","c4"]),
                    step("4... d6", "d7d6", False, "Pawn to d6", "Stopping e5.", "", [{"from":"d7","to":"d6","color":"blue"}]),
                    step("5. Nf3", "g1f3", True, "Develop Kingside Knight", "Harmonious development.", "", [{"from":"g1","to":"f3","color":"green"}]),
                    step("5... O-O", "e8g8", False, "Black Castles", "King safe.", "", [{"from":"e8","to":"g8","color":"blue"}]),
                    step("6. Be2", "f1e2", True, "Prepare Castling", "Develop the bishop modestly to e2.", "Avoid putting the bishop on d3 where it can be attacked.", [{"from":"f1","to":"e2","color":"green"}], ["e2"]),
                    step("6... e5", "e7e5", False, "Black Strikes Center", "Classical KID move.", "", [{"from":"e7","to":"e5","color":"blue"}]),
                    step("7. O-O", "e1g1", True, "Castle into Safety", "Secure White's king before closing the center with d5.", "White has completed ideal development.", [{"from":"e1","to":"g1","color":"green"}], ["g1"])
                ]
            },
            {
                "id": "kid_w_saemisch",
                "title": "The Sämisch Variation: Iron Wall (5. f3)",
                "subtitle": "Cement e4 with f3 and launch opposite-side castling",
                "orientation": "white",
                "steps": [
                    step("1. d4", "d2d4", True, "Pawn to d4", "Center.", "", [{"from":"d2","to":"d4","color":"green"}]),
                    step("1... Nf6", "g8f6", False, "Knight to f6", "Control e4.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("2. c4", "c2c4", True, "Pawn to c4", "Space.", "", [{"from":"c2","to":"c4","color":"green"}]),
                    step("2... g6", "g7g6", False, "Pawn to g6", "Prep Bg7.", "", [{"from":"g7","to":"g6","color":"blue"}]),
                    step("3. Nc3", "b1c3", True, "Knight to c3", "Prep e4.", "", [{"from":"b1","to":"c3","color":"green"}]),
                    step("3... Bg7", "f8g7", False, "Bishop to g7", "Diagonal.", "", [{"from":"f8","to":"g7","color":"blue"}]),
                    step("4. e4", "e2e4", True, "Pawn to e4", "Big center.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("4... d6", "d7d6", False, "Pawn to d6", "Solidify.", "", [{"from":"d7","to":"d6","color":"blue"}]),
                    step("5. f3!", "f2f3", True, "The Sämisch Pawn", "Cement e4 with f3! This takes g4 away from Black and supports Be3 and Qd2.", "Forms an unshakeable central fortress.", [{"from":"f2","to":"f3","color":"green"}], ["e4","g4"])
                ]
            }
        ]
    })

    # ==========================================
    # 8. THE KING'S INDIAN DEFENSE (BLACK)
    # ==========================================
    courses.append({
        "id": "kings_indian_black",
        "name": "The King's Indian Defense",
        "color": "Black",
        "level": "Level 3: Strategic & Deep",
        "description": "Black's most romantic and devastating kingside counter-attacking opening. Close the center and march pawns down the kingside with ...f5 and ...g5 to hunt White's king.",
        "chapters": [
            {
                "id": "kid_b_kingside_storm",
                "title": "Classical KID: Kingside Checkmating Storm",
                "subtitle": "Lock the center with ...e5 and blast open White's king with ...f5!",
                "orientation": "black",
                "steps": [
                    step("1. d4", "d2d4", False, "White Starts d4", "Queen's pawn.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("1... Nf6", "g8f6", True, "The Hypermodern Knight", "Control e4 and d5 without committing central pawns early.", "Flexible opening move.", [{"from":"g8","to":"f6","color":"green"}], ["e4","d5"]),
                    step("2. c4", "c2c4", False, "White Takes Space", "Pawn to c4.", "", [{"from":"c2","to":"c4","color":"blue"}]),
                    step("2... g6", "g7g6", True, "Prepare the Dragon Bishop", "Prepare to fianchetto the bishop on the lethal long diagonal.", "Setting up the King's Indian setup.", [{"from":"g7","to":"g6","color":"green"}], ["g7"]),
                    step("3. Nc3", "b1c3", False, "White Develops", "Preparing e4.", "", [{"from":"b1","to":"c3","color":"blue"}]),
                    step("3... Bg7", "f8g7", True, "Occupy the Bastion", "Place the bishop on g7. It guards your king and exerts laser pressure down the diagonal.", "Chief defender and attacker in the KID.", [{"from":"f8","to":"g7","color":"green"}], ["a1"]),
                    step("4. e4", "e2e4", False, "White Takes Center", "Full pawn trio.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("4... d6", "d7d6", True, "Stop White's e5 Push", "Lock e5 so White cannot roll forward.", "Prepares our upcoming central strike with e5.", [{"from":"d7","to":"d6","color":"green"}], ["e5"]),
                    step("5. Nf3", "g1f3", False, "White Develops", "Standard flow.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("5... O-O", "e8g8", True, "Castle into the Bastion", "Safeguard your king behind your impenetrable dark-square shield.", "Essential before launching counterplay.", [{"from":"e8","to":"g8","color":"green"}], ["g8"]),
                    step("6. Be2", "f1e2", False, "White Bishop e2", "Classical setup.", "", [{"from":"f1","to":"e2","color":"blue"}]),
                    step("6... e5!", "e7e5", True, "The Explosive Central Strike!", "Strike in the center! Even if White takes on e5, Black easily recaptures with total equality.", "The signature thematic move of the King's Indian.", [{"from":"e7","to":"e5","color":"green"},{"from":"e5","to":"d4","color":"red"}], ["d4"])
                ]
            },
            {
                "id": "kid_b_saemisch_counter",
                "title": "Sämisch Counter: The 6... c5! Sac",
                "subtitle": "Rip open the center against White's f3 setup",
                "orientation": "black",
                "steps": [
                    step("1. d4", "d2d4", False, "White Plays d4", "Center.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("1... Nf6", "g8f6", True, "Knight to f6", "Control.", "", [{"from":"g8","to":"f6","color":"green"}]),
                    step("2. c4", "c2c4", False, "Pawn to c4", "Space.", "", [{"from":"c2","to":"c4","color":"blue"}]),
                    step("2... g6", "g7g6", True, "Fianchetto Prep", "g6.", "", [{"from":"g7","to":"g6","color":"green"}]),
                    step("3. Nc3", "b1c3", False, "Knight to c3", "Develop.", "", [{"from":"b1","to":"c3","color":"blue"}]),
                    step("3... Bg7", "f8g7", True, "Bishop to g7", "Occupied.", "", [{"from":"f8","to":"g7","color":"green"}]),
                    step("4. e4", "e2e4", False, "White Plays e4", "Center.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("4... d6", "d7d6", True, "Pawn to d6", "Anchor.", "", [{"from":"d7","to":"d6","color":"green"}]),
                    step("5. f3", "f2f3", False, "White Sämisch f3", "Pawn wall.", "", [{"from":"f2","to":"f3","color":"blue"}]),
                    step("5... O-O", "e8g8", True, "Castle First", "King safe.", "", [{"from":"e8","to":"g8","color":"green"}]),
                    step("6. Be3", "c1e3", False, "White Bishop e3", "Support.", "", [{"from":"c1","to":"e3","color":"blue"}]),
                    step("6... c5!", "c7c5", True, "The Sämisch Pawn Sacrifice!", "Strike at d4 with c5! Opening the position gives Black terrifying piece activity against White's uncastled King.", "World-class active counterplay.", [{"from":"c7","to":"c5","color":"green"},{"from":"c5","to":"d4","color":"red"}], ["d4"])
                ]
            }
        ]
    })

    # ==========================================
    # 9. THE SCANDINAVIAN DEFENSE (WHITE)
    # ==========================================
    courses.append({
        "id": "scandinavian_white",
        "name": "The Scandinavian Defense",
        "color": "White",
        "level": "Level 1: Fundamentals",
        "description": "Punish Black's premature 1... d5 Queen sorties. Gain tempos with 3. Nc3 and tear open the board with superior piece development.",
        "chapters": [
            {
                "id": "sca_w_mainline",
                "title": "Mainline 3. Nc3 Qa5 Punishment",
                "subtitle": "Chase Black's queen and seize total central control",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "King's Pawn", "Take center.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... d5", "d7d5", False, "Scandinavian Strike", "Black strikes 1... d5.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("2. exd5", "e4d5", True, "Capture the Pawn", "Accept the challenge! Black must waste moves recapturing with their Queen.", "The cleanest, strongest continuation.", [{"from":"e4","to":"d5","color":"green"}], ["d5"]),
                    step("2... Qxd5", "d8d5", False, "Queen Enters Early", "Black exposes their Queen.", "", [{"from":"d8","to":"d5","color":"blue"}]),
                    step("3. Nc3", "b1c3", True, "Develop with Tempo on Queen!", "Develop the knight while attacking Black's Queen! Win a free developing tempo.", "The fundamental principle: attack exposed major pieces with minor pieces.", [{"from":"b1","to":"c3","color":"green"},{"from":"c3","to":"d5","color":"red"}], ["d5"]),
                    step("3... Qa5", "d5a5", False, "Queen Retreats to a5", "Black's mainline retreat.", "", [{"from":"d5","to":"a5","color":"blue"}]),
                    step("4. d4", "d2d4", True, "Establish Dominant Center", "Push d4 to control e5 and c5. White has two pieces developed and full central domination.", "White is fundamentally better.", [{"from":"d2","to":"d4","color":"green"}], ["d4","e5"])
                ]
            },
            {
                "id": "sca_w_modern_nf6",
                "title": "Modern 2... Nf6 Punishment (3. d4)",
                "subtitle": "Maintain the central duo (c4 + d4) against knight recaptures",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "Pawn to e4", "Standard opening.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... d5", "d7d5", False, "Pawn to d5", "Direct challenge.", "", [{"from":"d7","to":"d5","color":"blue"}]),
                    step("2. exd5", "e4d5", True, "Take the Pawn", "Capture d5.", "", [{"from":"e4","to":"d5","color":"green"}]),
                    step("2... Nf6", "g8f6", False, "Modern Scandinavian", "Black avoids early queen sortie.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("3. d4", "d2d4", True, "Claim the Center with d4", "Occupy the center and open lines for your pieces.", "Don't cling greedily to the d5 pawn with 3. c4; play for rapid development.", [{"from":"d2","to":"d4","color":"green"}], ["d4"]),
                    step("3... Nxd5", "f6d5", False, "Black Recaptures", "Knight on d5.", "", [{"from":"f6","to":"d5","color":"blue"}]),
                    step("4. c4!", "c2c4", True, "Kick the Knight & Conquer Center", "Advance c4 with tempo! Drive the knight away and establish a magnificent broad pawn center.", "White has a decisive spatial edge.", [{"from":"c2","to":"c4","color":"green"},{"from":"c4","to":"d5","color":"red"}], ["d5","c4","d4"])
                ]
            }
        ]
    })

    # ==========================================
    # 9. THE SCANDINAVIAN DEFENSE (BLACK)
    # ==========================================
    courses.append({
        "id": "scandinavian_black",
        "name": "The Scandinavian Defense",
        "color": "Black",
        "level": "Level 1: Fundamentals",
        "description": "Black's direct, uncompromising challenge to 1. e4. Immediately dismantle White's center and transition into a solid Caro-Kann style fortress.",
        "chapters": [
            {
                "id": "sca_b_solid_qa5",
                "title": "Solid Mainline 3... Qa5 System",
                "subtitle": "Safe queen retreat and rapid light-squared bishop development",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Starts e4", "King's pawn.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... d5", "d7d5", True, "The Scandinavian Challenge!", "Immediately challenge White's e4 pawn on move 1!", "Forces an immediate resolution of central tension.", [{"from":"d7","to":"d5","color":"green"}], ["e4"]),
                    step("2. exd5", "e4d5", False, "White Takes d5", "Pawn captured.", "", [{"from":"e4","to":"d5","color":"blue"}]),
                    step("2... Qxd5", "d8d5", True, "Centralize the Queen", "Recapture with the queen in the center of the board.", "Be ready for White's 3. Nc3 attack.", [{"from":"d8","to":"d5","color":"green"}], ["d5"]),
                    step("3. Nc3", "b1c3", False, "White Attacks Queen", "Developing with tempo.", "", [{"from":"b1","to":"c3","color":"blue"}]),
                    step("3... Qa5", "d5a5", True, "The Mainline a5 Haven", "Tuck the queen on a5. Here she is safe from further minor piece attacks and pins White's c3 knight.", "The most reliable retreat square.", [{"from":"d5","to":"a5","color":"green"}], ["a5"]),
                    step("4. d4", "d2d4", False, "White Takes Center", "Pawn to d4.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("4... Nf6", "g8f6", True, "Develop Kingside Knight", "Control e4 and prepare rapid kingside development.", "Coordinate pieces harmoniously.", [{"from":"g8","to":"f6","color":"green"}], ["e4"]),
                    step("5. Nf3", "g1f3", False, "White Develops", "Developing move.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("5... Bf5", "c8f5", True, "Bishop Outside the Chain!", "Develop the light-squared bishop outside the pawn chain before playing e6! Complete harmony.", "Black achieves solid equality.", [{"from":"c8","to":"f5","color":"green"}], ["f5"])
                ]
            },
            {
                "id": "sca_b_modern_nf6",
                "title": "Modern 2... Nf6 Scandinavian",
                "subtitle": "Active piece play avoiding early queen excursions",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Plays e4", "Opening.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... d5", "d7d5", True, "Pawn to d5", "Challenge.", "", [{"from":"d7","to":"d5","color":"green"}]),
                    step("2. exd5", "e4d5", False, "White Takes d5", "Pawn gone.", "", [{"from":"e4","to":"d5","color":"blue"}]),
                    step("2... Nf6", "g8f6", True, "Develop Knight to Attack d5", "Aim to recapture d5 with the knight to avoid exposing the queen.", "Dynamic and active.", [{"from":"g8","to":"f6","color":"green"}], ["d5"]),
                    step("3. d4", "d2d4", False, "White Plays d4", "Center.", "", [{"from":"d2","to":"d4","color":"blue"}]),
                    step("3... Nxd5", "f6d5", True, "Centralize Knight", "Recapture d5 with piece power.", "Excellent central outpost.", [{"from":"f6","to":"d5","color":"green"}], ["d5"]),
                    step("4. c4", "c2c4", False, "White Kicks Knight", "Pawn push.", "", [{"from":"c2","to":"c4","color":"blue"}]),
                    step("4... Nb6", "d5b6", True, "Retreat to b6 Outpost", "Tuck the knight safely on b6 and prepare to pressure White's d4 pawn.", "Black has active diagonals.", [{"from":"d5","to":"b6","color":"green"}], ["b6"])
                ]
            }
        ]
    })

    # ==========================================
    # 10. THE RUY LOPEZ (WHITE)
    # ==========================================
    courses.append({
        "id": "ruy_lopez_white",
        "name": "The Ruy Lopez (Spanish Opening)",
        "color": "White",
        "level": "Level 3: Strategic & Deep",
        "description": "The quintessential royal opening. Play 3. Bb5 to pressure Black's e5 defender and build enduring positional superiority.",
        "chapters": [
            {
                "id": "ruy_w_closed",
                "title": "Closed Ruy Lopez: Mainline Mastery",
                "subtitle": "Pin the c6 defender and build the classical central bind",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "King's Pawn", "Take center.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... e5", "e7e5", False, "Black Responds e5", "Symmetrical.", "", [{"from":"e7","to":"e5","color":"blue"}]),
                    step("2. Nf3", "g1f3", True, "Knight to f3", "Attack e5.", "", [{"from":"g1","to":"f3","color":"green"}]),
                    step("2... Nc6", "b8c6", False, "Knight to c6", "Guards e5.", "", [{"from":"b8","to":"c6","color":"blue"}]),
                    step("3. Bb5", "f1b5", True, "The Spanish Bishop!", "Pin and pressure Black's c6 knight which defends e5. The hallmark of the Ruy Lopez.", "Positional pressure that lasts into the endgame.", [{"from":"f1","to":"b5","color":"green"},{"from":"b5","to":"c6","color":"red"}], ["c6","e5"]),
                    step("3... a6", "a7a6", False, "Morphy Defense", "Black questions the bishop.", "", [{"from":"a7","to":"a6","color":"blue"}]),
                    step("4. Ba4", "b5a4", True, "Maintain the Pin", "Retreat to a4 while keeping the pin along the diagonal.", "Don't take on c6 unless playing the Exchange Variation.", [{"from":"b5","to":"a4","color":"green"}], ["a4"]),
                    step("4... Nf6", "g8f6", False, "Black Attacks e4", "Developing knight.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("5. O-O", "e1g1", True, "Castle into Safety", "Secure White's king. If Black plays 5... Nxe4, White easily regains the pawn with Re1.", "White stands strategically superior.", [{"from":"e1","to":"g1","color":"green"}], ["g1"])
                ]
            },
            {
                "id": "ruy_w_anti_marshall",
                "title": "Anti-Marshall System (8. a4)",
                "subtitle": "Neutralize the Marshall Gambit before it starts",
                "orientation": "white",
                "steps": [
                    step("1. e4", "e2e4", True, "Pawn to e4", "Center.", "", [{"from":"e2","to":"e4","color":"green"}]),
                    step("1... e5", "e7e5", False, "Pawn to e5", "Symmetrical.", "", [{"from":"e7","to":"e5","color":"blue"}]),
                    step("2. Nf3", "g1f3", True, "Knight to f3", "Attack e5.", "", [{"from":"g1","to":"f3","color":"green"}]),
                    step("2... Nc6", "b8c6", False, "Knight to c6", "Defend.", "", [{"from":"b8","to":"c6","color":"blue"}]),
                    step("3. Bb5", "f1b5", True, "Bishop to b5", "Spanish pin.", "", [{"from":"f1","to":"b5","color":"green"}]),
                    step("3... a6", "a7a6", False, "Pawn to a6", "Morphy.", "", [{"from":"a7","to":"a6","color":"blue"}]),
                    step("4. Ba4", "b5a4", True, "Bishop to a4", "Keep pin.", "", [{"from":"b5","to":"a4","color":"green"}]),
                    step("4... Nf6", "g8f6", False, "Knight to f6", "Develop.", "", [{"from":"g8","to":"f6","color":"blue"}]),
                    step("5. O-O", "e1g1", True, "Castle Kingside", "King safe.", "", [{"from":"e1","to":"g1","color":"green"}]),
                    step("5... Be7", "f8e7", False, "Bishop to e7", "Develop.", "", [{"from":"f8","to":"e7","color":"blue"}]),
                    step("6. Re1", "f1e1", True, "Rook Defends e4", "Guard e4.", "", [{"from":"f1","to":"e1","color":"green"}]),
                    step("6... b5", "b7b5", False, "Pawn to b5", "Kick bishop.", "", [{"from":"b7","to":"b5","color":"blue"}]),
                    step("7. Bb3", "a4b3", True, "Bishop to b3", "Active diagonal.", "", [{"from":"a4","to":"b3","color":"green"}]),
                    step("7... O-O", "e8g8", False, "Black Castles", "Safe.", "", [{"from":"e8","to":"g8","color":"blue"}]),
                    step("8. a4!", "a2a4", True, "The Anti-Marshall Strike!", "Strike at Black's queenside before they can play 8... d5 Marshall Attack!", "Disrupts Black's harmony and wins the initiative.", [{"from":"a2","to":"a4","color":"green"},{"from":"a4","to":"b5","color":"red"}], ["b5"])
                ]
            }
        ]
    })

    # ==========================================
    # 10. THE RUY LOPEZ (BLACK)
    # ==========================================
    courses.append({
        "id": "ruy_lopez_black",
        "name": "The Ruy Lopez (Spanish Opening)",
        "color": "Black",
        "level": "Level 3: Strategic & Deep",
        "description": "Black's legendary counter-weapons against 3. Bb5. Unleash the ferocious Marshall Attack or construct Kramnik's impenetrable Berlin Wall.",
        "chapters": [
            {
                "id": "ruy_b_marshall",
                "title": "The Legendary Marshall Attack",
                "subtitle": "Sacrifice a pawn with 8... d5! for a terrifying kingside attack",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Opens e4", "Standard opening.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... e5", "e7e5", True, "Stake the Center", "Classical equality.", "", [{"from":"e7","to":"e5","color":"green"}]),
                    step("2. Nf3", "g1f3", False, "White Attacks e5", "Knight to f3.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("2... Nc6", "b8c6", True, "Defend e5", "Natural development.", "", [{"from":"b8","to":"c6","color":"green"}]),
                    step("3. Bb5", "f1b5", False, "The Ruy Lopez Bishop", "Pressuring c6.", "", [{"from":"f1","to":"b5","color":"blue"}]),
                    step("3... a6", "a7a6", True, "Ask the Bishop the Question", "Force White to make a decision with their bishop.", "The Morphy Defense.", [{"from":"a7","to":"a6","color":"green"}], ["b5"]),
                    step("4. Ba4", "b5a4", False, "White Retreats", "Maintaining pin.", "", [{"from":"b5","to":"a4","color":"blue"}]),
                    step("4... Nf6", "g8f6", True, "Develop Knight with Attack", "Counterattack White's e4 pawn.", "Force White to commit.", [{"from":"g8","to":"f6","color":"green"}], ["e4"]),
                    step("5. O-O", "e1g1", False, "White Castles", "White castles.", "", [{"from":"e1","to":"g1","color":"blue"}]),
                    step("5... Be7", "f8e7", True, "Develop Bishop to Safety", "Prepare castling and break any future pins along the e-file.", "Solid and principled.", [{"from":"f8","to":"e7","color":"green"}], ["e7"]),
                    step("6. Re1", "f1e1", False, "White Guards e4", "Rook to e1.", "", [{"from":"f1","to":"e1","color":"blue"}]),
                    step("6... b5", "b7b5", True, "Kick the Spanish Bishop", "Drive White's bishop away from the sensitive diagonal.", "Gains queenside space.", [{"from":"b7","to":"b5","color":"green"}], ["a4"]),
                    step("7. Bb3", "a4b3", False, "Bishop Retreats", "Bishop on b3.", "", [{"from":"a4","to":"b3","color":"blue"}]),
                    step("7... O-O", "e8g8", True, "Castle into Safety", "Complete king safety before launching the Marshall bombshell.", "Everything is in place.", [{"from":"e8","to":"g8","color":"green"}], ["g8"]),
                    step("8. c3", "c2c3", False, "White Prepares d4", "Preparing center.", "", [{"from":"c2","to":"c3","color":"blue"}]),
                    step("8... d5!", "d7d5", True, "THE MARSHALL ATTACK BOMB!", "Sacrifice the d5 pawn to rip open the center and launch one of the most ferocious mating attacks in chess history!", "Frank Marshall kept this secret for years. Black dominates the initiative.", [{"from":"d7","to":"d5","color":"green"},{"from":"d5","to":"e4","color":"red"}], ["d5","e4"])
                ]
            },
            {
                "id": "ruy_b_berlin",
                "title": "The Berlin Defense: Kramnik's Fortress",
                "subtitle": "Play 3... Nf6 and construct the uncrackable wall",
                "orientation": "black",
                "steps": [
                    step("1. e4", "e2e4", False, "White Plays e4", "King's pawn.", "", [{"from":"e2","to":"e4","color":"blue"}]),
                    step("1... e5", "e7e5", True, "Black Equalizes", "Central claim.", "", [{"from":"e7","to":"e5","color":"green"}]),
                    step("2. Nf3", "g1f3", False, "Knight to f3", "Attack e5.", "", [{"from":"g1","to":"f3","color":"blue"}]),
                    step("2... Nc6", "b8c6", True, "Knight to c6", "Defend.", "", [{"from":"b8","to":"c6","color":"green"}]),
                    step("3. Bb5", "f1b5", False, "Bishop to b5", "Spanish pin.", "", [{"from":"f1","to":"b5","color":"blue"}]),
                    step("3... Nf6", "g8f6", True, "The Berlin Wall!", "Counterattack e4 directly without playing a6. This is the legendary Berlin Defense that defeated Kasparov.", "The most impenetrable fortress in world championship history.", [{"from":"g8","to":"f6","color":"green"},{"from":"f6","to":"e4","color":"red"}], ["e4"]),
                    step("4. O-O", "e1g1", False, "White Castles", "Normal move.", "", [{"from":"e1","to":"g1","color":"blue"}]),
                    step("4... Nxe4", "f6e4", True, "Capture e4 Pawn", "Grab White's central pawn! Black forces an endgame where White has zero winning chances.", "Black establishes an unshakeable wall.", [{"from":"f6","to":"e4","color":"green"}], ["e4"])
                ]
            }
        ]
    })

    return courses
