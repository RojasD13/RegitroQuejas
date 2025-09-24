package com.uptc.edu.main.service;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;

public interface SendEmail {

    void sendEmail(HttpServletRequest request) throws IOException;

}
