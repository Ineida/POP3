package metier;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Message {
    private int sizeOctet;
    private String messageID;
    private ArrayList<String> to = new ArrayList<String> ();
    private String from;
    private String date;
    private String subject;
    private String message;

    public Message() {
        super();
    }

    public Message(String to, String from, String subject, String message) {
        this.to.add(to);
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.date = new Date().toString();
        this.sizeOctet =
                (this.to + this.from + this.message
                        + this.subject +this.date).length();
    }

    public Message(String message) {
        String[] messagesplit = message.split("\r\n");
        String line;
        boolean header = true;
        int i = 0;
        while (header){
                // le header et le corps sont separées par un une ligne "\r\n" detecter la ligne vide pour y remedier
                line = messagesplit[i];
                if (line.length() == 0 && i > 0) {
                    header = false;
                }

                int indexOfSeparator = line.indexOf(':');
                if (indexOfSeparator > 0 && header) {
                    String headerName = line.substring(0, indexOfSeparator).toUpperCase();
                    String headerContent = line.substring(indexOfSeparator + 1);
                    switch (headerName) {
                        case "DATE":
                            this.setDate(headerContent);
                            break;
                        case "FROM":
                            this.setFrom(headerContent);
                            break;
                        case "TO":
                            this.setTo(headerContent);
                            break;
                        case "SUBJECT":
                            this.setSubject(headerContent);
                            break;
                        case "MESSAGE-ID":
                            this.setMessageID(headerContent);
                            break;
                    }
                }
                i++;
            }
        this.setMessage(String.join("\r\n", Arrays.copyOfRange(messagesplit, i,messagesplit.length)));
        this.sizeOctet =
                (this.to + this.from + this.message
                        + this.subject +this.date).length();
    }

    public Message(String messageID, String to,
                   String from, Date date, String subject, String message) {
        this.messageID = messageID;
        this.to.add(to);
        this.from = from;
        this.date = date.toString();
        this.subject = subject;
        this.message = message;
        this.sizeOctet =
                (this.to + this.from + this.message
                        + this.subject +this.date).length();
    }

    public int getSizeOctet() {
        return sizeOctet;
    }

    public  String getMessageTitle() {
        String message = "";
        message += " From: " + this.getFrom() + "\r\n";
        message += " Date: " + this.getDate() + "\n";
        message += " Message-ID: " + this.getMessageID() ;
        message += " Subject: " + this.getSubject() ;

        return message;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getTo() {
        String to ="";
        for(String item : this.to){
            to += item +",";
        };
        return to;
    }

    public void setTo(String to) {
        this.to.add(to);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date.toString();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void readMessage(String messagePath) throws IOException {
        StringBuilder sbMessage = new StringBuilder();
        BufferedReader file = null;
        boolean header = true;

        try {
           file = new BufferedReader(new FileReader(messagePath));
            String line = file.readLine();
            while (line != null) {
                // le header et le corps sont separées par un une ligne "\r\n" detecter la ligne vide pour y remedier
                if(line.length() == 0) { header = false; }
                if(header) {
                    int indexOfSeparator = line.indexOf(':');
                    if(indexOfSeparator > 0) {
                        String headerName = line.substring(0, indexOfSeparator).toUpperCase();
                        String headerContent = line.substring(indexOfSeparator +1);
                        switch (headerName) {
                            case "DATE":
                                this.setDate(headerContent);
                                break;
                            case "FROM":
                                this.setFrom(headerContent);
                                break;
                            case "TO":
                                this.setTo(headerContent);
                                break;
                            case "SUBJECT":
                                this.setSubject(headerContent);
                                break;
                            case "MESSAGE-ID":
                                this.setMessageID(headerContent);
                                break;
                        }
                    }
                }
                else {
                    sbMessage.append("\n"+line +"\r");
                }
                line = file.readLine();

            }
            setMessage(String.valueOf(sbMessage));
        } catch (IOException notFound) {
                notFound.printStackTrace();
        } finally {
            if(file != null)
                file.close();
        }
    }

    public void saveMessage(String messagePath, String message) {
        File file = new File(messagePath);
        FileWriter fr = null;
        try{
            fr = new FileWriter(file);
            fr.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveMessage(String messagePath) {
        File file = new File(messagePath);
        FileWriter fr = null;
        try{
            fr = new FileWriter(file);
            fr.write(this.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        String message = "";
        message += "From: " + this.getFrom() + "\r\n";
        message += "To: " + this.getTo() + "\r\n";
        message += "Subject: " + this.getSubject() + "\r\n";
        message += "Date: " + this.getDate() + "\r\n";
        message += "Message-ID: " + this.getMessageID() + "\r\n";
        message += "\r\n";
        message += this.getMessage();
        return message;
    }
}
