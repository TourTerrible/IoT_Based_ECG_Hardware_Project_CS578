
//Libraries Included
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <PubSubClient.h>
#include <LiquidCrystal_I2C.h>
#include <Wire.h>

//Parameters and UBIDOTS API credetentials 
#define WIFISSID "Abdul Ahad" 
#define PASSWORD "180108002"
#define TOKEN "BBFF-p1Dv8xB3vBf6ayhTjBLS5pEwDUxpex"
#define MQTT_CLIENT_NAME "abdulahad"

//Sensor_pin for ECG sensor
#define SENSOR_PIN A0 


//Global Variable declaration
char mqttBroker[]  = "industrial.api.ubidots.com";
char payload[100];
char topic[150];
char str_sensor[10];
char str_status[2];
float sensor_value=0;

WiFiClient ubidots;
PubSubClient client(ubidots);
LiquidCrystal_I2C lcd(0x27, 16, 2);


//Function to display Health condition on LCD
void LCD_Display(int condition){
  
  Wire.begin(D2, D1);
  
  lcd.begin(D2,D1);
  
  lcd.home();
  
  if(condition==1){
    lcd.print("Health:Normal");
  }
  else {
    lcd.print("Health:Critical");
  }
  
}


//Function to analyze ECG sensor values and calculate RR interval
// Health said to be normalif RR interval is in range (0.6,1)
float rr_calculation(){
  float peak,minima,rr;
  for(int i=0;i<10;i++){
    peak=max(peak,sensor_value);
    minima=min(minima,sensor_value);
  }
  rr=(peak-minima)/((peak+minima)/2);
  return(rr);
}

//Sending ECG sensor value to IoT clouds 
void Send_ECG_Value(float sensor_value){
  sprintf(topic, "%s%s", "/v1.6/devices/", "ECG_IOT");
  sprintf(payload, "%s", ""); 
  sprintf(payload, "{\"%s\":", "ecg_sensor_value");
  dtostrf(sensor_value, 4, 2, str_sensor);
  sprintf(payload, "%s {\"value\": %s}}", payload, str_sensor);
  client.publish(topic, payload);
  client.loop();
  delay(500);
}


//Sending Health Status to IoT clouds 
void Send_Health_Status(int health_status){
  sprintf(topic, "%s%s", "/v1.6/devices/", "ECG_IOT");
  sprintf(payload, "%s", ""); 
  sprintf(payload, "{\"%s\":", "health_status");
  dtostrf(health_status, 4, 1, str_status);
  sprintf(payload, "%s {\"value\": %s}}", payload, str_status);
  client.publish(topic, payload);
  client.loop();
  delay(500);
}


void callback(char* topic, byte* payload, unsigned int length) {
  char p[length + 1];
  memcpy(p, payload, length);
  p[length] = NULL;
  Serial.write(payload, length);
  Serial.println(topic);
}

//Connect MQTT Client

void connect() {
  while (!client.connected()) {
    Serial.println("connecting...");
    
    if (client.connect(MQTT_CLIENT_NAME, TOKEN, "")) {
      Serial.println("Connected!");
    } 
    else {
      Serial.print("Failed, rc=");
      Serial.print(client.state());
      delay(1000);
    }
  }
}


void setup() {
  Serial.begin(115200);
  WiFi.begin(WIFISSID, PASSWORD);
  pinMode(SENSOR_PIN, INPUT);
  
  Serial.println();
  Serial.print("WiFi connecting...");
  
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  
  Serial.println("");
  Serial.println("Connected with wifi successfully!");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  client.setServer(mqttBroker, 1883);
  client.setCallback(callback);  
}

void loop() {
  //if client is not connected , connect it.
  if (!client.connected()) {
    connect();
  }

   sensor_value= analogRead(SENSOR_PIN); 
   Send_ECG_Value(sensor_value);
   
   float rr=rr_calculation();
   int health_status=1;

   //ananlysis for health condition
   if(rr<0.6 or rr>1){
     health_status=0;
   }
   else{
    health_status=1;
   }
   
   LCD_Display(health_status);
   Send_Health_Status(health_status);
}
