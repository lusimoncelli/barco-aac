char myChar;
const int irSensorPin = A0;  // Pin analógico conectado al sensor IR
int sensorValue = 0;         // Variable para almacenar la lectura del sensor

void setup() {
  Serial.begin(9600);
  Serial1.begin(9600);
  Serial1.println("Conexión exitosa"); 
}

void loop() {
  // Leer datos digitales de Serial1 (Bluetooth)
  while (Serial1.available()) {
    myChar = Serial1.read();
    Serial.print(myChar);
  }

  // Leer datos analógicos del sensor IR
  sensorValue = analogRead(irSensorPin);

  // Transmitir datos analógicos a través de Serial1 (Bluetooth)
  Serial1.print(sensorValue);

  // Enviar datos analógicos al Monitor Serial (opcional)
  Serial.print("Valor del sensor: ");
  Serial.println(sensorValue);

  // Agregar un pequeño retardo para ajustar la velocidad de actualización
  delay(100);  // Puedes ajustar el valor del retardo según sea necesario
}
