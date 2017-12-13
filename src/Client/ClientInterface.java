package Client;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Graphical User Interface
 * <p>This class contains a graphical user interface that allows de user
 * to control the chat in an easy and visual manner. It doesn't contain any of the logic that
 * makes the chat work, it contains only a interface for the user to control the chat; all the logic is inside EchoClient.</p>
 * <p>
 * <p>In the top there is the username with a button to update it and the ip of the host server.</p>
 * <p>
 * <p>In the middle we have all the messages sent by the users or by other users.</p>
 * <p>
 * <p>In the bottom there is a text field that allows de users to write the message and to send it
 * by clicking enter or by clicking on the button send</p>
 */
public class ClientInterface implements ActionListener {
    private JFrame frame;
    public JTextArea contentArea;
    public JTextField txt_message;
    private JTextField txt_host;
    private JTextField txt_username;
    private JButton btn_send;
    private JButton btn_setUsername;
    private JPanel northPanel;
    private JPanel southPanel;
    private JScrollPane rightPanel;
    private EchoClient client;
    private final Color COLOR_CHAT = Color.BLUE;

    /**
     * Constructor of ClientInterface
     * it constructs all the graphical interface
     *
     * @param client   client connected to the chat
     * @param host     ip of the host
     * @param username username of the user using the chat
     */
    public ClientInterface(EchoClient client, String host, String username) {
        frame = new JFrame("Client");
        this.client = client;

        //frame.setIconImage(Toolkit.getDefaultToolkit().createImage(Client.class.getResource("qq.png")));
        //frame.setIconImage(Toolkit.getDefaultToolkit().createImage(Server.class.getResource("qq.png")));
        contentArea = new JTextArea();
        contentArea.setEditable(false);
        contentArea.setForeground(COLOR_CHAT);


        txt_message = new JTextField();
        txt_message.addActionListener(this);
        txt_username = new JTextField(username);
        txt_username.addActionListener(this);

        txt_host = new JTextField(host);
        txt_host.setEditable(false);

        btn_send = new JButton("send");
        btn_send.addActionListener(this);
        btn_setUsername = new JButton("Set username:");
        btn_setUsername.addActionListener(this);


        southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(new TitledBorder("write"));
        southPanel.add(txt_message, "Center");
        southPanel.add(btn_send, "East");

        contentArea.setText(null);
        rightPanel = new JScrollPane(contentArea);
        rightPanel.setBorder(new TitledBorder("message"));
        northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 4));
        northPanel.add(btn_setUsername);
        northPanel.add(txt_username);
        northPanel.add(new JLabel("IP host:"));
        northPanel.add(txt_host);


        frame.setLayout(new BorderLayout());
        frame.add(northPanel, "North");
        frame.add(contentArea, "Center");
        frame.add(southPanel, "South");
        frame.setSize(600, 400);

        int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
        frame.setLocation((screen_width - frame.getWidth()) / 2,
                (screen_height - frame.getHeight()) / 2);


        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        contentArea.requestFocus();

    }

    /**
     * Prints a message in the content area so the user can read it followed by an endline
     *
     * @param msg message to be printed
     */
    public void printMessage(String msg) {

        contentArea.append(msg + "\n");
        contentArea.setCaretPosition(contentArea.getText().length() - 1);

    }

    /**
     * This method contains all the "reactions" of the chat to the user orders.
     * It sends a message when the user wants to send a message.
     * It changes the username when the user wants to change its username.
     *
     * @param e event that called actionPerformed
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        //send message
        if (o == btn_send || o == txt_message) {
            client.SendMessage(txt_message.getText());
            txt_message.setText("");
        //change username
        } else if (o == btn_setUsername || o == txt_username) {
            client.setName(txt_username.getText());
        }
        //put the cursor in the txt_message field
        txt_message.requestFocus();

    }


}
