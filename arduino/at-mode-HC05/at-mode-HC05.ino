#include "SoftwareSerial.h"

/*
 *  BT module connection:  
 *  - pin 2 <=> TXD
 *  - pin 3 <=> RXD
 *
 */ 
SoftwareSerial btChannel(2, 3);

void setup() {
  Serial.begin(9600);
  while (!Serial) {};
  
  btChannel.begin(9600);

  Serial.println("sending cmd..");
  /* check if it is there */
  btChannel.print("AT");
  delay(1000);
  
  /* set the name */
  //btChannel.print("AT+NAMEisi00"); // Set the name to isiXX
  //delay(1000);

  /* get the version */
  // btChannel.print("AT+VERSION");
  // delay(1000);

  /* get the name */
  //btChannel.print("AT+NAME"); 
  //delay(1000);

  /* get the addr */
   //btChannel.print("AT+ADDR"); 
   //delay(1000);

}

void loop() {
  
  /* reading data from BT to Serial */
  if (btChannel.available())
    Serial.write(btChannel.read());

  /* reading data from the Serial to BT */
  if (Serial.available())
    btChannel.write(Serial.read());
}
