#include <LiquidCrystal_I2C.h>

#include <Wire.h>

LiquidCrystal_I2C lcd(0x27, 16, 2);

void setup() {

Serial.begin(115200);

//Use predefined PINS consts

Wire.begin(D2, D1);

lcd.begin(D2,D1);

lcd.home();

lcd.print("Working");

}

void loop()
{
  // Do nothing here...
}
