#include <SoftwareSerial.h>

char myChar;
const int irSensorPin = 8;  // Pin digital conectado al sensor IR
int sensorValue = 0;        // Variable para almacenar la lectura del sensor
SoftwareSerial btSerial(0, 1); // RX, TX

void setup() {
  Serial.begin(9600);
  pinMode(irSensorPin, INPUT);
}

void loop() {
  // Leer datos digitales de Serial1 (Bluetooth)
  while (btSerial.available()) {
    myChar = btSerial.read();
    Serial.print(myChar);
  }

  // Leer datos del sensor IR
  // IR = 0 -> Parpadeo
  // IR = 1 -> Ojo abierto
  sensorValue = digitalRead(irSensorPin);

  // Transmitir datos analógicos a través de Serial1 (Bluetooth)
  btSerial.write(sensorValue);

  // Enviar datos analógicos al Monitor Serial (opcional)
  Serial.print("Valor del sensor: ");
  Serial.println(sensorValue);

  // Agregar un pequeño retardo para ajustar la velocidad de actualización
  delay(500);
}
