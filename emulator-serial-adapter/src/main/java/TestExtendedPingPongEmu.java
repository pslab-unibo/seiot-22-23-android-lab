import channel.CommChannel;
import channel.ExtendedSerialCommChannel;

/**
 * Esempio di utilizzo del componente channel.ExtendedSerialCommChannel
 * che permette lo scambio di messaggi via seriale anche con
 * emulatore Android (usato ad esempio per sostituire la comunicazione via
 * Bluetooth).
 *
 *
 * @author acroatti, aricci
 *
 */
public class TestExtendedPingPongEmu {

    public static void main(String[] args) throws Exception {

        String serialPortName = "COM5"; // It must be changed!

        /* setting up the channel, with server for the emu*/
        System.out.print("Creating the serial comm channel with IP server ...");
        CommChannel channel = new ExtendedSerialCommChannel(serialPortName, 9600, 8080);
        System.out.println("OK");

        System.out.println("Waiting Arduino for rebooting...");
        Thread.sleep(4000);
        System.out.println("Ready.");

    /*
        while(true){
            System.out.println("Sending ping...");
            channel.sendMsg("ping");
            System.out.println("received: "+channel.receiveMsg());
            Thread.sleep(500);
        }
    */

    }

}
