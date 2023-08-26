import java.io.*; 
import java.util.*; 
import java.net.*;

class ClientHandler implements Runnable{
    String name;
    DataInputStream dis;
    DataOutputStream dos;
    Socket client;
    boolean isLoggedin;

    ClientHandler(Socket c, String name, DataInputStream dis, DataOutputStream dos){
        this.client = c;
        this.name = name;
        this.dis = dis;
        this.dos = dos;
        this.isLoggedin = true;
    }

    public void run(){
        String recieved;
        while(true){
            try {
                recieved = dis.readUTF();
                System.out.println(recieved);

                if(recieved == "logout"){
                    this.isLoggedin = false;
                    this.client.close();
                    break;
                }

                for(ClientHandler c : MultiServer1.arr){
                    if(c.isLoggedin == true && c.name.equals(this.name) == false){
                        c.dos.writeUTF(name + ":    " + recieved);
                    }
                }
            } catch (Exception e) {
                //TODO: handle exception
                System.out.println("Exception:  " + e.getMessage());
            }
        }

        try {
            this.dis.close();
            this.dos.close();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Exception:  " + e.getMessage());
        }

    }
}

class MultiServer1{
    static int i = 0;
    static Vector<ClientHandler> arr = new Vector<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Server Started!");
            Socket client;

            while(true){
                //Accept connections
                client = server.accept();
                System.out.println("New client recieved:    " + client);

                // obtain input and output streams 
                DataInputStream dis = new DataInputStream(client.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(client.getOutputStream()); 

                //Creating new handler for client
                ClientHandler c = new ClientHandler(client, "client" + i,dis, dos);

                Thread t = new Thread(c);
                arr.add(c);

                t.start();

                i++;
                
            }
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Exception:  " + e.getMessage());
        }    
    }
}