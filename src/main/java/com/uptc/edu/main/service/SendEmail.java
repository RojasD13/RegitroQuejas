package com.uptc.edu.main.service;

import java.io.IOException;

public interface SendEmail {

    void sendEmail(String ip, String httpMethod, String requestUri) throws IOException;

}
