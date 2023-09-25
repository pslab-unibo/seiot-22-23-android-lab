# seiot-22-23-android-lab

Code for the labs of the academic year 2022/23 Embedded Systems and IoT course in the Computer Science and Engineering Bachelor Degree of University of Bologna.

# Structure of the repository
Inside this repository you will find several simple example of android applications to learn the basic concepts of the Android system plus a full example of an android app that connects to arduino to turn a led on and off.

- [`AndroidExample00`](./AndroidExample00)
Is a basic example of an Hello World and shows the lifecycle of an Activity.

- [`AndroidExample01`](./AndroidExample01)
Shows examples of how activities can be called from one another.

- [`AndroidExample02`](./AndroidExample02)
Shows an example of how to use multiple threads on the same application.

- [`AndroidExample03`](./AndroidExample03)
Is an example of doing HTTP requests to download an image from the internet.

- [`AndroidExample04`](./AndroidExample04)
Uses several built-in sensors to sample data and display them on screen.

- [`RemoteBlinkApp`](./RemoteBlinkApp)
Is a project configured to use bluetooth to connect to Arduino and turn a led on and off.

- [`arduino`](./arduino) is a folder containing utils and examples on how to use the bluetooth module.
  
- [`emulator-serial-adapter`](./emulator-serial-adapter) is a Java app that allow the Android emulator to connect using a WebSocket on the host machine and send message to arduino using the USB Serial.

# How to use RemoteBlinkApp

There are two ways you can use the RemoteBlinkApp:
- with an android device that has bluetooth capabilities
- with the Android Studio emulator

## Use app with Android device
1. Connect the Bluetooth module to Arduino. Please check that you connected the wires correctly on the RX and TX pins. TX of Arduino needs to be connected to RX on the bluetooth module and vice-versa. 
2. Setup your AT module using the [AT mode util](./arduino/at-mode-HC05/at-mode-HC05.ino)
3. Load the code on Arduino:
   - [bt-remote-blinking](./arduino/bt-remote-blinking) shows how to read directly from the bluetooth serial.
   - [bt-remote-blinking-msg](./arduino/bt-remote-blinking-msg) uses a MessageService class that encapsulate the use of serial communication. This is useful to use both the serial and the bluetooth at the same time.
   Check the `readSerialMessage(bool useBluetooth, bool useSerial) to understand the behaviour of this class and how to use it.
4. Install the [RemoteBlinkApp](./RemoteBlinkApp) on your Android Device.
5. Turn Bluetooth On.
6. Follow the steps on the app:
   - Scan for devices
   - Click on the AT module to establish connection
   - The `LedSwitchActivity` will open check the code in here to extend it for your assignments.
   - You should be able to turn the led on and off

## Use app with Android emulator on AndroidStudio
1. Connect Arduino to the USB serial, you won't need the bluetooth module for this.
3. Load the code on Arduino:
   - [emu-remote-blinking](./arduino/emu-remote-blinking) shows how to connect to read from the serial _faking_ the existence of two separate channels by using a special character (`$`) to discriminate the source of the message. Check the `readSerialMessage(bool useBluetooth, bool useSerial) to understand the behaviour of this class and how to use it and note how it's different from the Bluetooth implementation.
4. Start the [emulator-serial-adapter](./emulator-serial-adapter) this will open a socket on your local machine that will wait for messages and redirect them to the serial appending the special character (`$`) to each message.
5. Start the [RemoteBlinkApp](./RemoteBlinkApp) on the Android emulator and follow this steps:
   - click on the emulator button on the bottom of the screen, you won't need to pair with any bluetooth device since you're using the serial
   - The `LedSwitchEmulatedActivity` will open check the code here to extend it for your assignments
   - You should be able to turn the led on and off
  

