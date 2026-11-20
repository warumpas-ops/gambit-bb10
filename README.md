# ♟️ Gambit — Interactive Chess Opening Trainer for BlackBerry 10

> Master chess openings through guided tactical arrows, human move-by-move explanations of the "why", and active-recall spaced repetition drills. Specially engineered for the 720×720 square displays and physical QWERTY keyboards of BlackBerry 10 devices (Q5, Q10, Classic Q20, Passport).

---

## ⚡ Why Gambit?

Traditional chess training apps either require heavy modern Android runtimes or force you to memorize dry notation lines like a robot. **Gambit** brings the modern interactive pedagogy of platforms like Chessly and Chessable directly to legacy BlackBerry 10 devices:

1. **Visual Directional Arrows**:
   - 🟢 **Green Arrow**: The optimal repertoire move to play.
   - 🔵 **Blue Arrow**: Long-term strategic plan or pawn break.
   - 🔴 **Red Arrow**: Immediate opponent threat or tactical trap to avoid.
2. **Deep Explanations of the "Why"**:
   - Every single move is annotated with the conceptual reasoning behind it (center control, piece activity, pawn structure, king safety).
   - ⚠️ **Tactical Trap Warnings** warn you against tempting natural blunders.
3. **Dual Study & Drill Modes**:
   - **`[STUDY]` Mode**: Full interactive walkthrough with guided arrows and move annotations.
   - **`[DRILL]` Mode**: Blind active recall! Test your memory without arrows. Immediate green/red validation on the board with an optional `[💡 HINT]` button.
4. **Pre-Loaded Opening Repertoires**:
   - ⚪ **The Italian Game**:
     - *Two Knights & The Fried Liver Attack* (aggressive f7 tactics)
     - *Giuoco Piano* (solid center dominance with c3/d4)
   - ⚫ **The Caro-Kann Defense**:
     - *Classical Variation* (dynamic counterpunching with 4... Bf5!)
5. **Full BlackBerry QWERTY Keyboard Controls**:
   - `[Space]` — Advance to next move / chapter
   - `[R]` — Reset current opening line
   - `[H]` — Reveal tactical hint & arrows
   - `[M]` — Instant toggle between Study & Drill modes
   - `[C]` — Open Chapter & Opening selector dialog
6. **Zero Bloat**: Pure native Java API 18, zero external dependencies, under **30 KB**!

---

## 📲 Sideloading onto BlackBerry 10

1. Download **`Gambit-v1.0.0.apk`** from [Releases](https://github.com/warumpas-ops/gambit-bb10/releases).
2. Transfer the `.apk` file to your BlackBerry device via USB cable or MicroSD card.
3. Open the built-in **File Manager** app on your BB10 phone.
4. Tap `Gambit-v1.0.0.apk` and tap **Install** in the top-right corner.

---

## 🏗️ Building from Source

```bash
git clone https://github.com/warumpas-ops/gambit-bb10.git
cd gambit-bb10
./gradlew assembleDebug
```
The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.
