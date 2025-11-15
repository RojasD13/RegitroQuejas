package com.uptc.edu.main;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
// import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;

// import com.uptc.edu.main.service.ResendEmailService;

@Component
@Profile("dev")
public class EmailTest implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }

    /* private static final Logger logger = LoggerFactory.getLogger(EmailTest.class);

    private final ResendEmailService emailService;

    @Autowired
    public EmailTest(ResendEmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void run(String... args) throws Exception {        
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setMethod("GET");
        request.setRequestURI("/");
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("User-Agent", "EntidadPrueba");

        emailService.sendEmail(request);

        logger.info("Email de prueba disparado (profile=dev). Revisa logs y la bandeja del admin configurado en app.admin.email.");
    } */
}
