import java.io.*; 
import java.util.*; 
import java.net.*;

class Recieve implements Runnable{
    Socket c;
    DataInputStream dis;
    
    Recieve(Socket c, DataInputStream dis){
        this.c = c;
        this.dis = dis;
    }

    public void run(){
      String recieved;

      while(true){
          try{
            recieved = dis.readUTF();
            System.out.println("Message:  " + recieved);
          }
          catch(IOException e){
              System.out.println("IOException :" + e.getMessage());
          }
          
      }
    }
}

class Send implements Runnable{
    Socket c;
    DataOutputStream dos;
    
    Send(Socket c, DataOutputStream dos){
        this.c = c;
        this.dos = dos;
    }

    public void run(){
      String recieved;
      Scanner sc = new Scanner(System.in);
      
      while(true){
          recieved = sc.nextLine();
          try{
            dos.writeUTF(recieved);
            System.out.println("Sent:    " + recieved);
          }
          catch(IOException e){
              System.out.println("IOException:  " + e.getMessage());
          }
      } 
    }
}

class Client{
    public static void main(String[] args) {
        try {
            Socket client = new Socket("localhost", 5000);
            DataInputStream dis = new DataInputStream(client.getInputStream()); 
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());

             //creating thraeds for send and recieve
             Recieve r = new Recieve(client, dis);
             Send s = new Send(client, dos);
 
             Thread t1 = new Thread(r);
             Thread t2 = new Thread(s);
 
             t1.start();
             t2.start();
        } 
        catch (Exception e) {
            //TODO: handle exception
            System.out.println("Exception:  " + e.getMessage());
        }
    }
}