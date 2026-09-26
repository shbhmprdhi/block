package com.block.fitness

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.SystemClock
import android.speech.tts.TextToSpeech
import android.view.HapticFeedbackConstants
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Locale

private val Ink = Color(0xFF101114)
private val Chalk = Color(0xFFF7F6F2)
private val Paper = Color(0xFFFFFFFF)
private val Muted = Color(0xFF73757B)
private val Line = Color(0xFFE5E3DF)
private val Blue = Color(0xFF3859D6)
private val BluePale = Color(0xFFE8ECFF)
private val Lime = Color(0xFFD9F77A)
private val FuelOrange = Color(0xFFE7774D)
private val FuelYellow = Color(0xFFF2C94C)
private val FuelPink = Color(0xFFEFA7B4)
private val FuelGreen = Color(0xFFB6DDBF)
private val FuelPurple = Color(0xFFC9B7EF)
private val FuelBlue = Color(0xFFAED7E8)
private val FuelPalette = listOf(FuelYellow, FuelPink, FuelGreen, FuelPurple, FuelBlue, Color(0xFFF2C9A2))
private val GoogleSansFlex = FontFamily(
    Font(R.font.google_sans_flex, FontWeight.Normal, variationSettings = FontVariation.Settings(FontWeight.Normal, FontStyle.Normal, FontVariation.Setting("ROND", 100f))),
    Font(R.font.google_sans_flex, FontWeight.Medium, variationSettings = FontVariation.Settings(FontWeight.Medium, FontStyle.Normal, FontVariation.Setting("ROND", 100f))),
    Font(R.font.google_sans_flex, FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontWeight.SemiBold, FontStyle.Normal, FontVariation.Setting("ROND", 100f))),
    Font(R.font.google_sans_flex, FontWeight.Bold, variationSettings = FontVariation.Settings(FontWeight.Bold, FontStyle.Normal, FontVariation.Setting("ROND", 100f))),
    Font(R.font.google_sans_flex, FontWeight.Black, variationSettings = FontVariation.Settings(FontWeight.Black, FontStyle.Normal, FontVariation.Setting("ROND", 100f)))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { BlockApp() }
    }
}

private class LocalData(context: Context) {
    private val prefs = context.getSharedPreferences("block_local", Context.MODE_PRIVATE)
    private val today: String get() = LocalDate.now().toString()

    fun check(id: String) = prefs.getBoolean("check:$today:$id", false)
    fun toggleCheck(id: String): Boolean {
        val value = !check(id)
        prefs.edit().putBoolean("check:$today:$id", value).apply()
        return value
    }
    fun checkedCount(ids: List<String>) = ids.count(::check)
    fun text(id: String, original: String) = prefs.getString("edit:$id", original) ?: original
    fun saveText(id: String, value: String) { prefs.edit().putString("edit:$id", value).apply() }
    fun workingSets(day: TrainingDay, exercise: Exercise, week: Int): Int =
        if (day.isRun) 1 else PlanData.workingSets(exercise.copy(prescription = text("${day.id}:${exercise.id}:prescription", exercise.prescription)), week)
    fun workingRir(day: TrainingDay, exercise: Exercise, week: Int): String =
        PlanData.workingRir(exercise.copy(rir = text("${day.id}:${exercise.id}:rir", exercise.rir)), week)
    fun startDate(): LocalDate {
        val saved = prefs.getString("start_date", null)
        if (saved != null) return runCatching { LocalDate.parse(saved) }.getOrDefault(LocalDate.now())
        prefs.edit().putString("start_date", LocalDate.now().toString()).apply()
        return LocalDate.now()
    }
    fun setStartDate(value: LocalDate) { prefs.edit().putString("start_date", value.toString()).apply() }
    fun sessionJson(date: String): JSONArray = runCatching {
        JSONArray(prefs.getString("session:$date", "[]"))
    }.getOrDefault(JSONArray())
    fun beginSession(date: String) { prefs.edit().putBoolean("session_active:$date", true).apply() }
    fun nextExerciseIndex(date: String, day: TrainingDay, week: Int): Int {
        val records = sessionJson(date)
        return day.exercises.indexOfFirst { exercise ->
            val completed = (0 until records.length()).count { i -> records.optJSONObject(i)?.optString("exerciseId") == exercise.id }
            completed < workingSets(day, exercise, week)
        }.let { if (it < 0) day.exercises.size else it }
    }
    fun saveSet(date: String, dayId: String, exercise: Exercise, displayName: String, target: String, setNo: Int, weight: String, reps: String, rir: String) {
        val records = sessionJson(date)
        records.put(JSONObject().apply {
            put("day", dayId); put("exerciseId", exercise.id); put("exercise", displayName); put("target", target)
            put("set", setNo); put("weight", weight); put("reps", reps); put("rir", rir); put("rest", text("$dayId:${exercise.id}:rest", exercise.rest))
            put("time", System.currentTimeMillis())
        })
        prefs.edit().putString("session:$date", records.toString()).putBoolean("session_active:$date", true).apply()
    }
    fun active(date: String) = prefs.getBoolean("session_active:$date", false)
    fun finish(date: String) { prefs.edit().putBoolean("session_active:$date", false).putBoolean("session_done:$date", true).apply() }
    fun sessionDone(date: String) = prefs.getBoolean("session_done:$date", false)
    fun allSessions(): List<Pair<String, JSONArray>> = prefs.all.keys.mapNotNull { key ->
        if (!key.startsWith("session:") || key.startsWith("session_active:") || key.startsWith("session_done:")) return@mapNotNull null
        val date = key.removePrefix("session:")
        runCatching { date to JSONArray(prefs.getString(key, "[]")) }.getOrNull()
    }.sortedByDescending { it.first }
    fun addBodyWeight(weight: String) {
        val values = prefs.getStringSet("body_weight", emptySet())?.toMutableSet() ?: mutableSetOf()
        values.removeAll { it.startsWith("$today|") }
        values.add("$today|$weight")
        prefs.edit().putStringSet("body_weight", values).apply()
    }
    fun bodyWeights() = prefs.getStringSet("body_weight", emptySet())?.toList().orEmpty().sortedByDescending { it.substringBefore('|') }
    fun addMeasurement(area: String, value: String) {
        val values = prefs.getStringSet("measurements", emptySet())?.toMutableSet() ?: mutableSetOf()
        values.add("$today|$area|$value")
        prefs.edit().putStringSet("measurements", values).apply()
    }
    fun measurements() = prefs.getStringSet("measurements", emptySet())?.toList().orEmpty().sortedByDescending { it.substringBefore('|') }
    fun timerEnd() = prefs.getLong("timer_end", 0L)
    fun timerEnd(value: Long) { prefs.edit().putLong("timer_end", value).apply() }
    fun hapticsEnabled() = prefs.getBoolean("haptics", true)
    fun setHaptics(enabled: Boolean) { prefs.edit().putBoolean("haptics", enabled).apply() }
    fun screenAwake() = prefs.getBoolean("screen_awake", true)
    fun setScreenAwake(enabled: Boolean) { prefs.edit().putBoolean("screen_awake", enabled).apply() }
    fun timerSound() = prefs.getBoolean("timer_sound", false)
    fun setTimerSound(enabled: Boolean) { prefs.edit().putBoolean("timer_sound", enabled).apply() }
    fun spokenCues() = prefs.getBoolean("spoken_cues", false)
    fun setSpokenCues(enabled: Boolean) { prefs.edit().putBoolean("spoken_cues", enabled).apply() }
    fun clearAll() { prefs.edit().clear().apply() }
}

private enum class Destination(val label: String, val icon: Int) {
    Today("Today", 0), Block("BLOCK", 1), Fuel("FUEL", 2), Progress("Progress", 3)
}

@Composable
private fun BlockApp() {
    val context = LocalContext.current
    val data = remember { LocalData(context.applicationContext) }
    val view = LocalView.current
    var destination by rememberSaveable { mutableStateOf(Destination.Today) }
    var editing by rememberSaveable { mutableStateOf(false) }
    var dataVersion by remember { mutableIntStateOf(0) }
    var selectedDayId by rememberSaveable { mutableStateOf(dayFor(LocalDate.now()).id) }
    var activeWorkout by rememberSaveable { mutableStateOf(data.active(LocalDate.now().toString())) }
    var activeExercise by rememberSaveable { mutableIntStateOf(data.nextExerciseIndex(LocalDate.now().toString(), dayFor(LocalDate.now()), PlanData.weekNumber(data.startDate().toEpochDay(), LocalDate.now().toEpochDay()))) }
    var editorFuel by rememberSaveable { mutableStateOf(false) }
    var settingsOpen by rememberSaveable { mutableStateOf(false) }
    var todayTick by remember { mutableIntStateOf(0) }
    val today = LocalDate.now()
    val dayId = dayFor(today).id
    val week = PlanData.weekNumber(data.startDate().toEpochDay(), today.toEpochDay())
    val selectedDay = PlanData.days.firstOrNull { it.id == selectedDayId } ?: dayFor(today)

    DisposableEffect(activeWorkout, data.screenAwake()) {
        val keep = activeWorkout && data.screenAwake()
        if (keep) (context as? ComponentActivity)?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose { (context as? ComponentActivity)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) }
    }

    MaterialExpressiveTheme(
        colorScheme = expressiveLightColorScheme().copy(
            primary = Blue, onPrimary = Color.White, secondary = FuelOrange, onSecondary = Ink,
            tertiary = Color(0xFF8E63C7), background = Chalk, surface = Paper,
            onSurface = Ink, onSurfaceVariant = Muted, outlineVariant = Line
        ),
        motionScheme = MotionScheme.expressive(),
        typography = Typography(defaultFontFamily = GoogleSansFlex)
    ) {
        Surface(Modifier.fillMaxSize(), color = Chalk) {
            Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)) {
                if (editing) {
                    EditPlanScreen(data, editorFuel, { editorFuel = it }, {
                        editing = false
                        dataVersion++
                        if (data.hapticsEnabled()) view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                    })
                } else if (activeWorkout) {
                    WorkoutRunner(
                        day = selectedDay,
                        week = week,
                        data = data,
                        exerciseIndex = activeExercise,
                        onExercise = { activeExercise = it },
                        onFinish = {
                            data.finish(today.toString())
                            activeWorkout = false
                            activeExercise = 0
                            if (data.hapticsEnabled()) view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                        },
                        onExit = { activeWorkout = false }
                    )
                } else {
                    TopHeader(destination, onEdit = {
                        editorFuel = destination == Destination.Fuel
                        editing = true
                    }, onSettings = { settingsOpen = true })
                    AnimatedContent(
                        targetState = destination,
                        modifier = Modifier.weight(1f),
                        transitionSpec = {
                            (fadeIn(spring(stiffness = Spring.StiffnessMediumLow)) + slideInHorizontally { it / 18 }) togetherWith
                                (fadeOut() + slideOutHorizontally { -it / 24 })
                        },
                        label = "destination"
                    ) { target ->
                        when (target) {
                            Destination.Today -> TodayScreen(data, week, dataVersion,
                                onStart = { selectedDayId = dayId; activeExercise = data.nextExerciseIndex(today.toString(), dayFor(today), week); data.beginSession(today.toString()); activeWorkout = true },
                                onOpenBlock = { destination = Destination.Block },
                                onOpenFuel = { destination = Destination.Fuel },
                                onCheck = { id -> data.toggleCheck(id); todayTick++ }
                            )
                            Destination.Block -> BlockScreen(
                                data = data, week = week, selectedDay = selectedDay,
                                onSelectDay = { selectedDayId = it },
                                onStart = { activeExercise = data.nextExerciseIndex(today.toString(), selectedDay, week); data.beginSession(today.toString()); activeWorkout = true }
                            )
                            Destination.Fuel -> FuelScreen(data, todayTick, onCheck = { id ->
                                data.toggleCheck(id); todayTick++
                                if (data.hapticsEnabled()) view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                            })
                            Destination.Progress -> ProgressScreen(data)
                        }
                    }
                    BottomDock(destination, onSelect = {
                        destination = it
                        if (data.hapticsEnabled()) view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    })
                }
                if (settingsOpen) SettingsDialog(data, onClose = { settingsOpen = false; dataVersion++ })
            }
        }
    }
}

private fun dayFor(date: LocalDate): TrainingDay = when (date.dayOfWeek) {
    DayOfWeek.MONDAY -> PlanData.days[0]
    DayOfWeek.TUESDAY -> PlanData.days[1]
    DayOfWeek.WEDNESDAY -> PlanData.days[2]
    DayOfWeek.THURSDAY -> PlanData.days[3]
    DayOfWeek.FRIDAY -> PlanData.days[4]
    DayOfWeek.SATURDAY -> PlanData.days[5]
    DayOfWeek.SUNDAY -> PlanData.days[6]
}

@Composable
private fun TopHeader(destination: Destination, onEdit: () -> Unit, onSettings: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(start = 22.dp, end = 16.dp, top = 10.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text("BLOCK", fontSize = 13.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp, color = Muted)
            Text(destination.label, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = Ink)
        }
        if (destination == Destination.Block || destination == Destination.Fuel) {
            SmallAction("Edit plan", "edit", onEdit)
        }
        Spacer(Modifier.width(8.dp))
        SmallAction("Settings", "settings", onSettings)
    }
}

@Composable
private fun SmallAction(label: String, glyph: String, onClick: () -> Unit) {
    Surface(onClick = onClick, shape = CircleShape, color = Paper, modifier = Modifier.size(44.dp).semantics { contentDescription = label }, shadowElevation = 1.dp) {
        Box(contentAlignment = Alignment.Center) { AppGlyph(glyph, Modifier.size(20.dp), Ink) }
    }
}

@Composable
private fun BottomDock(selected: Destination, onSelect: (Destination) -> Unit) {
    Surface(
        Modifier.fillMaxWidth().padding(start = 14.dp, end = 14.dp, top = 8.dp, bottom = 8.dp),
        shape = RoundedCornerShape(38.dp), color = Color(0xFFF0EFEC), shadowElevation = 7.dp, tonalElevation = 2.dp
    ) {
        Row(Modifier.padding(6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Destination.entries.forEach { destination ->
                val active = destination == selected
                val bg by animateColorAsState(if (active) Paper else Color.Transparent, spring(stiffness = Spring.StiffnessMediumLow), label = "dockColor")
                val scale by animateFloatAsState(if (active) 1f else 0.94f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), label = "dockScale")
                Row(
                    Modifier.weight(1f).heightIn(min = 52.dp).scale(scale).clip(RoundedCornerShape(30.dp))
                        .background(bg).clickable { onSelect(destination) }
                        .semantics { contentDescription = destination.label }
                        .padding(horizontal = 5.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                ) {
                    if (active) {
                        AppGlyph(destination.label, Modifier.size(19.dp), Blue)
                        Spacer(Modifier.width(6.dp))
                        Text(destination.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Ink, maxLines = 1, softWrap = false)
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            AppGlyph(destination.label, Modifier.size(17.dp), Muted)
                            Text(destination.label, fontSize = 9.sp, fontWeight = FontWeight.Medium, color = Muted, maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TodayScreen(
    data: LocalData,
    week: Int,
    dataVersion: Int,
    onStart: () -> Unit,
    onOpenBlock: () -> Unit,
    onOpenFuel: () -> Unit,
    onCheck: (String) -> Unit
) {
    val today = LocalDate.now()
    val day = dayFor(today)
    val mealCount = data.checkedCount(PlanData.meals.map { it.id })
    val mobilityCount = data.checkedCount(PlanData.mobility.take(8).indices.map { "mobility_$it" })
    LazyColumn(
        Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 18.dp, end = 18.dp, top = 8.dp, bottom = 18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(today.format(java.time.format.DateTimeFormatter.ofPattern("EEEE · d MMMM", Locale.getDefault())).uppercase(), fontSize = 12.sp, letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold, color = Muted)
                    Text("Your day,\nin motion.", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, lineHeight = 38.sp), color = Ink)
                }
                WeekBadge(week)
            }
        }
        item {
            WorkoutHero(day, week, data.sessionDone(today.toString()), data.active(today.toString()), onOpenBlock, onStart)
        }
        item {
            SectionHeading("FUEL", "$mealCount of ${PlanData.meals.size} checked", onOpenFuel)
            Spacer(Modifier.height(8.dp))
            val nextMeal = PlanData.meals.firstOrNull { !data.check(it.id) }
            if (nextMeal != null) {
                Card(
                    onClick = { onOpenFuel() }, shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = FuelPalette[mealCount % FuelPalette.size])
                ) {
                    Row(Modifier.fillMaxWidth().padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("UP NEXT · ${nextMeal.time}", fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = Ink.copy(alpha = .62f))
                            Spacer(Modifier.height(5.dp))
                            Text(nextMeal.name, fontSize = 23.sp, fontWeight = FontWeight.Bold, color = Ink)
                            Text(nextMeal.detail, maxLines = 2, overflow = TextOverflow.Ellipsis, fontSize = 14.sp, lineHeight = 20.sp, color = Ink.copy(alpha = .78f))
                        }
                        RoundIconButton("check", "Check next meal", onClick = { onCheck(nextMeal.id) }, filled = true)
                    }
                }
            } else {
                ColorCard("All fueled up", "Your plan check-ins are complete for today.", FuelGreen)
            }
        }
        item {
            SectionHeading("MORNING RESET", "$mobilityCount / 8 quick set", {})
            Spacer(Modifier.height(8.dp))
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Paper)) {
                Column(Modifier.padding(16.dp)) {
                    PlanData.mobility.take(8).forEachIndexed { index, (name, dose) ->
                        CheckRow(name, dose.substringBefore(" ·"), data.check("mobility_$index")) { onCheck("mobility_$index") }
                        if (index != 7) DividerLine()
                    }
                    Text("Short on time? This first-eight sequence is your quick option.", fontSize = 12.sp, color = Muted, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatTile("TODAY", if (data.sessionDone(today.toString())) "Done" else day.day, Modifier.weight(1f))
                StatTile("THIS WEEK", "Week $week", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun WeekBadge(week: Int) {
    Surface(shape = RoundedCornerShape(22.dp), color = Ink, modifier = Modifier.size(72.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("WEEK", fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = .68f))
            Text(week.toString().padStart(2, '0'), fontSize = 26.sp, fontWeight = FontWeight.Black, color = Lime)
        }
    }
}

@Composable
private fun WorkoutHero(day: TrainingDay, week: Int, done: Boolean, active: Boolean, onOpen: () -> Unit, onStart: () -> Unit) {
    Card(shape = RoundedCornerShape(32.dp), colors = CardDefaults.cardColors(containerColor = Ink)) {
        Column(Modifier.fillMaxWidth().clickable(onClick = onOpen).padding(20.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text("${day.day.uppercase()} · ${if (day.isRun) "RECOVERY" else "TRAINING"}", fontSize = 11.sp, letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold, color = Lime)
                    Spacer(Modifier.height(9.dp))
                    Text(if (day.isRun) "Run easy.\nRecover well." else day.focus.substringBefore(" ·") + "\n${day.focus.substringAfter(" ·", "")}", fontSize = 29.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Text("${if (day.isRun) "↗" else "B"}", fontSize = 45.sp, fontWeight = FontWeight.Black, color = Color.White.copy(alpha = .18f))
            }
            Spacer(Modifier.height(15.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(day.duration, fontSize = 14.sp, color = Color.White.copy(alpha = .73f))
                Text("  ·  ", color = Color.White.copy(alpha = .35f))
                Text(if (week <= 4) "WEEK $week · RAMP-UP" else "WEEK $week · FULL PLAN", fontSize = 11.sp, letterSpacing = .6.sp, fontWeight = FontWeight.Bold, color = Lime)
            }
            Spacer(Modifier.height(18.dp))
            Button(
                onClick = if (done) onOpen else onStart,
                modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Lime, contentColor = Ink)
            ) {
                Text(if (done) "Review today’s session" else if (active) "Resume ${if (day.isRun) "session" else "workout"}" else "Start ${if (day.isRun) "recovery" else "workout"}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(Modifier.width(8.dp)); AppGlyph("arrow", Modifier.size(18.dp), Ink)
            }
        }
    }
}

@Composable
private fun BlockScreen(
    data: LocalData,
    week: Int,
    selectedDay: TrainingDay,
    onSelectDay: (String) -> Unit,
    onStart: () -> Unit
) {
    var showDetail by rememberSaveable(selectedDay.id) { mutableStateOf(false) }
    var mobilityDone by remember { mutableIntStateOf(PlanData.mobility.indices.count { data.check("mobility_$it") }) }
    if (showDetail) {
        WorkoutDetail(data, selectedDay, week, onBack = { showDetail = false }, onStart = onStart)
        return
    }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("THE TRAINING SPLIT", fontSize = 11.sp, letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold, color = Muted)
                    Text("Build, one block\nat a time.", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), lineHeight = 31.sp, color = Ink)
                }
                WeekBadge(week)
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                PlanData.days.forEach { day ->
                    val isSelected = day.id == selectedDay.id
                    Surface(
                        onClick = { onSelectDay(day.id) },
                        modifier = Modifier.weight(1f).height(64.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) Ink else Paper
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text(day.day.take(1), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Ink)
                            Box(Modifier.padding(top = 4.dp).size(5.dp).clip(CircleShape).background(if (isSelected) Lime else Blue))
                        }
                    }
                }
            }
        }
        item {
            Card(
                onClick = { showDetail = true }, shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Paper)
            ) {
                Column(Modifier.fillMaxWidth().padding(20.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Column(Modifier.weight(1f)) {
                            Text(selectedDay.day.uppercase(), fontSize = 11.sp, letterSpacing = 1.4.sp, fontWeight = FontWeight.Bold, color = Blue)
                            Spacer(Modifier.height(4.dp))
                            Text(selectedDay.focus.substringBefore(" ·"), fontSize = 27.sp, lineHeight = 30.sp, fontWeight = FontWeight.Bold, color = Ink)
                            Text(selectedDay.focus.substringAfter(" ·", "Recovery"), fontSize = 15.sp, color = Muted)
                        }
                        Text(selectedDay.duration, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Muted)
                    }
                    Spacer(Modifier.height(16.dp)); DividerLine(); Spacer(Modifier.height(12.dp))
                    selectedDay.exercises.take(4).forEachIndexed { index, exercise ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text((index + 1).toString().padStart(2, '0'), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Blue, modifier = Modifier.width(32.dp))
                            Text(data.text("${selectedDay.id}:${exercise.id}:name", exercise.name), Modifier.weight(1f), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text(if (selectedDay.isRun) exercise.prescription else "${data.workingSets(selectedDay, exercise, week)} sets", fontSize = 12.sp, color = Muted)
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("VIEW SESSION  →", fontSize = 12.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = Ink)
                }
            }
        }
        item {
            SectionHeading("EVERY MORNING", "${PlanData.mobility.size} moves", {})
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFEDECEA))) {
                Column(Modifier.padding(16.dp)) {
                    Text("Mobility · 10–15 min", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Text("Dynamic first. Keep it easy.", fontSize = 13.sp, color = Muted)
                    Spacer(Modifier.height(10.dp))
                    PlanData.mobility.forEachIndexed { index, (name, detail) ->
                        CheckRow(name, detail.substringBefore(" ·"), data.check("mobility_$index")) {
                            data.toggleCheck("mobility_$index")
                            mobilityDone = PlanData.mobility.indices.count { data.check("mobility_$it") }
                        }
                    }
                    Text("${mobilityDone} of ${PlanData.mobility.size} checked · Short on time? Use the first eight.", fontSize = 12.sp, color = Blue, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 6.dp))
                }
            }
        }
        item { SectionHeading("PROGRAM NOTES", "Tap to review", {}) }
        item { NoteCard("Double progression", "Add reps inside the prescribed range. Once every set reaches the top with the target RIR, consider increasing load next session.", BluePale) }
        item { NoteCard("Deload when needed", "The plan suggests every 6–8 weeks or when fatigue builds. It is a prompt, not an automatic schedule change.", Color(0xFFEDECEA)) }
    }
}

@Composable
private fun WorkoutDetail(data: LocalData, day: TrainingDay, week: Int, onBack: () -> Unit, onStart: () -> Unit) {
    val scroll = rememberScrollState()
    var voiceGuidance by remember(day.id) { mutableStateOf(data.spokenCues()) }
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().verticalScroll(scroll).padding(horizontal = 18.dp).padding(bottom = 100.dp)) {
            Text("‹  ALL TRAINING", fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp, color = Blue, modifier = Modifier.clickable(onClick = onBack).padding(vertical = 8.dp))
            Card(shape = RoundedCornerShape(30.dp), colors = CardDefaults.cardColors(containerColor = Ink), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(22.dp)) {
                    Text(day.day.uppercase() + "  ·  " + day.duration, fontSize = 11.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold, color = Lime)
                    Spacer(Modifier.height(9.dp))
                    Text(data.text("${day.id}:title", day.focus.substringBefore(" ·")), fontSize = 34.sp, lineHeight = 37.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(Modifier.height(5.dp))
                    Text(data.text("${day.id}:focus", day.focus.substringAfter(" ·", "Recovery")), fontSize = 15.sp, color = Color.White.copy(alpha = .72f))
                    Spacer(Modifier.height(18.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MetaPill("${day.exercises.size} MOVES")
                        MetaPill(if (week <= 2) "2 SETS · RIR 3–4" else if (week <= 4) "3 SETS · RIR 2–3" else "FULL PLAN")
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            PlanGroup("WARM-UP", "Prepare to move", day.warmup.mapIndexed { i, item -> data.text("${day.id}:warmup:$i", item) })
            Spacer(Modifier.height(14.dp))
            Text("${if (day.isRun) "SESSION" else "WORKING SETS"}", fontSize = 11.sp, letterSpacing = 1.6.sp, fontWeight = FontWeight.Bold, color = Blue)
            Spacer(Modifier.height(8.dp))
            day.exercises.forEachIndexed { index, item ->
                ExerciseCard(data, day, item, index, week)
                Spacer(Modifier.height(10.dp))
            }
            Spacer(Modifier.height(4.dp))
            PlanGroup("COOL-DOWN & RECOVERY", "Leave room to recover", day.cooldown.mapIndexed { i, item -> data.text("${day.id}:cooldown:$i", item) })
            Spacer(Modifier.height(14.dp))
            NoteCard("How to progress", "Keep reps inside the target range. If every set reaches the upper end with the prescribed RIR, the plan suggests adding weight next time.", BluePale)
        }
        Surface(
            Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp),
            shape = RoundedCornerShape(30.dp), color = Chalk.copy(alpha = .96f)
        ) {
            Row(Modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = onStart, Modifier.weight(1f).height(54.dp), shape = RoundedCornerShape(27.dp), colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Color.White)) {
                    Text(if (day.isRun) "Start session" else "Start workout", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(8.dp))
                RoundIconButton("sound", "Spoken exercise cues", {
                    voiceGuidance = !voiceGuidance
                    data.setSpokenCues(voiceGuidance)
                }, filled = voiceGuidance)
            }
        }
    }
}

@Composable
private fun MetaPill(text: String) {
    Surface(shape = CircleShape, color = Color.White.copy(alpha = .12f)) {
        Text(text, Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = .6.sp, color = Color.White)
    }
}

@Composable
private fun PlanGroup(title: String, subtitle: String, rows: List<String>) {
    Card(shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = Paper)) {
        Column(Modifier.fillMaxWidth().padding(18.dp)) {
            Text(title, fontSize = 12.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold, color = Blue)
            Text(subtitle, fontSize = 13.sp, color = Muted, modifier = Modifier.padding(top = 2.dp, bottom = 8.dp))
            rows.forEachIndexed { index, row ->
                Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.Top) {
                    Box(Modifier.padding(top = 6.dp).size(6.dp).clip(CircleShape).background(Blue))
                    Spacer(Modifier.width(10.dp))
                    Text(row, Modifier.weight(1f), fontSize = 14.sp, lineHeight = 20.sp, color = Ink)
                }
                if (index != rows.lastIndex) DividerLine()
            }
        }
    }
}

@Composable
private fun ExerciseCard(data: LocalData, day: TrainingDay, exercise: Exercise, index: Int, week: Int) {
    var expanded by rememberSaveable(exercise.id) { mutableStateOf(false) }
    val name = data.text("${day.id}:${exercise.id}:name", exercise.name)
    val target = data.text("${day.id}:${exercise.id}:prescription", exercise.prescription)
    val cue = data.text("${day.id}:${exercise.id}:cue", exercise.cue)
    val rir = data.text("${day.id}:${exercise.id}:rir", exercise.rir)
    val rest = data.text("${day.id}:${exercise.id}:rest", exercise.rest)
    Card(shape = RoundedCornerShape(25.dp), colors = CardDefaults.cardColors(containerColor = Paper), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().clickable { expanded = !expanded }.padding(17.dp).animateContentSize(spring(dampingRatio = Spring.DampingRatioNoBouncy))) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text((index + 1).toString().padStart(2, '0'), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Blue, modifier = Modifier.width(32.dp))
                Column(Modifier.weight(1f)) {
                    Text(name, fontSize = 17.sp, lineHeight = 21.sp, fontWeight = FontWeight.SemiBold, color = Ink)
                    Text(target, fontSize = 14.sp, color = Muted, modifier = Modifier.padding(top = 3.dp))
                }
                Text(if (day.isRun) "DETAILS" else "${data.workingSets(day, exercise, week)}×", fontSize = 11.sp, letterSpacing = .5.sp, fontWeight = FontWeight.Bold, color = Blue)
            }
            if (expanded) {
                Spacer(Modifier.height(14.dp)); DividerLine(); Spacer(Modifier.height(12.dp))
                Text("CUE", fontSize = 10.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold, color = Muted)
                Text(cue, fontSize = 14.sp, lineHeight = 20.sp, color = Ink, modifier = Modifier.padding(top = 4.dp))
                if (!day.isRun) {
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TinyTag("RIR ${data.workingRir(day, exercise, week)}")
                        TinyTag("REST $rest")
                    }
                    val alternatives = data.text("${day.id}:${exercise.id}:alternatives", exercise.alternatives)
                    if (alternatives.isNotBlank()) {
                        Text("ALTERNATIVES", fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = Muted, modifier = Modifier.padding(top = 12.dp))
                        Text(alternatives, fontSize = 13.sp, color = Ink, modifier = Modifier.padding(top = 3.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TinyTag(text: String) {
    Surface(shape = RoundedCornerShape(12.dp), color = BluePale) {
        Text(text, Modifier.padding(horizontal = 9.dp, vertical = 5.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Blue)
    }
}

@Composable
private fun FuelScreen(data: LocalData, version: Int, onCheck: (String) -> Unit) {
    val done = data.checkedCount(PlanData.meals.map { it.id })
    val progress = done.toFloat() / PlanData.meals.size
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 18.dp, end = 18.dp, top = 4.dp, bottom = 22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(shape = RoundedCornerShape(32.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF4D184))) {
                Column(Modifier.fillMaxWidth().padding(20.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(Modifier.weight(1f)) {
                            Text("A LITTLE RHYTHM, ALL DAY", fontSize = 11.sp, letterSpacing = 1.1.sp, fontWeight = FontWeight.Bold, color = Ink.copy(alpha = .6f))
                            Text("Fuel your\nkind of strong.", fontSize = 32.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold, color = Ink, modifier = Modifier.padding(top = 8.dp))
                        }
                        RingProgress(progress, "$done", Modifier.size(66.dp), Ink)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("$done of ${PlanData.meals.size} moments checked in", fontSize = 13.sp, color = Ink.copy(alpha = .72f))
                    Box(Modifier.fillMaxWidth().padding(top = 12.dp).height(7.dp).clip(CircleShape).background(Color.White.copy(alpha = .56f))) {
                        Box(Modifier.fillMaxWidth(progress.coerceAtLeast(.04f)).height(7.dp).clip(CircleShape).background(Ink))
                    }
                }
            }
        }
        item { SectionHeading("TODAY’S MEAL FLOW", "As written in your plan", {}) }
        itemsIndexed(PlanData.meals, key = { _, meal -> meal.id }) { index, base ->
            val meal = base.copy(
                time = data.text("meal:${base.id}:time", base.time),
                name = data.text("meal:${base.id}:name", base.name),
                detail = data.text("meal:${base.id}:detail", base.detail)
            )
            MealCard(meal, index, data.check(meal.id), onCheck = { onCheck(meal.id) })
        }
        item {
            ColorCard("Daily targets", "Approx. 2,600–2,800 kcal  ·  140–160 g protein\n300–350 g carbs  ·  70–80 g fat\n\nTargets are shown as written in the plan.", FuelPurple)
        }
        item {
            ColorCard("Hydration", "The plan’s daily guidance is 3+ L water. Track the prompts in your routine; this app does not infer intake from meal check-ins.", FuelBlue)
        }
    }
}

@Composable
private fun MealCard(meal: Meal, index: Int, checked: Boolean, onCheck: () -> Unit) {
    val cardColor = FuelPalette[index % FuelPalette.size]
    Card(shape = RoundedCornerShape(if (index % 2 == 0) 28.dp else 22.dp), colors = CardDefaults.cardColors(containerColor = if (checked) cardColor.copy(alpha = .52f) else cardColor)) {
        Row(Modifier.fillMaxWidth().clickable(onClick = onCheck).padding(horizontal = 17.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.width(64.dp)) {
                Text(meal.time.substringBefore(' '), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
                Text(meal.time.substringAfter(' '), fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = Ink.copy(alpha = .58f))
            }
            Box(Modifier.padding(horizontal = 13.dp).width(1.dp).height(54.dp).background(Ink.copy(alpha = .16f)))
            Column(Modifier.weight(1f)) {
                Text(meal.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
                Spacer(Modifier.height(3.dp))
                Text(meal.detail, fontSize = 13.sp, lineHeight = 18.sp, color = Ink.copy(alpha = .78f))
                if (meal.supplement.isNotBlank()) {
                    Spacer(Modifier.height(7.dp))
                    Text(meal.supplement, fontSize = 10.sp, letterSpacing = .7.sp, fontWeight = FontWeight.Bold, color = Ink.copy(alpha = .68f))
                }
            }
            Spacer(Modifier.width(8.dp))
            AnimatedCheck(checked, onCheck)
        }
    }
}

@Composable
private fun ProgressScreen(data: LocalData) {
    var weight by rememberSaveable { mutableStateOf("") }
    var measurement by rememberSaveable { mutableStateOf("") }
    var measurementArea by rememberSaveable { mutableStateOf("Waist") }
    val sessions = data.allSessions()
    val date = LocalDate.now()
    val todayIndex = when (date.dayOfWeek) { DayOfWeek.MONDAY -> 0; DayOfWeek.TUESDAY -> 1; DayOfWeek.WEDNESDAY -> 2; DayOfWeek.THURSDAY -> 3; DayOfWeek.FRIDAY -> 4; DayOfWeek.SATURDAY -> 5; else -> 6 }
    val completedThisWeek = (0..todayIndex).count { index ->
        val day = PlanData.days[index]
        val d = date.minusDays((todayIndex - index).toLong())
        data.sessionDone(d.toString())
    }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = androidx.compose.foundation.layout.PaddingValues(start = 18.dp, end = 18.dp, top = 6.dp, bottom = 22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Card(shape = RoundedCornerShape(30.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFB8D4F7))) {
                Column(Modifier.fillMaxWidth().padding(20.dp)) {
                    Text("THE WORK ADDS UP", fontSize = 11.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold, color = Ink.copy(alpha = .62f))
                    Text("Progress,\nnot perfection.", fontSize = 31.sp, lineHeight = 34.sp, fontWeight = FontWeight.Bold, color = Ink, modifier = Modifier.padding(top = 7.dp))
                    Spacer(Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        MetricPill("${sessions.size}", "SESSIONS")
                        MetricPill("$completedThisWeek", "THIS WEEK")
                        MetricPill("${sessions.sumOf { it.second.length() }}", "SETS LOGGED")
                    }
                }
            }
        }
        item {
            SectionHeading("BODY WEIGHT", "Manual entries only", {})
            Card(shape = RoundedCornerShape(25.dp), colors = CardDefaults.cardColors(containerColor = Paper)) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(weight, { weight = it.filter { c -> c.isDigit() || c == '.' }.take(6) }, Modifier.weight(1f), label = { Text("Weight · kg") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), shape = RoundedCornerShape(18.dp))
                        Spacer(Modifier.width(9.dp))
                        Button(onClick = {
                            if ((weight.toFloatOrNull() ?: 0f) > 0f) { data.addBodyWeight(weight); weight = "" }
                        }, shape = RoundedCornerShape(18.dp)) { Text("Save") }
                    }
                    data.bodyWeights().take(5).forEach { entry ->
                        val parts = entry.split('|')
                        if (parts.size == 2) Text("${parts[0]}     ${parts[1]} kg", fontSize = 14.sp, color = Ink, modifier = Modifier.padding(top = 10.dp))
                    }
                    if (data.bodyWeights().isEmpty()) Text("Your first entry will appear here. The plan suggests weighing 2–3 times a week at the same time of day.", fontSize = 13.sp, lineHeight = 19.sp, color = Muted, modifier = Modifier.padding(top = 9.dp))
                }
            }
        }
        item {
            SectionHeading("BODY MEASUREMENTS", "Plan suggests every 4 weeks", {})
            Card(shape = RoundedCornerShape(25.dp), colors = CardDefaults.cardColors(containerColor = Paper)) {
                Column(Modifier.padding(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Chest", "Waist", "Arms").forEach { area -> FilterChip(area, measurementArea == area) { measurementArea = area } }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(top = 6.dp)) {
                        listOf("Thighs", "Shoulders").forEach { area -> FilterChip(area, measurementArea == area) { measurementArea = area } }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                        OutlinedTextField(measurement, { measurement = it.filter { c -> c.isDigit() || c == '.' }.take(6) }, Modifier.weight(1f), label = { Text("$measurementArea · cm") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), shape = RoundedCornerShape(18.dp))
                        Spacer(Modifier.width(9.dp))
                        Button(onClick = {
                            if ((measurement.toFloatOrNull() ?: 0f) > 0f) {
                                data.addMeasurement(measurementArea, measurement)
                                measurement = ""
                            }
                        }, shape = RoundedCornerShape(18.dp)) { Text("Save") }
                    }
                    data.measurements().take(8).forEach { item ->
                        val parts = item.split('|')
                        if (parts.size == 3) Text("${parts[0]}   ${parts[1]}   ${parts[2]} cm", fontSize = 13.sp, color = Ink, modifier = Modifier.padding(top = 9.dp))
                    }
                    if (data.measurements().isEmpty()) Text("Add a measurement when you take one. Entries stay on this device.", fontSize = 13.sp, color = Muted, modifier = Modifier.padding(top = 9.dp))
                }
            }
        }
        item { SectionHeading("SESSION HISTORY", "Saved on this device", {}) }
        if (sessions.isEmpty()) item { ColorCard("Your log starts here", "Complete a workout and log a set. You’ll see your session history and exercise progression here.", FuelGreen) }
        itemsIndexed(sessions.take(30), key = { _, item -> item.first }) { _, (sessionDate, entries) ->
            Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Paper)) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    Text(sessionDate, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = .5.sp, color = Blue)
                    Text("${entries.length()} sets · ${entries.optJSONObject(0)?.optString("day", "Session") ?: "Session"}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Ink, modifier = Modifier.padding(top = 3.dp))
                    entries.takeLast(2).forEach { obj ->
                        val summary = if (obj.optString("weight").isBlank()) "${obj.optString("reps")} tracked" else "${obj.optString("weight")} kg × ${obj.optString("reps")}"
                        Text("${obj.optString("exercise")} · $summary", fontSize = 12.sp, color = Muted, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
        item {
            var haptics by remember { mutableStateOf(data.hapticsEnabled()) }
            var awake by remember { mutableStateOf(data.screenAwake()) }
            SettingsCard("Quick settings") {
                ToggleRow("Set and check-in haptics", haptics) { haptics = it; data.setHaptics(it) }
                DividerLine()
                ToggleRow("Keep screen awake during workouts", awake) { awake = it; data.setScreenAwake(it) }
            }
        }
    }
}

@Composable
private fun MetricPill(value: String, label: String) {
    Column(Modifier.clip(RoundedCornerShape(18.dp)).background(Color.White.copy(alpha = .63f)).padding(horizontal = 12.dp, vertical = 8.dp)) {
        Text(value, fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Ink)
        Text(label, fontSize = 9.sp, letterSpacing = .6.sp, fontWeight = FontWeight.Bold, color = Ink.copy(alpha = .58f))
    }
}

@Composable
private fun WorkoutRunner(
    day: TrainingDay,
    week: Int,
    data: LocalData,
    exerciseIndex: Int,
    onExercise: (Int) -> Unit,
    onFinish: () -> Unit,
    onExit: () -> Unit
) {
    val today = LocalDate.now().toString()
    val exercise = day.exercises.getOrNull(exerciseIndex)
    if (exercise == null) {
        WorkoutComplete(day, data.sessionJson(today).length(), onFinish)
        return
    }
    val plannedSets = data.workingSets(day, exercise, week)
    val loggedCount = (0 until data.sessionJson(today).length()).count { i ->
        val item = data.sessionJson(today).optJSONObject(i)
        item?.optString("exerciseId") == exercise.id
    }
    val setNo = (loggedCount + 1).coerceAtMost(plannedSets)
    val view = LocalView.current
    val context = LocalContext.current
    var speechReady by remember { mutableStateOf(false) }
    var speechEngine by remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context, data.spokenCues()) {
        var engine: TextToSpeech? = null
        if (data.spokenCues()) {
            engine = TextToSpeech(context) { status ->
                speechReady = status == TextToSpeech.SUCCESS
                engine?.language = Locale.getDefault()
            }
            speechEngine = engine
        }
        onDispose {
            engine?.stop()
            engine?.shutdown()
            speechEngine = null
            speechReady = false
        }
    }
    LaunchedEffect(exercise.id, speechReady, data.spokenCues()) {
        if (speechReady && data.spokenCues()) {
            val spokenName = data.text("${day.id}:${exercise.id}:name", exercise.name)
            val spokenCue = data.text("${day.id}:${exercise.id}:cue", exercise.cue)
            speechEngine?.speak("$spokenName. $spokenCue", TextToSpeech.QUEUE_FLUSH, null, "cue-${exercise.id}")
        }
    }
    var load by remember(exercise.id, setNo) { mutableStateOf(lastValue(data, exercise.id, "weight")) }
    val prescribedValue = if (day.isRun) {
        Regex("\\d+(?:\\.\\d+)?").find(exercise.prescription)?.value.orEmpty()
    } else {
        Regex("\\d+(?:\\.\\d+)?").find(exercise.prescription.substringAfter("×", exercise.prescription))?.value.orEmpty()
    }
    var reps by remember(exercise.id, setNo) { mutableStateOf(lastValue(data, exercise.id, "reps").ifBlank { prescribedValue }) }
    var rir by remember(exercise.id, setNo) { mutableStateOf(data.workingRir(day, exercise, week).substringBefore('–').substringBefore(' ').trim()) }
    var timerRefresh by remember { mutableIntStateOf(0) }
    var timerNow by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(timerRefresh) {
        val timerEndAtStart = data.timerEnd()
        val hasPendingTimer = timerEndAtStart > System.currentTimeMillis()
        while (timerEndAtStart > System.currentTimeMillis()) {
            timerNow = System.currentTimeMillis()
            delay(500)
        }
        timerNow = System.currentTimeMillis()
        if (hasPendingTimer && data.timerEnd() == timerEndAtStart && data.hapticsEnabled()) {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
        if (hasPendingTimer && data.timerEnd() == timerEndAtStart && data.timerSound()) {
            val tone = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 55)
            tone.startTone(ToneGenerator.TONE_PROP_ACK, 160)
            delay(220)
            tone.release()
        }
    }
    val timerEnd = data.timerEnd()
    val secondsLeft = ((timerEnd - timerNow) / 1000).toInt().coerceAtLeast(0)
    Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars).padding(horizontal = 18.dp)) {
        Row(Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            RoundIconButton("back", "Close workout", onExit, filled = false)
            Text("${day.day.uppercase()} · WEEK $week", fontSize = 11.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold, color = Muted)
            TextButton(onClick = onFinish) { Text("Finish", color = Blue, fontWeight = FontWeight.Bold) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.fillMaxWidth()) {
            day.exercises.forEachIndexed { idx, _ ->
                val color = if (idx < exerciseIndex) Blue else if (idx == exerciseIndex) Ink else Line
                Box(Modifier.weight(1f).height(4.dp).clip(CircleShape).background(color))
            }
        }
        Text("MOVE ${exerciseIndex + 1} OF ${day.exercises.size}", fontSize = 11.sp, letterSpacing = 1.5.sp, fontWeight = FontWeight.Bold, color = Blue, modifier = Modifier.padding(top = 25.dp))
        Text(data.text("${day.id}:${exercise.id}:name", exercise.name), fontSize = 32.sp, lineHeight = 35.sp, fontWeight = FontWeight.Bold, color = Ink, modifier = Modifier.padding(top = 7.dp))
        Text(data.text("${day.id}:${exercise.id}:prescription", exercise.prescription), fontSize = 17.sp, fontWeight = FontWeight.Medium, color = Muted, modifier = Modifier.padding(top = 7.dp))
        Spacer(Modifier.height(16.dp))
        Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = BluePale)) {
            Column(Modifier.padding(16.dp)) {
                Text("FORM CUE", fontSize = 10.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold, color = Blue)
                Text(data.text("${day.id}:${exercise.id}:cue", exercise.cue), fontSize = 15.sp, lineHeight = 22.sp, color = Ink, modifier = Modifier.padding(top = 5.dp))
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            StatTile("SET", "$setNo / $plannedSets", Modifier.weight(1f))
            StatTile("TARGET RIR", data.workingRir(day, exercise, week), Modifier.weight(1f))
            StatTile("REST", data.text("${day.id}:${exercise.id}:rest", exercise.rest), Modifier.weight(1f))
        }
        Spacer(Modifier.height(14.dp))
        Text("LOG THIS SET", fontSize = 11.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold, color = Muted)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!day.isRun) NumberField("Load · kg", load, { load = it }, Modifier.weight(1f))
            NumberField(if (day.isRun) "Distance / time", reps, { reps = it }, Modifier.weight(1f))
            if (!day.isRun && exercise.rir != "—") NumberField("RIR", rir, { rir = it }, Modifier.weight(.7f))
        }
        if (exerciseIndex == 0 && data.sessionJson(today).length() > 0) {
            Text("Last set values are suggested. Adjust before logging.", fontSize = 12.sp, color = Muted, modifier = Modifier.padding(top = 8.dp))
        }
        Spacer(Modifier.height(12.dp))
        if (secondsLeft > 0) {
            Card(shape = RoundedCornerShape(23.dp), colors = CardDefaults.cardColors(containerColor = Ink)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("REST", fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = Lime)
                        Text("${secondsLeft / 60}:${(secondsLeft % 60).toString().padStart(2, '0')}", fontSize = 25.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    TextButton(onClick = { data.timerEnd(System.currentTimeMillis() + 30_000L); timerRefresh++ }) { Text("+30s", color = Lime) }
                    TextButton(onClick = { data.timerEnd(0L); timerRefresh++ }) { Text("Skip", color = Color.White) }
                }
            }
            Spacer(Modifier.height(9.dp))
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = {
                val name = data.text("${day.id}:${exercise.id}:name", exercise.name)
                val target = data.text("${day.id}:${exercise.id}:prescription", exercise.prescription)
                data.saveSet(today, day.id, exercise, name, target, setNo, load, reps, rir)
                val rest = restSeconds(data.text("${day.id}:${exercise.id}:rest", exercise.rest))
                data.timerEnd(if (rest > 0) System.currentTimeMillis() + rest * 1000L else 0L)
                timerRefresh++
                if (data.hapticsEnabled()) view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                if (setNo >= plannedSets && exerciseIndex < day.exercises.lastIndex) onExercise(exerciseIndex + 1)
                else if (setNo >= plannedSets) onExercise(day.exercises.size)
            },
            modifier = Modifier.fillMaxWidth().height(58.dp), enabled = reps.isNotBlank(),
            shape = RoundedCornerShape(29.dp), colors = ButtonDefaults.buttonColors(containerColor = Ink, contentColor = Color.White)
        ) { Text(if (setNo < plannedSets) "Log set ${setNo}" else if (exerciseIndex == day.exercises.lastIndex) "Finish session" else "Log set & continue", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
        Text("${exerciseIndex + 1}  /  ${day.exercises.size}", fontSize = 11.sp, color = Muted, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 8.dp, bottom = 7.dp))
    }
}

private fun lastValue(data: LocalData, exerciseId: String, field: String): String {
    data.allSessions().forEach { (_, entries) ->
        for (i in entries.length() - 1 downTo 0) {
            val item = entries.optJSONObject(i) ?: continue
            if (item.optString("exerciseId") == exerciseId && item.optString(field).isNotBlank()) return item.optString(field)
        }
    }
    return ""
}

private fun restSeconds(value: String): Int {
    val amount = Regex("\\d+").find(value)?.value?.toIntOrNull() ?: 0
    return when {
        "min" in value -> amount * 60
        "sec" in value -> amount
        else -> 0
    }
}

@Composable
private fun NumberField(label: String, value: String, onValue: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(value, { text -> onValue(text.filter { it.isDigit() || it == '.' || it == '-' }.take(7)) }, modifier,
        label = { Text(label, maxLines = 1, fontSize = 11.sp) }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), shape = RoundedCornerShape(17.dp))
}

@Composable
private fun WorkoutComplete(day: TrainingDay, setCount: Int, onFinish: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        RingProgress(1f, "✓", Modifier.size(100.dp), Blue)
        Text("Block complete.", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Ink, modifier = Modifier.padding(top = 18.dp))
        Text("${day.day} · $setCount sets logged", fontSize = 15.sp, color = Muted, modifier = Modifier.padding(top = 6.dp))
        Button(onClick = onFinish, modifier = Modifier.fillMaxWidth().padding(top = 28.dp).height(56.dp), shape = RoundedCornerShape(28.dp)) { Text("Done", fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun EditPlanScreen(data: LocalData, initialFuel: Boolean, onMode: (Boolean) -> Unit, onDone: () -> Unit) {
    var fuel by rememberSaveable { mutableStateOf(initialFuel) }
    var selectedDay by rememberSaveable { mutableStateOf(PlanData.days.first().id) }
    Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.navigationBars)) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            RoundIconButton("back", "Close editor", onDone, filled = false)
            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text("EDIT PLAN", fontSize = 11.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold, color = Blue)
                Text(if (fuel) "FUEL routine" else "BLOCK training", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Ink)
            }
            TextButton(onClick = onDone) { Text("Done", fontWeight = FontWeight.Bold) }
        }
        Row(Modifier.padding(horizontal = 18.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip("BLOCK", !fuel) { fuel = false; onMode(false) }
            FilterChip("FUEL", fuel) { fuel = true; onMode(true) }
        }
        if (!fuel) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                PlanData.days.forEach { day ->
                    val active = day.id == selectedDay
                    Surface(onClick = { selectedDay = day.id }, shape = CircleShape, color = if (active) Ink else Paper, modifier = Modifier.weight(1f)) {
                        Text(day.day.take(1), Modifier.padding(vertical = 9.dp), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (active) Color.White else Ink, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    }
                }
            }
            val day = PlanData.days.first { it.id == selectedDay }
            LazyColumn(Modifier.weight(1f), contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    EditField("Session name", "${day.id}:title", day.focus.substringBefore(" ·"), data)
                    EditField("Focus", "${day.id}:focus", day.focus.substringAfter(" ·", "Recovery"), data)
                }
                item { Text("WARM-UP", fontSize = 11.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold, color = Blue) }
                itemsIndexed(day.warmup, key = { i, _ -> "warmup_$i" }) { index, line ->
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Paper)) {
                        EditField("Warm-up ${index + 1}", "${day.id}:warmup:$index", line, data)
                    }
                }
                item { Text("EXERCISES & TARGETS", fontSize = 11.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold, color = Blue) }
                itemsIndexed(day.exercises, key = { _, e -> e.id }) { _, exercise ->
                    Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Paper)) {
                        Column(Modifier.padding(14.dp)) {
                            EditField("Exercise", "${day.id}:${exercise.id}:name", exercise.name, data)
                            EditField("Sets / reps / distance", "${day.id}:${exercise.id}:prescription", exercise.prescription, data)
                            EditField("RIR target", "${day.id}:${exercise.id}:rir", exercise.rir, data)
                            EditField("Rest", "${day.id}:${exercise.id}:rest", exercise.rest, data)
                            EditField("Technique cue", "${day.id}:${exercise.id}:cue", exercise.cue, data)
                            EditField("Alternatives", "${day.id}:${exercise.id}:alternatives", exercise.alternatives, data)
                        }
                    }
                }
                item { Text("COOL-DOWN", fontSize = 11.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold, color = Blue) }
                itemsIndexed(day.cooldown, key = { i, _ -> "cooldown_$i" }) { index, line ->
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Paper)) {
                        EditField("Stretch ${index + 1}", "${day.id}:cooldown:$index", line, data)
                    }
                }
                item { Text("Edits are stored only on this device. Completed logs keep the values used in that session.", fontSize = 12.sp, color = Muted) }
            }
        } else {
            LazyColumn(Modifier.weight(1f), contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item { Text("MEAL FLOW", fontSize = 11.sp, letterSpacing = 1.2.sp, fontWeight = FontWeight.Bold, color = FuelOrange) }
                itemsIndexed(PlanData.meals, key = { _, meal -> meal.id }) { _, meal ->
                    Card(shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = FuelPalette[PlanData.meals.indexOf(meal) % FuelPalette.size])) {
                        Column(Modifier.padding(14.dp)) {
                            EditField("Time", "meal:${meal.id}:time", meal.time, data)
                            EditField("Meal", "meal:${meal.id}:name", meal.name, data)
                            EditField("Plan details", "meal:${meal.id}:detail", meal.detail, data)
                        }
                    }
                }
                item { Text("Daily nutrition figures remain displayed as written in your source plan.", fontSize = 12.sp, color = Muted) }
            }
        }
        Button(onClick = onDone, modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 10.dp).height(52.dp), shape = RoundedCornerShape(26.dp)) { Text("Save and close", fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun EditField(label: String, key: String, initial: String, data: LocalData) {
    var value by remember(key) { mutableStateOf(data.text(key, initial)) }
    Column(Modifier.fillMaxWidth().padding(top = 5.dp)) {
        Text(label, fontSize = 10.sp, letterSpacing = .7.sp, fontWeight = FontWeight.Bold, color = Muted)
        BasicTextField(
            value = value,
            onValueChange = { value = it; data.saveText(key, it) },
            modifier = Modifier.fillMaxWidth().padding(top = 3.dp, bottom = 7.dp),
            textStyle = TextStyle(color = Ink, fontSize = 14.sp, lineHeight = 19.sp),
            decorationBox = { inner ->
                Box {
                    if (value.isEmpty()) Text("Add details", fontSize = 14.sp, color = Muted.copy(alpha = .65f))
                    inner()
                }
            }
        )
        DividerLine()
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(onClick = onClick, shape = CircleShape, color = if (selected) Ink else Paper) {
        Text(label, Modifier.padding(horizontal = 19.dp, vertical = 10.dp), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (selected) Color.White else Ink)
    }
}

@Composable
private fun SettingsCard(title: String, content: @Composable () -> Unit) {
    Card(shape = RoundedCornerShape(23.dp), colors = CardDefaults.cardColors(containerColor = Paper)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink, modifier = Modifier.padding(bottom = 8.dp))
            content()
        }
    }
}

@Composable
private fun SettingsDialog(data: LocalData, onClose: () -> Unit) {
    var haptics by remember { mutableStateOf(data.hapticsEnabled()) }
    var sound by remember { mutableStateOf(data.timerSound()) }
    var spoken by remember { mutableStateOf(data.spokenCues()) }
    var awake by remember { mutableStateOf(data.screenAwake()) }
    var startDate by remember { mutableStateOf(data.startDate().toString()) }
    var deletePrompt by remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("BLOCK settings", fontWeight = FontWeight.Bold) },
        text = {
            Column(Modifier.heightIn(max = 520.dp).verticalScroll(rememberScrollState())) {
                Text("LOCAL PLAN", fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = Blue, modifier = Modifier.padding(top = 3.dp, bottom = 5.dp))
                OutlinedTextField(startDate, { startDate = it.take(10) }, label = { Text("Program start · YYYY-MM-DD") }, singleLine = true, shape = RoundedCornerShape(16.dp))
                Text("Week number follows this date. Weekdays remain fixed; missed sessions are not silently moved.", fontSize = 12.sp, lineHeight = 17.sp, color = Muted, modifier = Modifier.padding(top = 6.dp, bottom = 9.dp))
                Text("FEEDBACK", fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = Blue, modifier = Modifier.padding(top = 5.dp))
                ToggleRow("Haptic confirmations", haptics) { haptics = it; data.setHaptics(it) }
                ToggleRow("Rest timer tone", sound) { sound = it; data.setTimerSound(it) }
                ToggleRow("Speak exercise cues", spoken) { spoken = it; data.setSpokenCues(it) }
                ToggleRow("Keep display awake in workouts", awake) { awake = it; data.setScreenAwake(it) }
                Text("Uses the device’s installed speech voice. No account or network sync is used by BLOCK.", fontSize = 12.sp, lineHeight = 17.sp, color = Muted, modifier = Modifier.padding(vertical = 7.dp))
                TextButton(onClick = { deletePrompt = true }) { Text("Delete all BLOCK data", color = Color(0xFF9B2C2C)) }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                runCatching { LocalDate.parse(startDate) }.getOrNull()?.let(data::setStartDate)
                onClose()
            }) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onClose) { Text("Close") } }
    )
    if (deletePrompt) {
        AlertDialog(
            onDismissRequest = { deletePrompt = false },
            title = { Text("Delete local data?") },
            text = { Text("This removes plan edits, check-ins, body-weight entries, and workout logs from this device. This cannot be undone.") },
            confirmButton = { TextButton(onClick = { data.clearAll(); deletePrompt = false; onClose() }) { Text("Delete", color = Color(0xFF9B2C2C)) } },
            dismissButton = { TextButton(onClick = { deletePrompt = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().heightIn(min = 48.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f), fontSize = 14.sp, color = Ink)
        Checkbox(checked, onToggle)
    }
}

@Composable
private fun SectionHeading(title: String, trailing: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().padding(top = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, fontSize = 12.sp, letterSpacing = 1.3.sp, fontWeight = FontWeight.Bold, color = Ink)
        if (trailing.isNotBlank()) Text(trailing, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Muted, modifier = Modifier.clickable(onClick = onClick))
    }
}

@Composable
private fun CheckRow(title: String, subtitle: String, checked: Boolean, onCheck: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onCheck).padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        AnimatedCheck(checked, onCheck)
        Column(Modifier.weight(1f).padding(start = 10.dp)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Ink)
            Text(subtitle, fontSize = 11.sp, color = Muted)
        }
    }
}

@Composable
private fun AnimatedCheck(checked: Boolean, onClick: () -> Unit) {
    val tint by animateColorAsState(if (checked) Blue else Color.White, spring(stiffness = Spring.StiffnessMedium), label = "checkFill")
    val scale by animateFloatAsState(if (checked) 1f else .92f, spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "checkScale")
    Surface(onClick = onClick, modifier = Modifier.size(34.dp).scale(scale), shape = CircleShape, color = tint, border = if (checked) null else androidx.compose.foundation.BorderStroke(1.dp, Line)) {
        Box(contentAlignment = Alignment.Center) {
            if (checked) Text("✓", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
private fun RoundIconButton(glyph: String, label: String, onClick: () -> Unit, filled: Boolean) {
    Surface(onClick = onClick, modifier = Modifier.size(46.dp).semantics { contentDescription = label }, shape = CircleShape, color = if (filled) Ink else Paper, shadowElevation = if (filled) 0.dp else 1.dp) {
        Box(contentAlignment = Alignment.Center) { AppGlyph(glyph, Modifier.size(21.dp), if (filled) Color.White else Ink) }
    }
}

@Composable
private fun AppGlyph(name: String, modifier: Modifier = Modifier, tint: Color = Ink) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = size.minDimension * .10f, cap = StrokeCap.Round)
        when (name) {
            "Today" -> {
                drawRoundRect(tint, topLeft = androidx.compose.ui.geometry.Offset(w*.16f,h*.2f), size = androidx.compose.ui.geometry.Size(w*.68f,h*.64f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(w*.1f), style = stroke)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.2f,h*.4f), androidx.compose.ui.geometry.Offset(w*.8f,h*.4f), strokeWidth = stroke.width)
                drawCircle(tint, w*.055f, androidx.compose.ui.geometry.Offset(w*.36f,h*.59f)); drawCircle(tint, w*.055f, androidx.compose.ui.geometry.Offset(w*.62f,h*.59f))
            }
            "BLOCK" -> {
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.2f,h*.5f), androidx.compose.ui.geometry.Offset(w*.8f,h*.5f), strokeWidth = stroke.width)
                drawRoundRect(tint, androidx.compose.ui.geometry.Offset(w*.08f,h*.3f), androidx.compose.ui.geometry.Size(w*.17f,h*.4f), androidx.compose.ui.geometry.CornerRadius(w*.04f))
                drawRoundRect(tint, androidx.compose.ui.geometry.Offset(w*.75f,h*.3f), androidx.compose.ui.geometry.Size(w*.17f,h*.4f), androidx.compose.ui.geometry.CornerRadius(w*.04f))
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.31f,h*.32f), androidx.compose.ui.geometry.Offset(w*.31f,h*.68f), strokeWidth = stroke.width)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.69f,h*.32f), androidx.compose.ui.geometry.Offset(w*.69f,h*.68f), strokeWidth = stroke.width)
            }
            "FUEL" -> {
                val path = Path().apply {
                    moveTo(w*.5f,h*.1f); cubicTo(w*.42f,h*.32f,w*.2f,h*.45f,w*.25f,h*.66f)
                    cubicTo(w*.29f,h*.87f,w*.7f,h*.92f,w*.78f,h*.68f); cubicTo(w*.87f,h*.43f,w*.61f,h*.29f,w*.5f,h*.1f); close()
                }
                drawPath(path,tint, style=stroke)
                drawCircle(tint,w*.07f, androidx.compose.ui.geometry.Offset(w*.51f,h*.63f))
            }
            "Progress" -> {
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.18f,h*.82f), androidx.compose.ui.geometry.Offset(w*.82f,h*.82f), strokeWidth = stroke.width)
                drawRoundRect(tint, androidx.compose.ui.geometry.Offset(w*.22f,h*.55f), androidx.compose.ui.geometry.Size(w*.12f,h*.23f), androidx.compose.ui.geometry.CornerRadius(w*.04f))
                drawRoundRect(tint, androidx.compose.ui.geometry.Offset(w*.44f,h*.34f), androidx.compose.ui.geometry.Size(w*.12f,h*.44f), androidx.compose.ui.geometry.CornerRadius(w*.04f))
                drawRoundRect(tint, androidx.compose.ui.geometry.Offset(w*.66f,h*.16f), androidx.compose.ui.geometry.Size(w*.12f,h*.62f), androidx.compose.ui.geometry.CornerRadius(w*.04f))
            }
            "arrow" -> {
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.15f,h*.5f), androidx.compose.ui.geometry.Offset(w*.82f,h*.5f), strokeWidth = stroke.width)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.56f,h*.24f), androidx.compose.ui.geometry.Offset(w*.82f,h*.5f), strokeWidth = stroke.width)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.56f,h*.76f), androidx.compose.ui.geometry.Offset(w*.82f,h*.5f), strokeWidth = stroke.width)
            }
            "back" -> {
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.82f,h*.5f), androidx.compose.ui.geometry.Offset(w*.18f,h*.5f), strokeWidth = stroke.width)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.18f,h*.5f), androidx.compose.ui.geometry.Offset(w*.46f,h*.22f), strokeWidth = stroke.width)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.18f,h*.5f), androidx.compose.ui.geometry.Offset(w*.46f,h*.78f), strokeWidth = stroke.width)
            }
            "timer" -> {
                drawCircle(tint, w*.34f, androidx.compose.ui.geometry.Offset(w*.5f,h*.55f), style=stroke)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.5f,h*.09f), androidx.compose.ui.geometry.Offset(w*.5f,h*.19f), strokeWidth=stroke.width)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.5f,h*.55f), androidx.compose.ui.geometry.Offset(w*.5f,h*.35f), strokeWidth=stroke.width)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.5f,h*.55f), androidx.compose.ui.geometry.Offset(w*.66f,h*.63f), strokeWidth=stroke.width)
            }
            "sound" -> {
                val speaker = Path().apply {
                    moveTo(w*.16f,h*.39f); lineTo(w*.36f,h*.39f); lineTo(w*.58f,h*.2f)
                    lineTo(w*.58f,h*.8f); lineTo(w*.36f,h*.61f); lineTo(w*.16f,h*.61f); close()
                }
                drawPath(speaker, tint)
                val wave = Path().apply {
                    moveTo(w*.69f,h*.36f); cubicTo(w*.82f,h*.45f,w*.82f,h*.55f,w*.69f,h*.64f)
                    moveTo(w*.77f,h*.23f); cubicTo(w*.98f,h*.38f,w*.98f,h*.62f,w*.77f,h*.77f)
                }
                drawPath(wave, tint, style = stroke)
            }
            "edit" -> {
                rotate(-40f, androidx.compose.ui.geometry.Offset(w*.5f,h*.5f)) {
                    drawRoundRect(tint, androidx.compose.ui.geometry.Offset(w*.42f,h*.16f), androidx.compose.ui.geometry.Size(w*.16f,h*.62f), androidx.compose.ui.geometry.CornerRadius(w*.06f))
                    drawLine(tint, androidx.compose.ui.geometry.Offset(w*.42f,h*.83f), androidx.compose.ui.geometry.Offset(w*.58f,h*.83f), strokeWidth=stroke.width)
                }
            }
            "settings" -> {
                drawCircle(tint, w*.28f, androidx.compose.ui.geometry.Offset(w*.5f,h*.5f), style=stroke)
                drawCircle(tint, w*.08f, androidx.compose.ui.geometry.Offset(w*.5f,h*.5f))
                for (i in 0..7) rotate(i*45f, androidx.compose.ui.geometry.Offset(w*.5f,h*.5f)) {
                    drawLine(tint, androidx.compose.ui.geometry.Offset(w*.5f,h*.08f), androidx.compose.ui.geometry.Offset(w*.5f,h*.19f), strokeWidth=stroke.width)
                }
            }
            "check" -> {
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.2f,h*.52f), androidx.compose.ui.geometry.Offset(w*.42f,h*.73f), strokeWidth = stroke.width)
                drawLine(tint, androidx.compose.ui.geometry.Offset(w*.42f,h*.73f), androidx.compose.ui.geometry.Offset(w*.82f,h*.28f), strokeWidth = stroke.width)
            }
        }
    }
}

@Composable
private fun RingProgress(progress: Float, center: String, modifier: Modifier = Modifier, color: Color = Blue) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize().padding(4.dp)) {
            drawArc(color.copy(alpha = .2f), -90f, 360f, false, style = Stroke(size.minDimension * .1f, cap = StrokeCap.Round))
            drawArc(color, -90f, 360f * progress.coerceIn(0f,1f), false, style = Stroke(size.minDimension * .1f, cap = StrokeCap.Round))
        }
        Text(center, fontSize = if (center.length > 1) 17.sp else 22.sp, fontWeight = FontWeight.Bold, color = if (color == Ink) Ink else Blue)
    }
}

@Composable
private fun StatTile(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.clip(RoundedCornerShape(19.dp)).background(Paper).padding(horizontal = 13.dp, vertical = 11.dp)) {
        Text(label, fontSize = 9.sp, letterSpacing = 1.sp, fontWeight = FontWeight.Bold, color = Muted)
        Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Ink, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun NoteCard(title: String, description: String, tint: Color) {
    Card(shape = RoundedCornerShape(23.dp), colors = CardDefaults.cardColors(containerColor = tint)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(description, fontSize = 13.sp, lineHeight = 19.sp, color = Ink.copy(alpha = .74f), modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun ColorCard(title: String, description: String, tint: Color) {
    Card(shape = RoundedCornerShape(25.dp), colors = CardDefaults.cardColors(containerColor = tint)) {
        Column(Modifier.fillMaxWidth().padding(18.dp)) {
            Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink)
            Text(description, fontSize = 13.sp, lineHeight = 19.sp, color = Ink.copy(alpha = .76f), modifier = Modifier.padding(top = 5.dp))
        }
    }
}

@Composable
private fun DividerLine() { Box(Modifier.fillMaxWidth().height(1.dp).background(Line)) }

