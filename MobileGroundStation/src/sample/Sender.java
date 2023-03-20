package sample;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender {

    public static void main(String[] args) throws Exception
    {
        DatagramSocket datasoc = new DatagramSocket();
        String strn = "Welcome to DatagramSocket class";
        //InetAddress ipaddr = InetAddress.getByName("127.0.0.1");
        InetAddress ipaddr = InetAddress.getLocalHost();
        DatagramPacket dpac = new DatagramPacket(strn.getBytes(), strn.length(), ipaddr, 3000);
        datasoc.send(dpac);
        datasoc.close();
    }

}
