package backend.academy.diplom.services.auth;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    private final String passwordTitle = "Пользователь зарегистрирован ProTim";
    private final String passwordEmailBody = "Вы были зарегистрированы в сервисе ПроТим." +
            "\n\nВаши данные для входа:\nEmail: %s\nТелефон: %s\nПароль: %s";

    private final String passwordResetTitle = "Восстановление пароля ProTim";
    private final String resetPasswordEmailBody = "Для восстановления пароля перейдите по ссылке:\n"
            + "%s";

    @Value("${spring.mail.username}")
    private String mailFrom;

    public void sendPasswordEmail(String email, String password,
                                  String phoneNumber) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(passwordTitle);
        message.setText(String.format(passwordEmailBody, email, phoneNumber, password));
        message.setFrom(mailFrom);
        mailSender.send(message);
    }

    public void sendResetPasswordToken(String email, String token) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Ссылка для сброса пароля");
            helper.setFrom(mailFrom);

            String resetLink = "https://www.protimrf.com/reset-password?token=" + token+"&"+"mail="+email;

            String content = "<p>Здравствуйте,</p>" +
                    "<p>Для сброса пароля, пожалуйста, перейдите по следующей ссылке:</p>" +
                    "<a href=\"" + resetLink + "\">Сбросить пароль</a>" +
                    "<p>С уважением,<br/>Команда Protim</p>";

            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
