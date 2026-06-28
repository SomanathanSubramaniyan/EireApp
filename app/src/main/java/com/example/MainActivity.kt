package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        MainScreen()
      }
    }
  }
}

// Data models
data class IrishPhrase(
  val irish: String,
  val phonetics: String,
  val english: String,
  val usageContext: String
)

data class SlangTerm(
  val term: String,
  val standardEnglish: String,
  val usage: String,
  val origin: String
)

data class TriviaQuestion(
  val question: String,
  val options: List<String>,
  val correctAnswerIndex: Int,
  val funFact: String
)

data class CityWeather(
  val name: String,
  val temp: String,
  val condition: String,
  val hasRain: Boolean,
  val report: String
)

@Composable
fun MainScreen() {
  var selectedTab by remember { mutableStateOf(0) }
  val context = LocalContext.current

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    bottomBar = {
      NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("bottom_nav_bar")
      ) {
        NavigationBarItem(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          label = { Text("Focal", fontWeight = FontWeight.SemiBold) },
          icon = {
            Icon(
              imageVector = Icons.Default.Favorite,
              contentDescription = "Focal an Lae"
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
          ),
          modifier = Modifier.testTag("tab_focal")
        )
        NavigationBarItem(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          label = { Text("Rain Alert", fontWeight = FontWeight.SemiBold) },
          icon = {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = "Is It Raining?"
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
          ),
          modifier = Modifier.testTag("tab_rain")
        )
        NavigationBarItem(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          label = { Text("Slang / Craic", fontWeight = FontWeight.SemiBold) },
          icon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Slang Dictionary"
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
          ),
          modifier = Modifier.testTag("tab_slang")
        )
        NavigationBarItem(
          selected = selectedTab == 3,
          onClick = { selectedTab = 3 },
          label = { Text("Quiz", fontWeight = FontWeight.SemiBold) },
          icon = {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = "Trivia Quiz"
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
          ),
          modifier = Modifier.testTag("tab_quiz")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(innerPadding)
    ) {
      when (selectedTab) {
        0 -> FocalTab()
        1 -> RainTab()
        2 -> SlangTab()
        3 -> QuizTab()
      }
    }
  }
}

@Composable
fun FocalTab() {
  val phrases = remember {
    listOf(
      IrishPhrase("Dia dhuit", "Dee-ah gwit", "Hello", "Literal meaning: 'God be with you'. Used as a standard daytime greeting."),
      IrishPhrase("Conas atá tú?", "Kun-us ah-taw too?", "How are you?", "A friendly inquiry about someone's day."),
      IrishPhrase("Go raibh maith agat", "Guh rev mah ah-gut", "Thank you", "Literal meaning: 'May good be at you'. The standard way to express gratitude."),
      IrishPhrase("Slán go fóill", "Slawn guh fole", "Goodbye for now", "A perfect friendly farewell when parting ways."),
      IrishPhrase("Gabh mo leithscéal", "Gow muh leh-shkayl", "Excuse me / Sorry", "Used to grab someone's attention or apologize."),
      IrishPhrase("Is maith liom tae", "Is mah lyum tay", "I like tea", "A vital survival sentence, since tea (usually Barry's or Lyons) is fuel in Ireland!"),
      IrishPhrase("Tá sé go hálainn", "Taw shay guh haw-lyinn", "It is beautiful", "Perfect for describing the spectacular rolling scenery of Ireland."),
      IrishPhrase("Sláinte", "Slawn-cha", "Cheers / Good health", "Said when clinking glasses of Guinness or cider in a cozy Irish pub.")
    )
  }

  var phraseIndex by remember { mutableStateOf(0) }
  val currentPhrase = phrases[phraseIndex]
  var showPronounceDialog by remember { mutableStateOf(false) }

  val context = LocalContext.current
  val greeting = remember {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    when {
      hour < 12 -> "Maidin mhaith! (Good morning)"
      hour < 18 -> "Dia dhuit! (Hello)"
      else -> "Tráthnóna maith! (Good evening)"
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    // Hero Banner Card
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
    ) {
      Image(
        painter = painterResource(id = R.drawable.img_ireland_hero_1782626852502),
        contentDescription = "Irish rolling green hills with cottage and rainbow",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )
      // Visual Overlay Dark Gradient for legibility
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
            )
          )
      )
      // Text over banner
      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(16.dp)
      ) {
        Text(
          text = "ÉirePocket",
          color = Color.White,
          fontSize = 32.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Text(
          text = "Your Irish Companion Guide • Fáilte go hÉirinn!",
          color = MaterialTheme.colorScheme.secondary,
          fontSize = 14.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }

    Column(modifier = Modifier.padding(16.dp)) {
      // Welcome Callout
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        ),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Welcome info",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
          )
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = greeting,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary,
              fontSize = 16.sp
            )
            Text(
              text = "Learn daily Irish (Gaeilge) phrases, test your slang, verify rainy weather, and master Irish trivia!",
              fontSize = 13.sp,
              color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
          }
        }
      }

      // Title header
      Text(
        text = "Focal an Lae (Phrase of the Day)",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 12.dp)
      )

      // Main Interactive Phrase Card
      ElevatedCard(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp)
          .testTag("phrase_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Gaeilge Text
          Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
          ) {
            Text(
              text = " Gaeilge (Irish) ",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          Text(
            text = currentPhrase.irish,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Phonetics Box
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
              .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
              .clickable { showPronounceDialog = true }
              .padding(12.dp),
            contentAlignment = Alignment.Center
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Pronounce",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Pronounce: \"${currentPhrase.phonetics}\"",
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // English Translation
          Text(
            text = "English Meaning",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
          )
          Text(
            text = currentPhrase.english,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Usage Context
          Text(
            text = currentPhrase.usageContext,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
          )
        }
      }

      // Actions Layout
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Button(
          onClick = {
            phraseIndex = (phraseIndex + 1) % phrases.size
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("next_phrase_btn")
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Cycle phrase"
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("Cycle Phrase (Bain Triail As)", fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Fun Culture Tip
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Did you know? ☘️",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 14.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Irish (Gaeilge) is a Celtic language and is the first official language of Ireland. Even though everyone speaks English, you'll see both languages printed on every street sign and hear Irish spoken in Gaeltacht areas along the rugged west coast!",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
          )
        }
      }
    }
  }

  // Simulated Speech Dialog
  if (showPronounceDialog) {
    Dialog(onDismissRequest = { showPronounceDialog = false }) {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "Irish Pronunciation Guide",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = currentPhrase.irish,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Pronounce as:",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
          )
          Text(
            text = "\"${currentPhrase.phonetics}\"",
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = "Note: Irish is highly phonetic once you get used to the unique letter combinations. Broad vowels (a, o, u) and slender vowels (e, i) shape how surrounding consonants are pronounced!",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
          )
          Spacer(modifier = Modifier.height(24.dp))
          Button(
            onClick = { showPronounceDialog = false },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text("Go raibh maith agat (Thanks!)", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@Composable
fun RainTab() {
  val cities = remember {
    listOf(
      CityWeather("Dublin", "14°C", "Passing showers & windy", true, "A grand soft day, thank God. Bring a light coat but expect a sudden horizontal lashing of rain in exactly five minutes. Keep yer eyes peeled!"),
      CityWeather("Galway", "12°C", "Torrential downpour & wild winds", true, "Lashing out of the heavens. A complete wash-out. The Atlantic wind would blow the head clean off ya. Run into the nearest pub immediately and grab a creamy pint!"),
      CityWeather("Cork", "15°C", "Misty spit & grey skies", true, "Misty and standard grey. Wait, is that a dry spell? Ah no, false alarm, it's starting to spit again. Keep your brolly (umbrella) glued to your hand."),
      CityWeather("Belfast", "11°C", "Pure Baltic overcast", true, "It is absolutely Baltic. Put on your big coat and your woolen hat, yer man. Stop stalling and get some hot Irish stew inside ya."),
      CityWeather("Kerry", "13°C", "Misty mountain fog", true, "A soft damp fog is settled over the lakes of Killarney. The mountains have vanished entirely! Ideal conditions for a hot whiskey by a blazing turf fire."),
      CityWeather("Donegal", "10°C", "Fierce gale & heavy drizzle", true, "Fierce windy altogether! Excellent for blowing away the cobwebs, assuming your umbrella doesn't fold inside out and fly off into the Atlantic.")
    )
  }

  var selectedCityIndex by remember { mutableStateOf(0) }
  val currentCity = cities[selectedCityIndex]

  var isScanning by remember { mutableStateOf(false) }
  var scanProgress by remember { mutableStateOf(0f) }
  var scanMessage by remember { mutableStateOf("") }
  val coroutineScope = rememberCoroutineScope()

  val scanningSteps = remember {
    listOf(
      "Brewing Barry's tea...",
      "Peeking outside through the blinds...",
      "Checking if the cows are lying down...",
      "Consulting the local pub elders...",
      "Analyzing dampness levels of the grass...",
      "Verifying if sheep are huddling under trees..."
    )
  }

  fun triggerScanning() {
    isScanning = true
    scanProgress = 0f
    coroutineScope.launch {
      for (i in 1..10) {
        delay(300)
        scanProgress = i / 10f
        scanMessage = scanningSteps[(i - 1) % scanningSteps.size]
      }
      isScanning = false
    }
  }

  // Trigger scanning once when city changes
  LaunchedEffect(selectedCityIndex) {
    triggerScanning()
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    Text(
      text = "Is It Raining in Ireland? 🌧️",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary,
      modifier = Modifier.padding(bottom = 6.dp)
    )
    Text(
      text = "The ultimate question in Ireland. Select an Irish county or city to obtain a humorous and damp real-time verdict.",
      fontSize = 13.sp,
      color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
      modifier = Modifier.padding(bottom = 16.dp)
    )

    // Horizontal list of city selectors
    Text(
      text = "Select Location:",
      fontWeight = FontWeight.Bold,
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.secondary,
      modifier = Modifier.padding(bottom = 8.dp)
    )

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Column(
        modifier = Modifier.fillMaxWidth()
      ) {
        // Grid/Row-based selectors for counties
        val rows = cities.chunked(3)
        rows.forEach { rowCities ->
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            rowCities.forEach { city ->
              val index = cities.indexOf(city)
              val isSelected = index == selectedCityIndex
              Box(
                modifier = Modifier
                  .weight(1f)
                  .padding(vertical = 4.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                  )
                  .border(
                    width = 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(8.dp)
                  )
                  .clickable { selectedCityIndex = index }
                  .padding(vertical = 12.dp, horizontal = 4.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = city.name,
                  color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                  fontWeight = FontWeight.Bold,
                  fontSize = 14.sp,
                  textAlign = TextAlign.Center
                )
              }
            }
          }
        }
      }
    }

    // Interactive scan card or weather card
    ElevatedCard(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.elevatedCardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        if (isScanning) {
          // Radar scanning state
          Text(
            text = "IRISH CLOUD RADAR SCANNING",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.5.sp
          )
          Spacer(modifier = Modifier.height(16.dp))
          LinearProgressIndicator(
            progress = { scanProgress },
            modifier = Modifier
              .fillMaxWidth()
              .height(10.dp)
              .clip(RoundedCornerShape(5.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = scanMessage,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
          )
        } else {
          // Finished scanning: Weather results
          Text(
            text = "Weather Report for ${currentCity.name}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
          )
          Spacer(modifier = Modifier.height(8.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "🌧️",
              fontSize = 44.sp,
              modifier = Modifier.padding(end = 12.dp)
            )
            Column {
              Text(
                text = currentCity.temp,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = currentCity.condition,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Verdict Banner
          Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(
              modifier = Modifier.padding(16.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "IS IT RAINING? VERDICT:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.sp
              )
              Text(
                text = "YES (LITERALLY ALWAYS)",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = currentCity.report,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            lineHeight = 22.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
          )
        }
      }
    }

    // Refresh weather button
    Button(
      onClick = { triggerScanning() },
      modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .testTag("refresh_weather_btn"),
      colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary
      ),
      shape = RoundedCornerShape(8.dp),
      enabled = !isScanning
    ) {
      Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = "Scan clouds"
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text("Scan Irish Clouds Live", fontWeight = FontWeight.Bold)
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Culture Note: Soft Day
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
      ),
      shape = RoundedCornerShape(12.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Irish Weather Lexicon 💡",
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary,
          fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "In Ireland, rain isn't just rain. If someone says it's a 'grand soft day', they mean it's a very light mist or drizzle that doesn't feel like much but will soak you to the bone within ten minutes. If it's 'lashing', it means torrential, horizontal, Atlantic rain is active!",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
      }
    }
  }
}

@Composable
fun SlangTab() {
  val slangTerms = remember {
    listOf(
      SlangTerm("Craic", "Fun, gossip, news, or a great time.", "E.g., 'What's the craic, lads?' or 'We had mighty craic last night!'", "Originates from Middle English 'crak' but was adapted into Irish Gaelic as 'craic' and became the cornerstone of Irish social life."),
      SlangTerm("Grand", "Fine, okay, or brilliant.", "E.g., 'How are you?' 'Ah, I'm grand!' or 'That's grand, don't worry about it.'", "The ultimate versatile Irish response. It can mean anything from 'I'm perfectly fine' to 'I am barely surviving but keeping up appearances' depending on the tone!"),
      SlangTerm("Sound", "Decent, reliable, friendly, or solid.", "E.g., 'Yer man Paddy is sound' or 'Thanks for the cup of tea.' 'Sound!'.", "Commonly used as an adjective of praise for a good person, or as a single word to mean 'Thank you' or 'Understood'."),
      SlangTerm("Deadly", "Fantastic, awesome, or brilliant.", "E.g., 'That concert last night was absolutely deadly!'", "Irish slang that completely reverses a dark word into a term of massive, enthusiastic appreciation!"),
      SlangTerm("Your Man / Your Wan", "That guy / That girl.", "E.g., 'Your man over there is acting the maggot' or 'Ask your wan behind the counter.'", "Used as a conversational placeholder for any male or female, regardless of whether you know their name!"),
      SlangTerm("Acting the maggot", "Behaving foolishly, messing around, or causing minor trouble.", "E.g., 'Stop acting the maggot and eat your dinner!'", "Usually said to children, friends, or mischievous colleagues who are behaving silly or playing pranks."),
      SlangTerm("Baltic", "Extremely cold weather.", "E.g., 'Put on your thick coat, it is absolutely Baltic out there!'", "Refers to the freezing Baltic Sea, indicating the temperature is icy and bitterly cold."),
      SlangTerm("Gas", "Hilarious, funny, or highly amusing.", "E.g., 'He told us a story about his sheep, it was gas!'", "Derives from 'funny gas'. Used when describing someone or a situation that is highly entertaining and funny.")
    )
  }

  var searchQuery by remember { mutableStateOf("") }
  val filteredTerms = slangTerms.filter {
    it.term.contains(searchQuery, ignoreCase = true) ||
        it.standardEnglish.contains(searchQuery, ignoreCase = true)
  }

  var expandedIndex by remember { mutableStateOf(-1) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Text(
      text = "Craic Slang Dictionary ☘️",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary,
      modifier = Modifier.padding(bottom = 6.dp)
    )
    Text(
      text = "Master Irish slang so you can chat with the locals like an absolute expert. Tap a term to expand details.",
      fontSize = 13.sp,
      color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
      modifier = Modifier.padding(bottom = 16.dp)
    )

    // Search Bar
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)
        .testTag("slang_search_input"),
      placeholder = { Text("Search slang or meaning...") },
      leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon") },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(Icons.Default.Clear, contentDescription = "Clear search")
          }
        }
      },
      singleLine = true,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline
      ),
      shape = RoundedCornerShape(12.dp)
    )

    // Slang list
    if (filteredTerms.isEmpty()) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = "No slang found! 📭",
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.secondary,
          fontSize = 18.sp
        )
        Text(
          text = "Ah, yer acting the maggot! Try searching another word like 'Craic' or 'Grand'.",
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
          textAlign = TextAlign.Center,
          modifier = Modifier.padding(16.dp)
        )
      }
    } else {
      Column(
        modifier = Modifier
          .weight(1f)
          .verticalScroll(rememberScrollState())
      ) {
        filteredTerms.forEachIndexed { index, slang ->
          val isExpanded = expandedIndex == index
          OutlinedCard(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp)
              .clickable {
                expandedIndex = if (isExpanded) -1 else index
              }
              .animateContentSize()
              .testTag("slang_item_${slang.term.lowercase()}"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.outlinedCardColors(
              containerColor = if (isExpanded) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
              else MaterialTheme.colorScheme.surface
            ),
            border = borderStrokeForSlang(isExpanded)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = slang.term,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                  )
                  Text(
                    text = slang.standardEnglish,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    overflow = TextOverflow.Ellipsis
                  )
                }
                Icon(
                  imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                  contentDescription = "Expand",
                  tint = MaterialTheme.colorScheme.primary
                )
              }

              if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                // Usage Section
                Surface(
                  color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                  shape = RoundedCornerShape(6.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                      text = "How to use it:",
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                      text = slang.usage,
                      fontSize = 14.sp,
                      fontWeight = FontWeight.Medium,
                      fontStyle = FontStyle.Italic,
                      color = MaterialTheme.colorScheme.onSurface
                    )
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Origin Section
                Text(
                  text = "Origin & Background:",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.primary
                )
                Text(
                  text = slang.origin,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun borderStrokeForSlang(isExpanded: Boolean): androidx.compose.foundation.BorderStroke {
  return androidx.compose.foundation.BorderStroke(
    width = if (isExpanded) 1.5.dp else 1.dp,
    color = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
  )
}

@Composable
fun QuizTab() {
  val quizQuestions = remember {
    listOf(
      TriviaQuestion(
        "What legendary creature is said to guard a pot of gold at the end of a rainbow?",
        listOf("Leprechaun", "Banshee", "Selkie", "Púca"),
        0,
        "Leprechauns are trickster fae of Irish mythology who make shoes, hide gold, and are famously difficult to outsmart!"
      ),
      TriviaQuestion(
        "Which famous Irish festival marks the pagan Celtic origin of modern Halloween?",
        listOf("Imbolc", "Samhain", "Bealtaine", "Lughnasadh"),
        1,
        "Samhain (pronounced Sow-in) marked the end of harvest and the time when the boundary between our world and the spirit realm grew thin!"
      ),
      TriviaQuestion(
        "What is the official national symbol of Ireland, making it the only country in the world represented by a musical instrument?",
        listOf("The Shamrock", "The High Cross", "The Celtic Harp", "The Round Tower"),
        2,
        "The Cláirseach (Celtic Harp) is officially on passports and state seals. In fact, Guinness registered the harp trademark first, so the state has to face its harp in the opposite direction!"
      ),
      TriviaQuestion(
        "According to popular folklore, which patron saint banished wild snakes from Ireland?",
        listOf("Saint Patrick", "Saint Brigid", "Saint Columba", "Saint Kevin"),
        0,
        "While legend credits Saint Patrick with driving the snakes out, scientists believe Ireland has never had wild snakes since the post-ice age era due to surrounding cold oceans!"
      ),
      TriviaQuestion(
        "What is the longest river in Ireland, flowing over 360 kilometers?",
        listOf("The River Liffey", "The River Shannon", "The River Lee", "The River Corrib"),
        1,
        "The River Shannon is the longest river in the British Isles, carving its way from County Cavan down into the Atlantic Ocean near Limerick."
      )
    )
  }

  var currentQuestionIndex by remember { mutableStateOf(0) }
  var selectedAnswerIndex by remember { mutableStateOf(-1) }
  var isAnswerSubmitted by remember { mutableStateOf(false) }
  var score by remember { mutableStateOf(0) }
  var isQuizFinished by remember { mutableStateOf(false) }

  val currentQuestion = quizQuestions[currentQuestionIndex]

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
      .verticalScroll(rememberScrollState())
  ) {
    Text(
      text = "Ireland Trivia Challenge 🧠",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary,
      modifier = Modifier.padding(bottom = 6.dp)
    )

    if (!isQuizFinished) {
      Text(
        text = "Test your knowledge of Irish history, mythology, culture, and nature. Answer 5 questions to claim your Irish title!",
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        modifier = Modifier.padding(bottom = 16.dp)
      )

      // Progress Tracker
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Question ${currentQuestionIndex + 1} of ${quizQuestions.size}",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.primary
        )
        Text(
          text = "Score: $score",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.secondary
        )
      }

      // Linear progress bar
      LinearProgressIndicator(
        progress = { (currentQuestionIndex + 1) / quizQuestions.size.toFloat() },
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 20.dp)
          .clip(RoundedCornerShape(4.dp)),
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.primaryContainer
      )

      // Question Card
      ElevatedCard(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Text(
            text = currentQuestion.question,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 24.sp
          )
        }
      }

      // Option Choices
      Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        currentQuestion.options.forEachIndexed { index, option ->
          val isSelected = selectedAnswerIndex == index
          val optionColor = when {
            isAnswerSubmitted && index == currentQuestion.correctAnswerIndex -> Color(0xFF2E7D32) // Green for correct
            isAnswerSubmitted && isSelected && index != currentQuestion.correctAnswerIndex -> Color(0xFFC62828) // Red for selected wrong
            isSelected -> MaterialTheme.colorScheme.primaryContainer
            else -> MaterialTheme.colorScheme.surface
          }

          val textColor = when {
            isAnswerSubmitted && index == currentQuestion.correctAnswerIndex -> Color.White
            isAnswerSubmitted && isSelected && index != currentQuestion.correctAnswerIndex -> Color.White
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurface
          }

          val borderStrokeColor = when {
            isAnswerSubmitted && index == currentQuestion.correctAnswerIndex -> Color(0xFF2E7D32)
            isAnswerSubmitted && isSelected && index != currentQuestion.correctAnswerIndex -> Color(0xFFC62828)
            isSelected -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.outlineVariant
          }

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(optionColor)
              .border(
                width = if (isSelected || isAnswerSubmitted) 2.dp else 1.dp,
                color = borderStrokeColor,
                shape = RoundedCornerShape(12.dp)
              )
              .clickable(enabled = !isAnswerSubmitted) {
                selectedAnswerIndex = index
              }
              .padding(16.dp)
              .testTag("quiz_option_$index"),
            contentAlignment = Alignment.CenterStart
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = option,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                modifier = Modifier.weight(1f)
              )
              if (isAnswerSubmitted) {
                if (index == currentQuestion.correctAnswerIndex) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Correct",
                    tint = Color.White
                  )
                } else if (isSelected) {
                  Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Incorrect",
                    tint = Color.White
                  )
                }
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Action Row (Submit / Next)
      if (!isAnswerSubmitted) {
        Button(
          onClick = {
            if (selectedAnswerIndex != -1) {
              isAnswerSubmitted = true
              if (selectedAnswerIndex == currentQuestion.correctAnswerIndex) {
                score++
              }
            }
          },
          enabled = selectedAnswerIndex != -1,
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("submit_answer_btn"),
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text("Submit Answer", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
      } else {
        Column(modifier = Modifier.fillMaxWidth()) {
          // Fun Fact Card
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
              containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(10.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Text(
                text = "Irish Wisdom Fact 💡",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = currentQuestion.funFact,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
              )
            }
          }

          Button(
            onClick = {
              if (currentQuestionIndex + 1 < quizQuestions.size) {
                currentQuestionIndex++
                selectedAnswerIndex = -1
                isAnswerSubmitted = false
              } else {
                isQuizFinished = true
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("next_question_btn"),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp)
          ) {
            val buttonText = if (currentQuestionIndex + 1 < quizQuestions.size) "Next Question" else "See Results"
            Text(buttonText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
          }
        }
      }
    } else {
      // Score Dashboard results state
      val rankTitle = when (score) {
        5 -> "Full-blown Irish Legend 👑"
        3, 4 -> "Honorary Irish Citizen ☘️"
        1, 2 -> "Tourist in Training 🗺️"
        else -> "Lost at Sea 🌊"
      }

      val rankDesc = when (score) {
        5 -> "You're practically made of Barry's tea and shamrocks! You know your history, mythology, and geography like an absolute chieftain. Go pour yourself a nice cup!"
        3, 4 -> "Not too shabby, you know your craic! A solid, respectable knowledge of Ireland. An honorary passport awaits you!"
        1, 2 -> "A decent effort, but you've still got a bit of traveling to do. Time to book a flight over and experience a grand soft day firsthand!"
        else -> "Ah, stop acting the maggot! Did you guess random buttons? Tap retry and actually read the wise lore cards!"
      }

      ElevatedCard(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "CHALLENGE COMPLETED",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 2.sp
          )
          Spacer(modifier = Modifier.height(16.dp))

          // Large Circular Score Indicator
          Box(
            modifier = Modifier
              .size(120.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "$score / ${quizQuestions.size}",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = "Score",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(24.dp))

          // Rank Title
          Text(
            text = rankTitle,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Rank Description
          Text(
            text = rankDesc,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 12.dp)
          )
        }
      }

      // Retry Button
      Button(
        onClick = {
          currentQuestionIndex = 0
          selectedAnswerIndex = -1
          isAnswerSubmitted = false
          score = 0
          isQuizFinished = false
        },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("restart_quiz_btn"),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Refresh,
          contentDescription = "Restart"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Restart Quiz", fontWeight = FontWeight.Bold, fontSize = 16.sp)
      }
    }
  }
}

// Keeping the Greeting helper intact for legacy GreetingScreenshotTest.kt compiling!
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
  MyApplicationTheme {
    MainScreen()
  }
}
