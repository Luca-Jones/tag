package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PacketListener extends Thread {

    private static final int BUFFER_SIZE = 1024;
    public final ConcurrentLinkedQueue<DatagramPacket> packets = new ConcurrentLinkedQueue<>();
    public DatagramSocket socket;

    public PacketListener(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            byte[] data = new byte[BUFFER_SIZE];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            packets.add(packet);
        }
    }

    
}
