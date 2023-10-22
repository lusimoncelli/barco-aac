char myChar;
const int irSensorPin = 8;  // Pin digital conectado al sensor IR
int sensorValue = 0;        // Variable para almacenar la lectura del sensor

void setup() {
  Serial.begin(9600);
  pinMode(irSensorPin, INPUT);
  Serial1.println("Conexión exitosa"); 
}

void loop() {
  // Leer datos digitales de Serial1 (Bluetooth)
  while (Serial1.available()) {
    myChar = Serial1.read();
    Serial.print(myChar);
  }

  // Leer datos del sensor IR
  // IR = 0 -> Parpadeo
  // IR = 1 -> Ojo abierto
  sensorValue = digitalRead(irSensorPin);

  // Transmitir datos analógicos a través de Serial1 (Bluetooth)
  Serial1.print(sensorValue);

  // Enviar datos analógicos al Monitor Serial (opcional)
  Serial.print("Valor del sensor: ");
  Serial.println(sensorValue);

  // Agregar un pequeño retardo para ajustar la velocidad de actualización
  delay(500);  // Puedes ajustar el valor del retardo según sea necesario
}
