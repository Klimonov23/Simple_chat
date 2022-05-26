import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame implements ActionListener,TCP_Connection_Listener

{
    private static final String ip_adress="192.168.0.11";
    private static final int port=22;
    private static final int WIDTH=600;
    private static final int HEIGHT=400;
    private final JTextArea log=new JTextArea();
    private final JTextField nickname=new JTextField("Kirill");
    private final JTextField input=new JTextField();
    private TCP_Connection connection;
    public static void main(String[] args){
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new ClientWindow();
                }
            });
        }

        private ClientWindow(){
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setSize(WIDTH,HEIGHT);
            setLocationRelativeTo(null);
            setAlwaysOnTop(true);
            log.setEditable(true);
            log.setLineWrap(true);
            input.addActionListener(this);
            add(log, BorderLayout.CENTER);
            add(input,BorderLayout.SOUTH);
            add(nickname,BorderLayout.NORTH);
            setVisible(true);
            try {
                connection = new TCP_Connection(this,ip_adress,port);
            } catch (IOException e) {
                print_message("Connection exception"+e);

            }

        }

    @Override
    public void actionPerformed(ActionEvent e) {
        String message=input.getText();
        if (message.equals("")) return;
        input.setText(null);
        connection.send_message(nickname.getText()+": "+ message);
    }

    @Override
    public void onConnectionReady(TCP_Connection tcp_connection) {
        print_message("Connection ready");

    }

    @Override
    public void onReceiveString(TCP_Connection tcp_connection, String val) {
        print_message(val);

    }

    @Override
    public void onDisconect(TCP_Connection tcp_connection) {
        print_message("Connection loads");

    }

    @Override
    public void onException(TCP_Connection tcp_connection, Exception e) {
    print_message("Connection exception"+e);
    }
    private synchronized void print_message(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg+"\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
