/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;

/**
 *
 * @author Christoffer
 */
public class EchoServer implements Runnable
{

    LocalDateTime date;
    DateTimeFormatter formatted;
    String dateFormatted;
    Scanner scan;
    ArrayList userList;
    StringBuilder sb;
    BufferedReader in;
    PrintWriter out;
    String echo;
    Socket s;
    String userName;
    Scanner checkUserName;
    boolean pendingUserName;
    Scanner msgPPL;
    ArrayList msgRecipients;
    EchoClient ec;

    public EchoServer(Socket s)
    {
        ec = new EchoClient();
        msgRecipients = new ArrayList();
        pendingUserName = true;
        date = LocalDateTime.now();
        formatted = DateTimeFormatter.ofPattern("E LLL dd HH:mm:ss yyyy");
        userList = new ArrayList();
        sb = new StringBuilder();
        this.s = s;
    }

    public static void main(String[] args) throws IOException
    {
        String ip = "localhost";
        int port = 4321;

        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(ip, port));

        while (true)
        {
            EchoServer e = new EchoServer(ss.accept());
            Thread t1 = new Thread(e);
            t1.start();

        }
    }

    @Override
    public void run()
    {

        try
        {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);

            dateFormatted = date.format(formatted);
            //LAVER NY BRUGER (MAX ET GODKENDT TRY)
            while (pendingUserName)
            {
                out.println("Please enter a username like this: USER#'your_name'");
                userName = in.readLine();
                checkUserName = new Scanner(userName);
                checkUserName.useDelimiter("#");
                while (checkUserName.hasNext())
                {
                    String a = checkUserName.next();
                    String b = checkUserName.next();

                    if (a.equals("USER"))
                    {
                        ec.setUserName(b);
                        userList.add(b);
                        out.println("your username is: " + b);
                        out.println("Online users: " + userList.toString());

                        pendingUserName = false;
                    }
                }
            }

            while (true)
            {
                String first = "";
                String middle = "";
                String last = "";
                echo = in.readLine();
                scan = new Scanner(echo);
                scan.useDelimiter("#");
                String msgName = "";
                while (scan.hasNext())
                {
                    first = scan.next();
                    middle = scan.next();
                    last = scan.next();
                }
                switch (first)
                {
                    case "MSG":
                        msgPPL = new Scanner(middle);
                        msgPPL.useDelimiter(",");

                        while (msgPPL.hasNext())
                        {
                            msgName = msgPPL.next();
                            msgRecipients.add(msgName);
                        }

                        if (userList.contains(middle))
                        {
                            out.println(last);
                        }

                        if (middle.equals("*"))
                        {
                            out.println(last);
                        }

                        break;
//                    case "LOWER":
//                        out.println(word.toLowerCase());
//                        break;
//                    case "":
//                        String toRev = new StringBuilder(word).reverse().toString();
//                        char toUpper = toRev.charAt(0);
//                        String charToUpper = toUpper + "";
//                        String newWord = charToUpper.toUpperCase() + toRev.substring(1, toRev.length());
//                        out.println(newWord);
//                        break;
//                    case "TRANSLATE":
//                        Document doc = Jsoup.connect("http://translate.reference.com/danish/english/" + word).get();
//                        Elements translated = doc.select(".translate-module .target-area-container textarea");
//                        out.println(translated.text());
//                        break;
                }
//                System.out.println(echo);
//                out.println(dateFormatted);
            }
        } catch (IOException ex)
        {
            Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
