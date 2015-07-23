package jparsec.vo;

public class MailElementTest {
    /**
     * For unit testing only.
     *
     * @param args Not used.
     */
    public static void main(final String[] args) {
        MailElement m = new MailElement();
        m.name = "JPARSEC";
        m.login = "me@somewhere";
        m.password = "mypassword";
        m.smtpServer = "smtp.gmail.com";
        m.recipients = new String[] { "destination@somewhere" };
        m.subject = "Hellow from Java";
        m.content = "<html><head><title>" +
                m.subject +
                "</title></head><body><h1>" +
                m.subject +
                "</h1><p>This is a test of sending an HTML e-mail through Java.";
        // m.addAttachment("/home/alonso/test.png");
        // m.content += "I have even included and image!!!!!!!!";
        m.content += "</body></html>";
        m.contentType = MailElement.CONTENT_TYPE_HTML_TEXT;

        try {
            m.send();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
