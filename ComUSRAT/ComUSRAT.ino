#include <TimerThree.h>

void rts();

unsigned int buffer1[100];
unsigned int buffer2[100];
bool bufferFlag = false;
bool sendFlag = false;

int level = 0;        // value output to the LED

void setup() {
  // initialize serial communications at 115200 bps:
  Serial.begin(9600);
  Timer3.initialize(5000);  // every 5ms
  Timer3.attachInterrupt(rts);
  pinMode(LED_BUILTIN, OUTPUT);
}

void rts(){
  static unsigned long counter = 0;
  static unsigned int pos = 0;

  counter++;

  // Every 500 ms
  if (counter%500 == 0)
  {
    // blink led
    level = level^1;
    digitalWrite(LED_BUILTIN, level);
  }
  
  // Every 5 ms
  if (counter%1 == 0)
  {
    // send the value of analog input 0:
      buffer1[pos++] = analogRead(A0);
  }
    
    if (pos == 100)
    {
      for (unsigned int i=0; i<100; i++)
        buffer2[i] = buffer1[i];
      
      bufferFlag = true;
      pos = 0;
    }
  }

void loop() 
{
  if (Serial.available() > 0)
  {
    sendFlag = true;
  }
  
  if (bufferFlag && sendFlag)
  {
    char data = Serial.read();
    if (data == 's')
    { 
      for (unsigned int i=0; i<100; i++)
      {
        int high = buffer2[i]/256;
        int low = buffer2[i]%256;
        Serial.write(high);
        Serial.write(low);
      }
      bufferFlag = false;
      sendFlag = false;
    }
  }
}
