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
    private  String key = "passwordClient";

    public ClientSecure(String ip, int port) {
        this.ip = ip;
        this.port = port;
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
        keystore.load(null, passphrase);
        FileOutputStream file = new FileOutputStream ("keyStoreClient.jks");
        keystore.store(file , passphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keystore);
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        context.init(null, trustManagers, null);

        SSLSocketFactory sf = context.getSocketFactory();

        this.client = (SSLSocket) sf.createSocket(this.ip, this.port);
        if(wantClientAuth) this.client.setWantClientAuth(true);
        String [] cipherSuite = this.client.getSupportedCipherSuites();
        String [] allowedCipherSuite = new String[8];
        int j=0;
        for (int i = 0; i < this.client.getSupportedCipherSuites().length; i++) {
            if(this.client.getSupportedCipherSuites()[i].contains("anon")){
                allowedCipherSuite[j] = this.client.getSupportedCipherSuites()[i];
                j++;
            }

        }


        this.client.setEnabledCipherSuites(allowedCipherSuite);

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

    public void sendMessage(String message) throws IOException {
        out.println(message);
        out.flush();
        System.out.println("Client" + message +"\n\n");

    }


    public int intRead() throws IOException {
        return  this.in.read();
    }

}
