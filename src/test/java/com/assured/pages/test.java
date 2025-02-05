package com.assured.pages;

import com.manybrain.mailinator.client.MailinatorClient;
import com.manybrain.mailinator.client.message.*;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    public static void main(String[] args) {
        MailinatorClient mailinatorClient = new MailinatorClient("947fc29e9d3b4c4b80be0e65f27fd8db");
        Inbox inbox = mailinatorClient.request(new GetInboxRequest("private"));
        List<Message> messages = inbox.getMsgs();

        for (Message m : messages) {
            String subject = m.getSubject();
            String messageId = m.getId(); // Fetching the message ID
            List<Part> parts = m.getParts();

            // Extract URL from the subject
            String url = extractUrlFromSubject(subject);
            if (url != null) {
                System.out.println("Message ID: " + messageId);
                System.out.println("Extracted URL: " + url);
            } else {
                System.out.println("Message ID: " + messageId);
                System.out.println("No URL found in subject: " + subject);
            }
        }

        List<Links> links = Collections.singletonList(mailinatorClient.request(
                new GetLinksRequest("private", "abc", "abc-1738505275-0961552951903")));
        System.out.println(links);
    }

    private static String extractUrlFromSubject(String subject) {
        // Regular expression to match URLs
        String urlRegex = "(https?://[\\w-]+(\\.[\\w-]+)+(/\\S*)?)";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher matcher = pattern.matcher(subject);

        if (matcher.find()) {
            return matcher.group(0); // Return the first matched URL
        }
        return null; // Return null if no URL is found
    }
}