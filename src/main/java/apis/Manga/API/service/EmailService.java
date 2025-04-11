package apis.Manga.API.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@Data
public class EmailService {

    private final String webadress = "https://fastglobeit.de";
    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("fastglobeit@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }

    public void sendHtmlEmailToAzubisCausedByNewReports(String email, String name, String von, String bis) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String subject = "Neuer Bericht in deinem Ausbildungscockpit";
            String html = "<h1>Hallo " + name + ", </h1>" +
                    "<p>Du hast einen neuen Bericht in deinem Ausbildungscockpit. " +
                    "Dieser geht vom " + von + " bis zum " + bis + ". <br><br></p>" +
                    "<p>Besuche jetzt das Ausbildungscockpit und bearbeite deinen Wochenbericht auf</p>" +
                    "<a href=\"https://" + webadress + "\">" + webadress + " <br><br><br></a>" +
                    "<i>Diese Email wurde automatisch versendet.</i>";


            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(html, true);
            javaMailSender.send(message);
            System.out.println("Email an " + email + " wurde versendet");
        } catch (MessagingException e) {
            System.out.println("Email an " + email + " konnte nicht versendet werden: " + e);
        }

    }
}

