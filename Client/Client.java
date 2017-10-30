

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.*;
/**
 * Created by Abhishek on 21-10-2017.
 */
public class Client {
    static Socket socket;
    ObjectOutputStream oos;
    static ObjectInputStream ois;
    static PublicKey serverPublicKey;
    //static RSA rsa;
    public Client(String localHost, int i) {
        try{
            socket = new Socket(localHost, i);
        }catch(Exception e) {
            System.out.println("Error in creating client socket");
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) {
        Client client = new Client("localHost",7777);
        client.getServerPublicKey();
        try {
            rsa = new RSA();
        }catch (Exception e){
            e.printStackTrace();
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter to\n1.Register\n2.Authenticate");
        System.out.println("Enter username and password");
        int i = sc.nextInt();
        String[] data = new String[2];
        data[0] = sc.next();
        data[1] = sc.next();
        String str = null;
        try {
            str = rsa.encryptMessage(data[0]+","+data[1],serverPublicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            socket = new Socket("localHost",7777);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (i){
            case 1:
                client.setServerMode(3);
                client.sendFile(str);
                //client.register(str);
                break;
            case 2:
                client.setServerMode(2);
                client.sendFile(str);
               // client.authenticate(str);
                break;
        }
    }*/
    public static void main(String[] args) {
        Client client = new Client("localHost",7777);
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter to\n1.Register\n2.Authenticate");
        int i = sc.nextInt();
        System.out.println("Enter username and password");
        String[] data = new String[2];
        data[0] = sc.next();
        data[1] = sc.next();
        if(i==1)
            sendFile(data[0]+","+data[1]+","+1);
        else
            sendFile(data[0]+","+data[1]+","+2);
        System.out.println("Disconnected");
    }

    private static void sendFile(String str) {
        File file = selectRandomFile();
        BufferedImage image = new Steganography().encode(file.getPath(),str);
        saveImage(image);
        try {
            ImageIO.write(image, "png", socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }try {
            ois = new ObjectInputStream(socket.getInputStream());
            String response = (String)ois.readObject();
            System.out.println(response);
            ois.close();
            socket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void saveImage(BufferedImage image) {
        File f = new File("Z:\\project\\Ntal MP\\Client\\Images\\op.png");
        try {
            ImageIO.write(image,"png",f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File selectRandomFile() {
        File dir = new File("Z:\\project\\Ntal MP\\Client\\Images\\");
        File[] list = dir.listFiles();
        return list[(int)Math.random()*list.length];

    }
}
