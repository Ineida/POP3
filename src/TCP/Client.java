package TCP;

import java.io.*;
import java.net.Socket;


public class Client {
    private String ip;
    private int port;
    private PrintStream out;
    private BufferedReader in;
    private Socket client;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public Client() {
        super();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public PrintStream getOut() {
        return out;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    /**
     * start a TCP connexion with an serveur using the client ip and port
     * initialise flux
     * @throws IOException
     */
    public Boolean connexion() throws IOException {
        this.client = new Socket(this.ip, this.port);
        this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.out = new PrintStream(this.client.getOutputStream());
        return true;
    }

    /**
     * close the TCP connection and the flux
     * @throws IOException
     */
    public void closeConnexion() throws IOException {
        this.out.close();
        this.in.close();
        this.client.close();
    }

    /**
     * send a message to the serveur
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        out.println(message);
    }


    /**
     *
     * @return in.read(). Le prochain caractere dans la reponse du serveur.
     * @throws IOException
     */
    public String lineRead() throws IOException {
        String line = this.in.readLine();
        return line;
    }

    public int intRead() throws IOException {
        int lettre = -1;
        lettre= this.in.read();
        return lettre;
    }



}
