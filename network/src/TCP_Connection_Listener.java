public interface TCP_Connection_Listener {
    void onConnectionReady(TCP_Connection tcp_connection);
    void onReceiveString(TCP_Connection tcp_connection,String val);
    void onDisconect(TCP_Connection tcp_connection);
    void onException(TCP_Connection tcp_connection,Exception e);
}
