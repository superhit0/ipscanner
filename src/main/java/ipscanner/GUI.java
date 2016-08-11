package ipscanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import java.io.*;

// An AWT program inherits from the top-level container java.awt.Frame
public class GUI extends Frame implements ActionListener {
    private Label startIP,stopIP;    // Declare a Label component
    private JTextArea ipList;
    private TextField startIPfield,stopIPfield; // Declare a TextField component
    private Button scan;   // Declare a Button component
    private int count = 0;     // Counter's value

    // Constructor to setup GUI components and event handlers
    public GUI() {
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        // "super" Frame (a Container) sets its layout to FlowLayout, which arranges
        // the components from left-to-right, and flow to next row from top-to-bottom.

        Panel query=new Panel();
        query.setLayout(new FlowLayout());

        startIP = new Label("Start IP:");  // construct the Label component
        query.add(startIP);                    // "super" Frame adds Label

        startIPfield = new TextField("0.0.0.0", 10); // construct the TextField component
        query.add(startIPfield);                     // "super" Frame adds TextField

        stopIP= new Label("Stop IP:");
        query.add(stopIP);

        stopIPfield = new TextField("0.0.0.0",10);
        query.add(stopIPfield);

        scan = new Button("Scan");   // construct the Button component
        query.add(scan);                    // "super" Frame adds Button
        add(query);

        ipList= new JTextArea("IPs Taken::");
        ipList.setRows(20);
        ipList.setColumns(60);
        add(ipList);

        scan.addActionListener(this);
        // scan is the source object that fires ActionEvent when clicked.
        // The source add "this" instance as an ActionEvent listener, which provides
        //  an ActionEvent handler called actionPerformed().
        // Clicking scan invokes actionPerformed().

        setTitle("IP Scanner");  // "super" Frame sets its title
        setSize(250, 100);        // "super" Frame sets its initial window size
        JScrollPane scroll = new JScrollPane (ipList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);

        // For inspecting the components/container objects
        // System.out.println(this);
        // System.out.println(startIP);
        // System.out.println(startIPfield);
        // System.out.println(scan);

        setVisible(true);         // "super" Frame shows
        setSize(1280,720);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // System.out.println(this);
        // System.out.println(startIP);
        // System.out.println(startIPfield);
        // System.out.println(scan);
    }

    // The entry main() method
    public static void main(String[] args) {
        // Invoke the constructor to setup the GUI, by allocating an instance
        GUI app = new GUI();
        // or simply "new GUI();" for an anonymous instance
    }

    // ActionEvent handler - Called back upon button-click.

    public void actionPerformed(ActionEvent evt) {
        String[] env = {"PATH=/bin:/usr/bin/"};
        File ipscan=null;
        try {
            java.util.jar.JarFile jar = new java.util.jar.JarFile(new java.io.File(GUI.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName());
            ipscan=new File("ipscan.sh");
            java.io.InputStream is = jar.getInputStream(jar.getEntry("ipscan.sh")); // get the input stream
            java.io.FileOutputStream fos = new java.io.FileOutputStream(ipscan);
            while (is.available() > 0) {  // write contents of 'is' to 'fos'
                fos.write(is.read());
            }
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        java.util.List<String> cmd = new java.util.ArrayList<String>();
        cmd.add("bash");
        cmd.add(ipscan.getAbsolutePath());
        for(String a:startIPfield.getText().split("\\."))
            cmd.add(a);
        for(String a:stopIPfield.getText().split("\\."))
            cmd.add(a);
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            Process process = pb.start();
            String out = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            for (String line = in.readLine(); line != null; line = in.readLine())
                out += "\n" + line;
            ipList.append(out);
            System.out.println(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}