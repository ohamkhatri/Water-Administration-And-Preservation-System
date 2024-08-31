#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <Firebase_ESP_Client.h>
//#include <Wire.h>
//#include <Adafruit_Sensor.h>
#include <NTPClient.h>
#include <WiFiUdp.h>

// Provide the token generation process info.
#include "addons/TokenHelper.h"
// Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

// Insert your network credentials
#define WIFI_SSID "JioFiber-4_plus"
#define WIFI_PASSWORD "676*****210"

// Insert Firebase project API Key
#define API_KEY "AIzaSyDuT-ba-2fkXcthXDgZxh8dnxgjhejhef"

// Insert Authorized Email and Corresponding Password
#define USER_EMAIL "abc04@gmail.com"
#define USER_PASSWORD "**********"

// Insert RTDB URLefine the RTDB URL
#define DATABASE_URL "https://rescueforce-3d798-default-rtdb.asia-southeast1.firebasedatabase.app/"

////////////////////////////////////////////////////////////////
#include <TinyGPS++.h>
#include <SoftwareSerial.h>
TinyGPSPlus gps;  // The TinyGPS++ object

SoftwareSerial ss(4, 5);  // The serial connection to the GPS device

const int trigPin = 12;
const int echoPin = 14;

//define sound velocity in cm/uS
#define SOUND_VELOCITY 0.034
#define CM_TO_INCH 0.393701

long duration;
float distanceCm;
float distanceInch;

float latitude, longitude;
String lat_str, lng_str;

int requestt = 0;
int ReceivedValue;

int i = 0;

////////////////////////////////////////////////////////////////

// Define Firebase objects
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

// Variable to save USER UID
String uid;

// Database main path (to be updated in setup with the user UID)

String databasePath;
//String requestPath;

// Database child nodes
String distPath = "/distance";
String latiPath = "/latitude";
String lngiPath = "/longitude";
String timePath = "/timestamp";
//String rPath = "/request";


// Parent Node (to be updated in every loop)
String parentPath;


FirebaseJson json;

// Define NTP Client to get time
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org");

// Variable to save current epoch time
int timestamp;

// Timer variables (send new readings every three minutes)
unsigned long sendDataPrevMillis = 0;
unsigned long timerDelay = 10000;

// Initialize WiFi
void initWiFi() {
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to WiFi ..");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print('.');
    delay(1000);
  }
  Serial.println(WiFi.localIP());
  Serial.println();
}

// Function that gets current epoch time
unsigned long getTime() {
  timeClient.update();
  unsigned long now = timeClient.getEpochTime();
  return now;
}

void setup() {
  Serial.begin(115200);
  ss.begin(9600);

  pinMode(trigPin, OUTPUT);  // Sets the trigPin as an Output
  pinMode(echoPin, INPUT);   // Sets the echoPin as an Input

  // Initialize BME280 sensor
  //initBME();
  
  initWiFi();
  timeClient.begin();

  // Assign the api key (required)
  config.api_key = API_KEY;

  // Assign the user sign in credentials
  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD;

  // Assign the RTDB URL (required)
  config.database_url = DATABASE_URL;

  Firebase.reconnectWiFi(true);
  fbdo.setResponseSize(4096);

  // Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback;  //see addons/TokenHelper.h

  // Assign the maximum retry of token generation
  config.max_token_generation_retry = 5;

  // Initialize the library with the Firebase authen and config
  Firebase.begin(&config, &auth);

  // Getting the user UID might take a few seconds
  Serial.println("Getting User UID");
  while ((auth.token.uid) == "") {
    Serial.print('.');
    delay(1000);
  }
  // Print user UID
  uid = auth.token.uid.c_str();
  Serial.print("User UID: ");
  Serial.println(uid);

  // Update database path
  //databasePath = "/UsersData/" + uid + "/readings";
  //databasePath = "/UsersData";
  databasePath = "/UsersData/" + uid + "/";
}

void loop() {

  while (ss.available() > 0) {

    if (gps.encode(ss.read()))

    {

      //Serial.println(GPSData);

      if (gps.location.isValid())

      {

        latitude = gps.location.lat();

        lat_str = String(latitude, 6);

        longitude = gps.location.lng();

        lng_str = String(longitude, 6);
      }
    }
  }

  // Clears the trigPin
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  // Sets the trigPin on HIGH state for 10 micro seconds
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  // Reads the echoPin, returns the sound wave travel time in microseconds
  duration = pulseIn(echoPin, HIGH);

  // Calculate the distance
  distanceCm = duration * SOUND_VELOCITY / 2;

  // Convert to inches
  distanceInch = distanceCm * CM_TO_INCH;

  // Prints the distance on the Serial Monitor
  Serial.print("Distance: ");
  Serial.print(distanceCm);
  Serial.println("cm");
  // Serial.print("Distance (inch): ");
  // Serial.println(distanceInch);
  Serial.print("Latitude: ");
  Serial.println(lat_str);
  Serial.print("Longitude: ");
  Serial.println(lng_str);

  delay(500);
  

  // Send new readings to database
  if (Firebase.ready() && (millis() - sendDataPrevMillis > timerDelay || sendDataPrevMillis == 0)) {
    sendDataPrevMillis = millis();
    i = i + 1;
    //Get current timestamp
    timestamp = getTime();
    Serial.print("time: ");
    Serial.println(timestamp);

    // parentPath = databasePath + "/" + String(timestamp);
    //parentPath = databasePath + "requests" + String(timestamp);

    json.set(distPath.c_str(), String(distanceCm));
    json.set(latiPath.c_str(), String(lat_str));
    json.set(lngiPath.c_str(), String(lng_str));
    json.set(timePath, String(timestamp));
    // json.set(rPath.c_str(), String(requestt));
    //Serial.printf("Set json... %s\n", Firebase.RTDB.setJSON(&fbdo, parentPath.c_str(), &json) ? "ok" : fbdo.errorReason().c_str());

    if (i == 1 || 2 || 3 || 4 || 5 || 6) {
      //parentPath = databasePath + "/" + String(timestamp);
      //parentPath = databasePath + "/"  + uid + "/requests" + String(timestamp);
      //parentPath = "/UsersData/" + uid + "/requests" + String(timestamp);
      //parentPath = databasePath + "requests" + String(timestamp);
      //Serial.printf("Set json... %s\n", Firebase.RTDB.setJSON(&fbdo, parentPath.c_str(), &json) ? "ok" : fbdo.errorReason().c_str());

      //Firebase.RTDB.setInt(&fbdo, parentPath.c_str(), i);

      //Firebase.RTDB.setInt(&fbdo, "/UsersData/" + uid + "/requests", i);

      if (Firebase.RTDB.getInt(&fbdo, "/UsersData/" + uid + "/requests")) {
        if (fbdo.dataType() == "int") {
          ReceivedValue = fbdo.intData();
          Serial.println(ReceivedValue);
          if (ReceivedValue == 1) {
            i = 6 ;
          }
        }
      } else {
        Serial.println(fbdo.errorReason());
      }
    }
    if (i == 6) {
      i = 0;
      //parentPath = requestPath + "/" + String(timestamp);
      //parentPath = databasePath + "/"  + uid + "/readings" + String(timestamp);
      //parentPath = "/UsersData/" + uid + "/readings" + String(timestamp);
      parentPath = databasePath + "readings" + String(timestamp);
      Serial.printf("Set json... %s\n", Firebase.RTDB.setJSON(&fbdo, parentPath.c_str(), &json) ? "ok" : fbdo.errorReason().c_str());
    }
    // Serial.printf("Set json... %s\n", Firebase.RTDB.setJSON(&fbdo, parentPath.c_str(), &json) ? "ok" : fbdo.errorReason().c_str());
  }
}
