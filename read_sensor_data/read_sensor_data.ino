#include <SoftwareSerial.h>

char myChar;
const int irSensorPin = 8;  // Pin digital conectado al sensor IR
int sensorValue = 0;        // Variable para almacenar la lectura del sensor
SoftwareSerial btSerial(15, 14); // RX, TX

void setup() {
  Serial.begin(38400);
  btSerial.begin(38400);
  pinMode(irSensorPin, INPUT);
}

void loop() {

  // Leer datos digitales del serial bt
  while (btSerial.available()) {
    myChar = btSerial.read();
    Serial.print(myChar);
  }

  // Leer datos del sensor IR
  // IR = 0 -> Parpadeo
  // IR = 1 -> Ojo abierto
  sensorValue = digitalRead(irSensorPin);
  btSerial.write(sensorValue); // Transmitir datos a través del btSerial

  // Enviar datos analógicos al Monitor Serial (opcional)
  //Serial.print("Valor del sensor: ");
  //Serial.println(sensorValue);

  // Limpio el buffer
  btSerial.flush();
}
