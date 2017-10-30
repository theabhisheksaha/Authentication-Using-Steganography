import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abhishek on 21-10-2017.
 */
public class Server extends Thread {
    ServerSocket ss;
    Socket clientSocket;
    ObjectOutputStream oos;
    static private Map<String,String > dataset = new HashMap<>();

    Server(int port){
        try {
            ss = new ServerSocket(port);
        }catch (Exception e){
            System.out.println("Error in creating server socket");
        }
    }

    public void run(){
        while (true){
            System.out.println("Waiting...");
            try {
                clientSocket = ss.accept();
                System.out.println("Connected to "+clientSocket);
            } catch (Exception e) {
                System.out.println("Couldn't create client socket-server");
            }
            try {
                BufferedImage image = ImageIO.read(clientSocket.getInputStream());
                saveImage(image);
                String str = new Steganography().decode(image);
                System.out.println(str);
                String[] data = str.split(",");
                String response;
                if(data[2].equals("1")){
                    response = register(data[0],data[1]);
                }else{
                    response = authenticate(data[0],data[1]);
                }
                System.out.println(response);
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.writeObject(response);
                oos.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static void saveImage(BufferedImage image) {
        File f = new File("Z:\\project\\Ntal MP\\Server\\Images\\serv.png");
        try {
            ImageIO.write(image,"png",f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String register(String username,String password){
        if(!dataset.containsKey(username)){
            dataset.put(username,password);
            return "Registered!";
        }else{
            return "Already registered!";
        }
    }

    private static String authenticate(String username, String password){
        if(dataset.containsKey(username)){
            if(dataset.get(username).equals(password)){
                return "Authenticated successfully";
            }else{
                return "Wrong password";
            }
        }else{
            return "Wrong credentials";
        }
    }
    private  void createMap() {
        dataset.put("bd","1234");
        dataset.put("neelam","12345");
        dataset.put("lokesh","123456");
    }

    public static void main(String[] args) {
        Server server = new Server(7777);
        server.createMap();
        server.start();
    }
}
