package com.n4systems.fieldid.selenium.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;
import org.subethamail.smtp.RejectException;
import org.subethamail.smtp.server.SMTPServer;

import com.n4systems.fieldid.selenium.SeleniumConfig;
import com.n4systems.fieldid.selenium.util.ConditionWaiter;
import com.n4systems.fieldid.selenium.util.Predicate;

public class MailServer implements MessageHandlerFactory {
	private static final Logger logger = Logger.getLogger(MailServer.class);
	
    private SMTPServer server;
    private List<MailMessage> messages;

    public MailServer(SeleniumConfig seleniumConfig) {
        messages = new ArrayList<MailMessage>();
        server = new SMTPServer(this);
        int mailPort = seleniumConfig.getMailServerPort();
        server.setPort(mailPort);
    }

    public void start() {
        server.start();
    }

    @Override
    public MessageHandler create(MessageContext messageContext) {
        return new MailMessageHandler();
    }

    class MailMessageHandler implements MessageHandler {

        private MailMessage message;

        public MailMessageHandler() {
            message = new MailMessage();
        }

        @Override
        public void from(String from) throws RejectException {
            message.setFrom(from);
        }

        @Override
        public void recipient(String recipient) throws RejectException {
            message.setTo(recipient);
        }

        @Override
        public void data(InputStream inputStream) throws RejectException, IOException {
            MailDataParser parser = new MailDataParser(inputStream);
            message.setBody(parser.getBody());
            message.setSubject(parser.getSubject());
        }

        @Override
        public void done() {
            addMessage(message);
        }
    }

    protected synchronized void addMessage(MailMessage message) {
        messages.add(message);
    }

    public synchronized int countMessages() {
        return messages.size();
    }

    public void waitForMessages(final int numMessages) {
        waitForMessages(numMessages, 30, 200);
    }

    public void waitForMessages(final int numMessages, int timeoutInSeconds, int pollIntervalMs) {
        new ConditionWaiter(new Predicate() {
            @Override
            public boolean evaluate() {
                return countMessages() >= numMessages;
            }
        }).run("Expected " + numMessages + " e-mail messages to arrive.", timeoutInSeconds, pollIntervalMs);
    }

    public synchronized List<MailMessage> getAndClearMessages() {
        List<MailMessage> copiedMessages = new ArrayList<MailMessage>(messages);
        messages = new ArrayList<MailMessage>();
        return copiedMessages;
    }

    public void stop() {
        this.server.stop();
    }

    public static void main(String[] args) {
        SeleniumConfig config = new SeleniumConfig();
        config.setMailServerPort(2525);
        MailServer server = new MailServer(config);
        server.start();

        try {
            server.waitForMessages(1, 60*60, 1000);

            MailMessage mailMessage = server.getAndClearMessages().get(0);
            logger.info(ToStringBuilder.reflectionToString(mailMessage));
        } finally {
            server.stop();
        }
    }
}
