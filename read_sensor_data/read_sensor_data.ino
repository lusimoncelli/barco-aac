//importo librerias y configuro pines de comunicacion
#include <SoftwareSerial.h>

const int irSensorPin = 8;  // Pin digital conectado al sensor IR
int sensorValue = 0;        // Variable para almacenar la lectura del sensor
int lastSent = 0; 
SoftwareSerial btSerial(14, 15); // RX, TX

void setup() {
  pinMode(irSensorPin, INPUT);

  // Communication rate of the Bluetooth Module
  btSerial.begin(9600); 
  Serial.begin(9600);
}

void loop() {
    sensorValue = digitalRead(irSensorPin);
    if(sensorValue == 0 || sensorValue == 1 && lastSent == 0){
      btSerial.write(sensorValue);
      lastSent = sensorValue; 
     } 
}
