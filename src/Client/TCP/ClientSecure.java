package Client.TCP;

import Client.metier.StringServices;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class ClientSecure  extends  Client{
    private PrintStream out;
    private BufferedReader in;
    private SSLSocket client;
    private  String key;

    public ClientSecure(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.key = StringServices.generateRandomString(8);
    }

    public ClientSecure() {
        super();
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
     * start a Client.Controller.Controller.TCP connexion with an serveur using the client ip and port
     * initialise flux
     * @throws IOException
     */
    public Boolean connexion( boolean wantClientAuth ) throws IOException, KeyStoreException,
            CertificateException, NoSuchAlgorithmException, KeyManagementException
    {
        char[] passphrase = this.key.toCharArray();
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream(".keystore"), passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("");
        tmf.init(keystore);
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        context.init(null, trustManagers, null);

        SSLSocketFactory sf = context.getSocketFactory();

        this.client = (SSLSocket) sf.createSocket(this.ip, this.port);
        if(wantClientAuth) this.client.setWantClientAuth(true);
        this.client.getSession().getCipherSuite();
        this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.out = new PrintStream(this.client.getOutputStream());
        return true;
    }

    public Boolean connexion() throws IOException, CertificateException,
            NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
      return this.connexion(false);
    }


    /**
     * close the Client.Controller.Controller.TCP connection and the flux
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
    public void sendMessageAvecSautLine(String message) throws IOException {
        client.startHandshake();
        out.println(message);
        out.flush();
    }

    public void sendMessage(String message) throws IOException {
        client.startHandshake();
        out.print(message);
        out.flush();

    }


    public int intRead() throws IOException {
        return  this.in.read();
    }

}
