int sensor_pin = A0;
int motor = 16;
int output_value ;

#include <SoftwareSerial.h>
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define FIREBASE_HOST "irrigation-7b803.firebaseio.com"
#define FIREBASE_AUTH "wcGltPzg5C837zE0WmyQaZMbZCMYhOTyZ0WNHE0Z"
#define WIFI_SSID "Redmi"
#define WIFI_PASSWORD "qwerty123"

void setup() {

   Serial.begin(115200);

   pinMode(motor,OUTPUT);
   digitalWrite(motor,LOW);

   Serial.println("Reading From the Sensor ...");

   delay(2000);

   WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("connecting");
    while (WiFi.status() != WL_CONNECTED) {
      Serial.print(".");
      delay(500);
    }
    Serial.println();
    Serial.print("connected: ");
    Serial.println(WiFi.localIP());

    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

   }

void loop() {

   output_value= analogRead(sensor_pin);

   output_value = map(output_value,550,0,0,100);
  
   Firebase.setFloat("Moisture",output_value);
   if (Firebase.failed()) {
        Serial.print("failed");
        Serial.println(Firebase.error());  
        return;
    }

   if(Firebase.getString("Motor") != "1"){
    digitalWrite(motor,1);
   }else{
    digitalWrite(motor,0);
   }

   Serial.print("Mositure : ");

   Serial.print(output_value);

   Serial.println("%");

   delay(1000);

   }
