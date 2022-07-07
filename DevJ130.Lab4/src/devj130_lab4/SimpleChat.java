/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devj130_lab4;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.text.PlainDocument;

/**
 *
 * @author bezdetk0@mail.ru
 */
public class SimpleChat /*extends JFrame */ implements ISimpleChat {


    private Socket socket; //сокет для общения
    private ServerSocket serverSocket; // серверсокет
    private BufferedReader reader; // поток чтения из сокета
    private JTextArea chatArea;
    private JTextField messageField;
    private JFrame frame;
    private JButton exitButton;

    private String address;
    private int port;
    private String title;
//    private String outputText;
//    private String inputText;

    private JFrame startFrame;
    private JLabel infoLabel;

    private LaunchEnum typeOfLaunch;

    private LocalDateTime time;

    private PrintWriter printWriter;

    private boolean flag = true;

    public SimpleChat() {
    }

    ;
    
    public SimpleChat(LaunchEnum typeOfLaunch) {
        if (typeOfLaunch.equals(LaunchEnum.SERVER)) {
            title = "Сервер";
            new Thread(() -> {
                try {
                    mainFrame();
                    server();

                } catch (ChatException e) {
                    System.out.println("Ошибка #1: " + e.getMessage());
                }
            }).start();
        }

        if (typeOfLaunch.equals(LaunchEnum.CLIENT)) {

            title = "Клиент";

            clientStartFrame();

        }

    }

    @Override
    public void client() throws ChatException {
//        mainFrame();
        try {
//                        socket = new Socket("localhost", ISimpleChat.SERVER_PORT);
            socket = new Socket(address, port);
            System.out.println("Клиент создал сокет");
            getMessage();

        } catch (IOException ex) {
            infoLabel.setText("Неверный адрес или порт");
        }
    }

    @Override
    public void server() throws ChatException {
//        mainFrame();
        try {
            serverSocket = new ServerSocket(ISimpleChat.SERVER_PORT);
            socket = serverSocket.accept();
            System.out.println("Сервер запущен");
            System.out.println("есть соединение на сервере. Клиент подключился");
            getMessage();
        } catch (IOException e) {
            throw new ChatException(e.getMessage());
        }
    }

    @Override
    public String getMessage() throws ChatException {
        String msg = "";
        try {
            InputStreamReader isr = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(isr);
            System.out.println("Открыт входящий поток");
            while (true) {
                if (socket.isClosed()) break;
                msg = bufferedReader.readLine();
                if (msg != null) {
                    chatArea.append(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + " "
                            + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME)
                            + " <-- " + msg + "\n");
                }
                
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());;
        }
        return msg;
    }

    @Override
    public void sendMessage(String message) throws ChatException {
        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(message);
            printWriter.flush();
            chatArea.append(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) + " "
                    + LocalDateTime.now().format(DateTimeFormatter.ISO_TIME)
                    + " --> " + message + "\n");
            messageField.setText(null);
        } catch (IOException e) {
            throw new ChatException(e.getMessage());
        }
    }

    @Override
    public void close() throws ChatException {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.getOutputStream().close();
                socket.getInputStream().close();
                socket.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            throw new ChatException(e.getMessage());
        }
    }

    private void mainFrame() {
//        JFrame frame = new JFrame();
        frame = new JFrame();
        frame.setTitle(title);
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        frame.add(chatArea, BorderLayout.CENTER);
//        
        Container south = new Container();
        south.setLayout(new FlowLayout());
        frame.add(south, BorderLayout.SOUTH);
        messageField = new JTextField(60);
        messageField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("Нажата кнопка Enter");
                    try {
                        if (!messageField.getText().isEmpty() && messageField.getText() != null) {
                            sendMessage(messageField.getText());
                        }
                        System.out.println("Собщение ушло на сервер");

                    } catch (ChatException ex) {
                        System.out.println("Ошибка#1: " + ex.getMessage());
                    }

                    messageField.setText("");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        exitButton = new JButton("EXIT");
        exitButton.addActionListener((e) -> {

            frame.dispose();
            try {
                close();
            } catch (ChatException ex) {
                System.out.println(ex.getMessage());
            }

        });

        south.add(messageField);
        south.add(exitButton);

        frame.setVisible(true);
    }

    private void clientStartFrame() {
        startFrame = new JFrame();
        Container container;
        startFrame.setTitle("Соединение");
        startFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        startFrame.setSize(500, 170);
        startFrame.setResizable(false);

        JLabel titleLabel = new JLabel("Введите адрес и порт сервера");
        titleLabel.setSize(350, 15);
        infoLabel = new JLabel(" ");
        infoLabel.setSize(350, 15);
        JLabel addressLabel = new JLabel("Адрес:");
        JLabel portLabel = new JLabel("Порт:");
        JTextField addressField = new JTextField(20);
        JTextField portField = new JTextField(20);
        JButton buttonConnect = new JButton("Соединить");
        JButton buttonCancel = new JButton("Отмена");

        container = startFrame.getContentPane();
        GridLayout gridLayout = new GridLayout(2, 2, 5, 5);
        Container grid = new Container();
        grid.setLayout(new GridLayout(3, 2, 5, 5));

        startFrame.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(titleLabel);
        container.add(infoLabel);
        container.add(grid);
        grid.add(addressLabel);
        grid.add(addressField);
        grid.add(portLabel);
        grid.add(portField);
        grid.add(buttonConnect);
        grid.add(buttonCancel);

        PlainDocument digitDoc = (PlainDocument) portField.getDocument();
        digitDoc.setDocumentFilter(new DigitFilter());

        buttonCancel.addActionListener((e) -> {
            startFrame.dispose();
        });

        buttonConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addressField.getText().isEmpty() || portField.getText().isEmpty() || Integer.parseInt(portField.getText()) < 1) {
                    infoLabel.setText("Адрес и порт не могут быть пустыми. Порт должен быть > 0");
                } else {
                    port = Integer.parseInt(portField.getText());
                    address = addressField.getText();
                    new Thread(() -> {
                        try {
                            mainFrame();
                            client();
                        } catch (ChatException ex) {
                            Logger.getLogger(SimpleChat.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }).start();

                    startFrame.dispose();

                }
            }
        });
        startFrame.setVisible(true);
    }

}
