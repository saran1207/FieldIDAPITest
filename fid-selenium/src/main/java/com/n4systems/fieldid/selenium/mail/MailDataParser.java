package com.n4systems.fieldid.selenium.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MailDataParser {

    private static final String SUBJECT_HEADER = "Subject: ";

    private String subject;
    private String body;

    public MailDataParser(InputStream data) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(data));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(SUBJECT_HEADER)) {
                    subject = line.substring(SUBJECT_HEADER.length());
                } else if (line.startsWith("-----")) {
                    parseBody(br);
                    break;
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void parseBody(BufferedReader br) throws IOException {
        // Consume body headers
        while (!br.readLine().equals("")) { }
        StringBuffer bodyBuffer = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("-----")) {
                break;
            }
            bodyBuffer.append(line).append("\n");
        }
        body = bodyBuffer.toString();
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

}
