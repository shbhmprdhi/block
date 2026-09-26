package com.block.fitness

internal data class Exercise(
    val id: String,
    val name: String,
    val prescription: String,
    val rir: String,
    val rest: String,
    val cue: String,
    val alternatives: String = "",
)

internal data class TrainingDay(
    val id: String,
    val day: String,
    val focus: String,
    val duration: String,
    val warmup: List<String>,
    val exercises: List<Exercise>,
    val cooldown: List<String>,
    val isRun: Boolean = false,
)

internal data class Meal(
    val id: String,
    val time: String,
    val name: String,
    val detail: String,
    val supplement: String = "",
)

internal object PlanData {
    val days = listOf(
        TrainingDay("mon", "Monday", "Push A · Chest, shoulders, triceps", "60–75 min", listOf("3 min bike or rower", "Arm circles ×15 each way", "Band pull-aparts ×15", "Shoulder dislocations ×10", "Push-up to downward dog ×10", "Scapular push-ups ×10", "Band external rotation ×12/side", "Bench warm-up sets: empty bar ×10, 40% ×8, 60% ×5, 80% ×3"), listOf(
            Exercise("bench", "Barbell Bench Press", "3 × 6–8", "2–3", "2–3 min", "Retract shoulder blades, feet planted, bar to lower chest", "Dumbbell Bench Press · Machine Chest Press"),
            Exercise("incline_db", "Incline Dumbbell Press", "3 × 8–12", "2", "90 sec", "30° bench, elbows at 45°", "Incline Barbell Press · Incline Machine Press"),
            Exercise("shoulder_db", "Seated Dumbbell Shoulder Press", "3 × 8–12", "2", "90 sec", "Brace your core; avoid arching"),
            Exercise("lateral_raise", "Cable Lateral Raise", "4 × 12–15", "1–2", "60 sec", "Lead with the elbow; slight lean", "Dumbbell Lateral Raise · Machine Lateral Raise"),
            Exercise("triceps_pushdown", "Dips or Cable Tricep Pushdown", "3 × 10–15", "1–2", "60 sec", "Keep elbows tucked; finish with a full lockout"),
            Exercise("overhead_triceps", "Overhead Cable Tricep Extension", "3 × 10–15", "1–2", "60 sec", "Keep elbows by your ears"),
            Exercise("wrist_curl", "Wrist Curl", "2 × 15", "1", "45 sec", "Use a full, controlled range"),
            Exercise("reverse_wrist", "Reverse Wrist Curl", "2 × 15", "1", "45 sec", "Keep the load light")
        ), listOf("Doorway chest stretch · 30 sec/side", "Cross-body shoulder stretch · 30 sec/side", "Overhead triceps stretch · 30 sec/side")),
        TrainingDay("tue", "Tuesday", "Pull A · Back, biceps, traps, core", "60–75 min", listOf("3 min rower or bike", "Cat-Cow ×10", "Thoracic rotation ×8/side", "Band pull-aparts ×15", "Bodyweight RDL ×10", "Glute bridge ×12", "Scapular pull-ups ×8", "Dead hang · 20 sec", "Deadlift warm-up: empty bar ×10, 40% ×5, 60% ×3, 80% ×2"), listOf(
            Exercise("deadlift", "Deadlift (Conventional)", "3 × 5", "3 → 2", "3 min", "Bar over midfoot, brace, push the floor away", "Trap Bar Deadlift · Rack Pulls"),
            Exercise("pullup_a", "Pull-Ups or Lat Pulldown", "3 × 8–10", "2", "90 sec", "Chest up; drive elbows down", "Lat Pulldown · Assisted Pull-Up"),
            Exercise("barbell_row", "Barbell Row", "3 × 8–10", "2", "90 sec", "Hinge 45°; pull toward lower ribs", "Dumbbell Row · Seated Cable Row"),
            Exercise("face_pull_a", "Face Pulls", "3 × 15–20", "1", "60 sec", "Rope to face, elbows high", "Reverse Fly · Band Pull-Aparts"),
            Exercise("shrug", "Barbell Shrugs", "3 × 12–15", "1–2", "60 sec", "Move straight up and pause"),
            Exercise("curl_a", "Barbell or Dumbbell Bicep Curl", "3 × 10–12", "1–2", "60 sec", "No swing; full squeeze"),
            Exercise("leg_raise_a", "Hanging Leg Raise", "3 × 10–15", "1", "60 sec", "Move under control; no swinging"),
            Exercise("carry", "Farmer’s Carry", "3 × 30–40 m", "—", "90 sec", "Carry heavy and walk tall")
        ), listOf("Child’s pose · 45 sec", "Seated hamstring stretch · 30 sec/side", "Bicep wall stretch · 30 sec/side")),
        TrainingDay("wed", "Wednesday", "Leg A · Quads, hamstrings, glutes, calves, core", "60–75 min", listOf("3 min bike", "Bodyweight squat ×15", "Walking knee-to-chest ×10/side", "Ankle rocks ×10/side", "Bodyweight lunge ×10/leg", "Glute bridge ×12", "Leg swings ×10/side", "Squat warm-up: empty bar ×10, 40% ×8, 60% ×5, 80% ×3"), listOf(
            Exercise("back_squat", "Barbell Back Squat", "3 × 6–8", "2–3", "2–3 min", "Brace, knees out, reach comfortable depth", "Goblet Squat · Leg Press · Hack Squat"),
            Exercise("rdl_a", "Romanian Deadlift", "3 × 8–10", "2–3", "90 sec", "Hinge; keep bar close and feel hamstring stretch", "Good Morning · Seated Leg Curl"),
            Exercise("leg_press_a", "Leg Press", "3 × 10–12", "2", "90 sec", "Use full range; do not lock knees", "Hack Squat · Belt Squat"),
            Exercise("walking_lunge", "Walking Lunges", "3 × 10/leg", "2", "90 sec", "Long stride, upright torso", "Bulgarian Split Squat · Step-Ups"),
            Exercise("leg_curl_a", "Leg Curl", "3 × 12–15", "1–2", "60 sec", "Full range; squeeze at the end", "Nordic Curl · Glute-Ham Raise"),
            Exercise("calf_a", "Standing Calf Raise", "4 × 12–15", "1", "45 sec", "Full stretch; pause at the top", "Seated Calf Raise · Leg Press Calf Raise"),
            Exercise("crunch_a", "Cable Crunch", "3 × 15", "1", "60 sec", "Crunch down; do not pull with arms", "Hanging Leg Raise · Ab Wheel"),
            Exercise("plank", "Plank", "3 × 45–60 sec", "—", "60 sec", "Neutral spine; squeeze glutes")
        ), listOf("Standing quad stretch · 30 sec/side", "Seated hamstring stretch · 30 sec/side", "Figure-four stretch · 30 sec/side", "Standing calf stretch · 30 sec/side")),
        TrainingDay("thu", "Thursday", "Push B · Shoulders, upper chest, triceps", "60–75 min", listOf("3 min bike or rower", "Arm circles ×15", "Band pull-aparts ×15", "Shoulder dislocations ×10", "Wall slides ×10", "Band external rotation ×12/side", "Incline bench warm-up: empty bar ×10, 40% ×8, 60% ×5"), listOf(
            Exercise("incline_bar", "Incline Barbell Bench Press", "4 × 8–10", "2–3", "2 min", "30° bench; bar to upper chest", "Incline Dumbbell Press · Incline Machine Press"),
            Exercise("shoulder_db_b", "Seated Dumbbell Shoulder Press", "3 × 8–12", "2", "90 sec", "Core tight; full lockout"),
            Exercise("lateral_raise_b", "Cable Lateral Raise", "4 × 12–15", "1", "60 sec", "Lead with elbow"),
            Exercise("pec_deck", "Pec Deck or Cable Fly", "3 × 12–15", "1–2", "60 sec", "Squeeze and control the return"),
            Exercise("overhead_tri_b", "Overhead Cable Tricep Extension", "3 × 10–15", "1–2", "60 sec", "Elbows by your ears"),
            Exercise("dips_b", "Dips or Cable Pushdown", "3 × 10–15", "1–2", "60 sec", "Finish with a full lockout"),
            Exercise("wrist_b", "Wrist Curl", "2 × 15", "1", "45 sec", "Full range"),
            Exercise("reverse_wrist_b", "Reverse Wrist Curl", "2 × 15", "1", "45 sec", "Keep the load light")
        ), listOf("Doorway chest stretch · 30 sec/side", "Cross-body shoulder stretch · 30 sec/side", "Overhead triceps stretch · 30 sec/side")),
        TrainingDay("fri", "Friday", "Pull B · Back, biceps, rear delts, core", "60–75 min", listOf("3 min rower", "Cat-Cow ×10", "Thoracic rotation ×8/side", "Band pull-aparts ×15", "Bodyweight RDL ×10", "Scapular pull-ups ×8", "RDL warm-up: empty bar ×10, 40% ×8, 60% ×5"), listOf(
            Exercise("rdl_b", "Romanian Deadlift", "3 × 8–10", "2–3", "2 min", "Hinge; keep bar close"),
            Exercise("pullup_b", "Pull-Ups or Lat Pulldown", "4 × 8–12", "2", "90 sec", "Chest up; elbows down", "Lat Pulldown · Assisted Pull-Up"),
            Exercise("cable_row", "Seated Cable Row", "3 × 10–12", "2", "90 sec", "Squeeze shoulder blades"),
            Exercise("face_pull_b", "Face Pulls", "3 × 15–20", "1", "60 sec", "Rope to face"),
            Exercise("reverse_fly", "Reverse Fly", "3 × 12–15", "1", "60 sec", "Slight bend; squeeze", "Reverse Fly · Band Pull-Aparts"),
            Exercise("barbell_curl_b", "Barbell Curl", "3 × 8–12", "1–2", "60 sec", "No swing"),
            Exercise("hammer_curl", "Hammer Curl", "3 × 10–12", "1–2", "60 sec", "Keep a neutral grip"),
            Exercise("leg_raise_b", "Hanging Leg Raise", "3 × 10–15", "1", "60 sec", "Controlled; no swing")
        ), listOf("Child’s pose · 45 sec", "Seated hamstring stretch · 30 sec/side", "Bicep wall stretch · 30 sec/side")),
        TrainingDay("sat", "Saturday", "Leg B · Posterior chain, quads, glutes, calves", "60–75 min", listOf("3 min bike", "Bodyweight squat ×15", "Walking knee-to-chest ×10/side", "Ankle rocks ×10/side", "Bodyweight lunge ×10/leg", "Glute bridge ×12", "Leg press warm-up: light ×15"), listOf(
            Exercise("leg_press_b", "Leg Press", "4 × 10–12", "2", "90 sec", "Full range; do not lock knees", "Hack Squat · Belt Squat"),
            Exercise("bulgarian", "Bulgarian Split Squat", "3 × 8–10/leg", "2", "90 sec", "Upright torso; knee can travel over toe"),
            Exercise("leg_extension", "Leg Extension", "3 × 12–15", "1", "60 sec", "Squeeze at the top"),
            Exercise("leg_curl_b", "Leg Curl", "4 × 10–15", "1", "60 sec", "Full range"),
            Exercise("hip_thrust", "Hip Thrust", "3 × 10–12", "2", "90 sec", "Chin tucked; squeeze glutes"),
            Exercise("calf_b", "Standing Calf Raise", "4 × 12–15", "1", "45 sec", "Full stretch; pause at top", "Seated Calf Raise · Leg Press Calf Raise"),
            Exercise("adductor", "Adductor Machine", "3 × 12–15", "1", "60 sec", "Move under control"),
            Exercise("crunch_b", "Cable Crunch", "3 × 15", "1", "60 sec", "Crunch down", "Hanging Leg Raise · Ab Wheel")
        ), listOf("Standing quad stretch · 30 sec/side", "Seated hamstring stretch · 30 sec/side", "Figure-four stretch · 30 sec/side", "Standing calf stretch · 30 sec/side")),
        TrainingDay("sun", "Sunday", "3 km easy run + recovery", "30–45 min", listOf("Leg swings ×10/side", "High knees · 20 m", "Butt kicks · 20 m"), listOf(
            Exercise("easy_run", "Easy conversational run", "3 km · RPE 4–5/10", "—", "Self-paced", "Stay at a pace where you can speak in full sentences. If legs are very tired: walk 5 min, jog 1.5 km, walk 5 min. No sprints."),
            Exercise("recovery_walk", "Recovery walk", "5–10 min", "—", "—", "Walk easily after the run."),
            Exercise("foam_roll", "Foam roll", "5 min", "—", "—", "Quads, hamstrings, calves, glutes, upper back."),
            Exercise("recovery_stretch", "Gentle stretching", "30 sec/side", "—", "—", "Quad, hamstring, calf, and hip flexor.")
        ), listOf("Hydrate · 500 ml water + electrolytes"), isRun = true)
    )

    val mobility = listOf(
        "Cat-Cow" to "10 reps · Dynamic · Spine",
        "Thoracic Rotation (quadruped)" to "8/side · Dynamic · T-spine",
        "Hip Circles" to "8/side · Dynamic · Hips",
        "Leg Swings (front/back)" to "10/side · Dynamic · Hip flexors, hamstrings",
        "Leg Swings (side/side)" to "10/side · Dynamic · Adductors, abductors",
        "Arm Circles" to "15 each way · Dynamic · Shoulders",
        "Band Pull-Aparts" to "15 reps · Dynamic · Rear delts, upper back",
        "Wall Slides" to "10 reps · Dynamic · Scapular control",
        "Ankle Rocks" to "10/side · Dynamic · Ankles",
        "Deep Squat Hold" to "30 sec · Static · Hips, ankles",
        "World’s Greatest Stretch" to "5/side · Dynamic · Hips, T-spine",
        "Standing Quad Stretch" to "20 sec/side · Static · Quads",
        "Doorway Chest Stretch" to "20 sec/side · Static · Chest"
    )

    val meals = listOf(
        Meal("wake", "8:00 AM", "Wake", "Lemon water + honey"),
        Meal("breakfast", "9:00 AM", "Breakfast", "Milk 250 ml · whey 1 scoop · creatine 5 g · banana ×2 · 10–12 almonds · 3–4 walnuts", "CREATINE · 5 G"),
        Meal("lunch", "1:00 PM", "Lunch", "4 roti · 1 cup rice · dal 1.5 cups · paneer 100 g · sabzi + salad + ghee"),
        Meal("snack", "4:00 PM", "Snack", "Curd 200 g · roasted chana 30 g · pumpkin seeds 15 g"),
        Meal("preworkout", "6:15 PM", "Pre-workout", "1 plain banana · 30–45 min before gym"),
        Meal("training", "7:00 PM", "Gym session", "500–750 ml water + pinch of salt & lemon, or electrolyte mix"),
        Meal("postworkout", "8:15 PM", "Post-workout", "Milk 250 ml · whey 1 scoop"),
        Meal("dinner", "9:00 PM", "Dinner", "4 roti · dal 1 cup · tofu/paneer 75 g · sabzi + 1 tbsp peanut butter", "B12 · D3 · magnesium · omega-3")
    )

    fun weekNumber(startEpochDay: Long, todayEpochDay: Long): Int =
        (((todayEpochDay - startEpochDay).coerceAtLeast(0)) / 7L + 1L).coerceAtMost(999L).toInt()

    fun workingSets(exercise: Exercise, week: Int): Int {
        val prescribed = Regex("^(\\d+)").find(exercise.prescription)?.groupValues?.get(1)?.toIntOrNull() ?: 1
        return when {
            week <= 2 -> 2
            week <= 4 -> 3
            else -> prescribed
        }
    }

    fun workingRir(exercise: Exercise, week: Int): String = when {
        exercise.rir == "—" -> "—"
        week <= 2 -> "3–4"
        week <= 4 -> "2–3"
        else -> exercise.rir
    }
}
