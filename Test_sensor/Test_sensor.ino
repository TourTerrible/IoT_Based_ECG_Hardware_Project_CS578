#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <math.h>


const int ledPin =  LED_BUILTIN;

const char* SSID_ = "Tenda";
const char* PASSWORD = "12345679";     

void setup() {
   pinMode(ledPin, OUTPUT);
   Serial.begin(115200);
  WiFi.begin(SSID_, PASSWORD);
  Serial.print("Connecting..");
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print("..");
  }
  Serial.println("Connected");
  pinMode(D5,INPUT);  //LO+
  pinMode(D6,OUTPUT);  //L0-

}

void loop() {
  if((digitalRead(D5) == 1)||(digitalRead(D6) == 1)){
    Serial.println('!');
  }
  else 
     Serial.println(analogRead(A0));

  delay(1);   
       

}
