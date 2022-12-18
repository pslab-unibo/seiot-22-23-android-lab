#include "SoftwareSerial.h"
#include <Wire.h>

#define RX_PIN 2  // to be connected to TX of the BT module
#define TX_PIN 3  // to be connected to RX of the BT module
#define LED_PIN 8

SoftwareSerial channel(RX_PIN, TX_PIN);
String msg = "";

void setup() {
  pinMode(LED_PIN,OUTPUT);
  channel.begin(9600);
  
  Serial.begin(9600);
  while (!Serial){}
  Serial.println("ready to go.");   
}

void loop() {
  if (channel.available()) {
    char letter = (char)channel.read();
    if(letter != '\n'){
      msg.concat(letter);
    } else {
      if (msg == "on"){
        Serial.println("LED ON");  
        digitalWrite(LED_PIN,HIGH);
        channel.println("ok");  
      } else if (msg == "off"){
        Serial.println("LED OFF");    
        digitalWrite(LED_PIN,LOW);
        channel.println("ok");  
      } else {
        Serial.println(msg);  
      }
      msg = "";
    }
  }
}
