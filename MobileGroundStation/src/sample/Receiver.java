package sample;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Receiver {

    public static void main(String[] args) throws Exception
    {
        DatagramSocket datasoc = new DatagramSocket(3000);
        byte[] buff = new byte[1024];
        DatagramPacket dpac = new DatagramPacket(buff, 1024);
        datasoc.receive(dpac);
        String strn = new String(dpac.getData(), 0, dpac.getLength());
        System.out.println(strn);




        datasoc.close();
    }

}
