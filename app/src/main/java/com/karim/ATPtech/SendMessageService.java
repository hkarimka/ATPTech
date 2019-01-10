package com.karim.ATPtech;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.File;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMessageService extends AsyncTask<Void,Void,Boolean> {

    public interface AsyncResponse {
        void processFinish(boolean isMessageSended);
    }

    public AsyncResponse delegate = null;

    private String SUBJECT = "Логи";
    private String MESSAGE = "Файл с логами устройства";
    private String FILENAME = "";
    private boolean isFileNeed = true;

    private String host, port, fromEmail, fromPassword, toEmail;
    private Context context;
    private ProgressDialog progressDialog;

    public SendMessageService(Context context, String host, String port, String fromEmail, String fromPassword, String toEmail, AsyncResponse delegate) {
        this.delegate = delegate;
        this.context = context;
        this.host = host;
        this.port = port;
        this.fromEmail = fromEmail;
        this.fromPassword = fromPassword;
        this.toEmail = toEmail;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isFileNeed) {
            File file = new File(FILENAME);
            if (!file.exists()) {
                cancel(true);
                Toast.makeText(context, "Файл не существует", Toast.LENGTH_LONG).show();
                return;
            }
        }

        progressDialog = ProgressDialog.show(context, "Отправка сообщения", "Подождите...", false, false);
    }

    @Override
    protected void onPostExecute(Boolean isMessageSended) {
        super.onPostExecute(isMessageSended);
        delegate.processFinish(isMessageSended);
        progressDialog.dismiss();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        Transport transport = null;
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.socketFactory.port", port);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", port);

            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        //Authenticating the password
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(fromEmail, fromPassword);
                        }
                    });

            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(fromEmail));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            mm.setSubject(SUBJECT);
            Multipart multipart = new MimeMultipart();

            if(isFileNeed) {
                MimeBodyPart attachementPart = new MimeBodyPart();
                attachementPart.attachFile(new File(FILENAME));
                multipart.addBodyPart(attachementPart);
            }

            MimeBodyPart messagePart = new MimeBodyPart();
            messagePart.setContent(MESSAGE, "text/html; charset=utf-8");
            multipart.addBodyPart(messagePart);

            mm.setContent(multipart);
            transport = session.getTransport("smtp");
            transport.connect(host, Integer.valueOf(port), fromEmail, fromPassword);
            transport.sendMessage(mm, mm.getRecipients(Message.RecipientType.TO));
            transport.close();
            return true;
        } catch (Exception ex) {
            progressDialog.dismiss();
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (transport != null)
                    transport.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setFileNeed(boolean fileNeed) {
        isFileNeed = fileNeed;
    }

    public void setSUBJECT(String SUBJECT) {
        this.SUBJECT = SUBJECT;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }
}
