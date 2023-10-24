<<<<<<< HEAD
char myChar;
const int irSensorPin = A0;  // Pin analógico conectado al sensor IR
int sensorValue = 0;         // Variable para almacenar la lectura del sensor

void setup() {
  Serial.begin(38400);
  Serial1.begin(38400);
  Serial1.println("Conexión exitosa"); 
}

void loop() {
  // Leer datos digitales de Serial1 (Bluetooth)
  while (Serial1.available()) {
    myChar = Serial1.read();
    //Serial.print(myChar);
  }

  // Leer datos analógicos del sensor IR
  sensorValue = analogRead(irSensorPin);

  // Transmitir datos analógicos a través de Serial1 (Bluetooth)
  //Serial1.print(sensorValue);

  // Enviar datos analógicos al Monitor Serial (opcional)
  //Serial.print("Valor del sensor: ");
  //Serial.println(sensorValue);

  // Agregar un pequeño retardo para ajustar la velocidad de actualización
  delay(100);  // Puedes ajustar el valor del retardo según sea necesario

=======
#include <SoftwareSerial.h>

char myChar;
const int irSensorPin = 8;  // Pin digital conectado al sensor IR
int sensorValue = 0;        // Variable para almacenar la lectura del sensor
SoftwareSerial btSerial(15, 14); // RX, TX

void setup() {
  Serial.begin(9600);
  btSerial.begin(9600);
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


  // Limpio el buffer
  btSerial.flush();
}
