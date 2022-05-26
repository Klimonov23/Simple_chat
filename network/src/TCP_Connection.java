import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TCP_Connection {
    private final Socket socket;
    private final Thread rxthread;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final TCP_Connection_Listener event_listener;

    public TCP_Connection(TCP_Connection_Listener event_listener,String ip_adr,int port) throws IOException {
        this(new Socket(ip_adr,port),event_listener);

    }


    public TCP_Connection(Socket socket,TCP_Connection_Listener listener)throws IOException {
        this.event_listener=listener;
        this.socket=socket;
        in=new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));
        rxthread=new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                event_listener.onConnectionReady(TCP_Connection.this);
                while (!rxthread.isInterrupted()){
                event_listener.onReceiveString(TCP_Connection.this,in.readLine());
                }}
                catch (IOException e){
                    event_listener.onException(TCP_Connection.this,e);
                }finally {
                    event_listener.onDisconect(TCP_Connection.this);
                }
            }
        });
        rxthread.start();
    }
    public synchronized void send_message(String val){
        try{
            out.write(val+"\r\n"); //Нет символа конца строки
            out.flush();
        }
        catch (IOException e){
            event_listener.onException(TCP_Connection.this,e);
            disconect();
        }

    }
    public synchronized void disconect(){
        rxthread.interrupt();
        try{
        socket.close();}
        catch (IOException e){
            event_listener.onException(TCP_Connection.this,e);
        }
    }
    @Override
    public String toString(){
        return  "TCP Connection" + socket.getInetAddress()+ " :" + socket.getPort();
    }
}
