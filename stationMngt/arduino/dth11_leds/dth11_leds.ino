
#define MAX_TRACKS 2
#define RED     0
#define YELLOW  1
#define GREEN   2

typedef struct {
  int redPin;
  int yellowPin;
  int greenPin;
  bool blinking;
} TrackLights;

TrackLights tracks[MAX_TRACKS];

float mockTemp = 22.5;  
float mockHum  = 55.0; 

// Blinking control
unsigned long previousMillis = 0;
const unsigned long blinkInterval = 500;
bool blinkState = false;

// Serial input
String inputString = "";
bool commandReady = false;

/* ===================== SETUP ===================== */
void setup() {
  Serial.begin(9600);

  // -------- TRACK 1 --------
  tracks[0].redPin    = 10; // vermelho
  tracks[0].yellowPin = 12; // amarelo
  tracks[0].greenPin  = 14; // verde
  tracks[0].blinking  = false;

  // -------- TRACK 2 --------
  tracks[1].redPin    = 21;
  tracks[1].yellowPin = 19;
  tracks[1].greenPin  = 17;
  tracks[1].blinking  = false;

  // Inicialmente: todos os LEDs desligados (OUTPUT + LOW)
  for (int i = 0; i < MAX_TRACKS; i++) {
    pinMode(tracks[i].redPin, OUTPUT);
    pinMode(tracks[i].yellowPin, OUTPUT);
    pinMode(tracks[i].greenPin, OUTPUT);

    digitalWrite(tracks[i].redPin, LOW);
    digitalWrite(tracks[i].yellowPin, LOW);
    digitalWrite(tracks[i].greenPin, LOW);
  }

  inputString.reserve(32);
  Serial.println("LightSigns READY");
}

/* ===================== LOOP ===================== */
void loop() {
  if (commandReady) {
    processCommand(inputString);
    inputString = "";
    commandReady = false;
  }

  handleBlinking();
}

/* ===================== SERIAL EVENT ===================== */
void serialEvent() {
  while (Serial.available()) {
    char c = (char)Serial.read();
    if (c == '\n') {
      commandReady = true;
    } else {
      inputString += c;
    }
  }
}

/* ===================== COMMAND PROCESSING ===================== */
void processCommand(String cmd) {
  cmd.trim();
  cmd.toUpperCase();

  // ---- USAC13: Sensor ----
  if (cmd == "GTH") {
    handleSensorCommand();
    return;
  }

  // ---- USAC14: Luzes (com vírgula) ----
  int comma = cmd.indexOf(',');
  if (comma == -1) {
    Serial.println("ERROR");
    return;
  }

  String command = cmd.substring(0, comma);
  int trackNum = cmd.substring(comma + 1).toInt();

  if (trackNum < 1 || trackNum > MAX_TRACKS) {
    Serial.println("ERROR");
    return;
  }

  int idx = trackNum - 1;

  if (command == "RE") {
    setTrackLight(idx, RED);
    Serial.println("OK");
  }
  else if (command == "YE") {
    setTrackLight(idx, YELLOW);
    Serial.println("OK");
  }
  else if (command == "GE") {
    setTrackLight(idx, GREEN);
    Serial.println("OK");
  }
  else if (command == "RB") {
    setTrackBlinking(idx);
    Serial.println("OK");
  }
  else {
    Serial.println("ERROR");
  }
}

/* ===================== LIGHT CONTROL ===================== */

void turnOffAllLights(int idx) {
  digitalWrite(tracks[idx].redPin, LOW);
  digitalWrite(tracks[idx].yellowPin, LOW);
  digitalWrite(tracks[idx].greenPin, LOW);
}

void setTrackLight(int idx, int color) {
  turnOffAllLights(idx);
  tracks[idx].blinking = false;

  int pin = -1;
  if (color == RED)    pin = tracks[idx].redPin;
  if (color == YELLOW) pin = tracks[idx].yellowPin;
  if (color == GREEN)  pin = tracks[idx].greenPin;

  digitalWrite(pin, HIGH);
}

void setTrackBlinking(int idx) {
  turnOffAllLights(idx);
  tracks[idx].blinking = true;
}

/* ===================== BLINKING ===================== */
void handleBlinking() {
  unsigned long now = millis();

  if (now - previousMillis >= blinkInterval) {
    previousMillis = now;
    blinkState = !blinkState;

    for (int i = 0; i < MAX_TRACKS; i++) {
      if (tracks[i].blinking) {
        digitalWrite(tracks[i].redPin, blinkState ? HIGH : LOW);
      }
    }
  }
}

void handleSensorCommand() {
  // Simular pequenas variações realistas
  mockTemp += random(-3, 4) * 0.1;   // ±0.3 ºC
  mockHum  += random(-2, 3) * 0.2;   // ±0.4 %

  // Limites realistas
  if (mockTemp < 15.0) mockTemp = 15.0;
  if (mockTemp > 35.0) mockTemp = 35.0;

  if (mockHum < 30.0) mockHum = 30.0;
  if (mockHum > 80.0) mockHum = 80.0;

  // Converter para inteiros (OBRIGATÓRIO para extract_data.s)
  int tempInt = (int)mockTemp;
  int humInt  = (int)mockHum;

  /*
   * FORMATO EXATO ESPERADO:
   * TOKEN &unit:xxxx &value:xx # TOKEN &unit:xxxx &value:xx
   */

  Serial.print("TEMP &unit:celsius &value:");
  Serial.print(tempInt);
  Serial.print(" # HUM &unit:percent &value:");
  Serial.println(humInt);
}


