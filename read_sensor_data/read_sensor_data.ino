//importo librerias y configuro pines de comunicacion
#include <SoftwareSerial.h>
#include <TimerThree.h>

const int irSensorPin = 8;  // Pin digital conectado al sensor IR
int sensorValue = 0;        // Variable para almacenar la lectura del sensor
SoftwareSerial btSerial(14, 15); // RX, TX
unsigned long myTime; //para millis y probar

//esto para protocolo de comunicacion
void rts();
unsigned int buffer1[125];
unsigned int buffer2[125];
bool bufferFlag = false;
bool sendFlag = false;

void setup() {
  // Initialization
  Timer3.initialize(50000);  // every 50 ms
  Timer3.attachInterrupt(rts);
  pinMode(irSensorPin, INPUT);

  // Communication rate of the Bluetooth Module
  btSerial.begin(9600); 
  Serial.begin(9600);
}

void rts(){
  static unsigned long counter = 0;
  static unsigned int pos = 0;
  counter++;
  
  // Every 50 ms
  if (counter%1 == 0)
  {
    // send the value of analog input 0:
      buffer1[pos++] = digitalRead(irSensorPin);
  }
    
    if (pos == 125)
    {
      for (unsigned int i=0; i<125; i++)
        buffer2[i] = buffer1[i];
      bufferFlag = true;
      pos = 0;
    }
  }

void loop() {
  // Si la bandera está activada, enviar el buffer a través del módulo Bluetooth
  if (bufferFlag) {
    for (unsigned int i = 0; i < 125; i++) {
    btSerial.write(buffer2[i]);
  }
  bufferFlag = false;
  }
}
