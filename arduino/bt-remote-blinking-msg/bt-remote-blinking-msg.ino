#include "MsgService.h"
#include <Wire.h>

#define LED_PIN 8

void setup() {
  MsgServiceBT.init();
  MsgService.init();
  pinMode(LED_PIN,OUTPUT);
  
  Serial.begin(9600);
  while (!Serial){}
  Serial.println("ready to go.");   
}

void loop() {
  if (MsgServiceBT.isMsgAvailable()) {
    Msg* msg = MsgServiceBT.receiveMsg();
    String command = msg->getContent();
    if (command == "on"){
       Serial.println("LED ON");  
       digitalWrite(LED_PIN,HIGH);
    } else if (command == "off"){
      Serial.println("LED OFF");    
      digitalWrite(LED_PIN,LOW);
    }else{
      Serial.println(command);
    }
    delete msg;
  }
  //in this example messages incoming from serial are flushed
  readSerialMessage(true, false);
}
