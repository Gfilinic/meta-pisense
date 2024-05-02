#include <stdio.h>
#include <wiringPi.h>

#define SENSOR_PIN 4 // Example GPIO pin where your sensor is connected

int main(void) {
    // Initialize WiringPi
    wiringPiSetupGpio();

    // Set the sensor pin as input
    pinMode(SENSOR_PIN, INPUT);

    while (1) {
        // Read the state of the sensor pin
        int sensorState = digitalRead(SENSOR_PIN);

        // Print the state
        printf("Sensor state: %d\n", sensorState);

        // Wait for a short time before reading again
        delay(1000); // Delay for 1 second
    }

    return 0;
}
