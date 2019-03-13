package fps.reader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FPSCounter {

    private String xml;
    private FPSViewer viewer;
    private Map<Long, Long> media = new HashMap<>();
    private long atualId = 0;
    private long lastMedia = 0;

    private int lastClear = 30;

    private int maximum = 0;

    private FPSEntry entry;

    public FPSCounter(String xml){
        this.xml = xml;
        entry = new FPSEntry();
        if(this.xml.isEmpty()) {
            try {
                this.sugaHTML("http://127.0.0.1:96/mahm");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.viewer = new FPSViewer();
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("FPSViewer");
        frame.setContentPane(viewer.getPanel1());
        //frame.setOpacity(0.1f);
        //frame.setBackground(new Color(0f, 0f, 0f, 0f));
        this.viewer.getPanel1().setSize(new Dimension(900, 300));
        this.viewer.getPanel1().setPreferredSize(new Dimension(900, 300));

        this.viewer.getProgressBar1().setFont(new Font("Calibri", Font.BOLD, 90));
        this.viewer.getProgressBar1().setStringPainted(true);
        this.viewer.getProgressBar1().setForeground(Color.GREEN);

        this.viewer.getProgressBar2().setFont(new Font("Calibri", Font.BOLD, 90));
        this.viewer.getProgressBar2().setStringPainted(true);
        this.viewer.getProgressBar2().setForeground(Color.GREEN);

        this.viewer.getGpuvram().setFont(new Font("Calibri", Font.BOLD, 50));
        this.viewer.getGpuvram().setStringPainted(true);
        this.viewer.getGpuvram().setForeground(Color.GREEN);

        this.viewer.getTemperature().setFont(new Font("Calibri", Font.BOLD, 50));
        this.viewer.getTemperature().setStringPainted(true);
        this.viewer.getTemperature().setForeground(Color.GREEN);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        this.init();
    }

    public void uiDetectors(){
        JProgressBar fpsview = this.viewer.getProgressBar1();
        fpsview.setMaximum(maximum);
        fpsview.setString("FPS: " + entry.getFps());
        fpsview.setValue(entry.getFps());

        JProgressBar gpuUseview = this.viewer.getProgressBar2();
        gpuUseview.setMaximum(100);
        gpuUseview.setValue(entry.getGpuUse());
        gpuUseview.setString("GPU " + entry.getGpuUse() + "%");

        JProgressBar gpuVram = this.viewer.getGpuvram();
        gpuVram.setMaximum(4096);
        gpuVram.setValue(entry.getVram());
        gpuVram.setString("VRAM: " + entry.getVram());

        JProgressBar gpuTemp = this.viewer.getTemperature();
        gpuTemp.setMaximum(100);
        gpuTemp.setValue(entry.getGpuTemp());
        gpuTemp.setString("GPU Temp: " + entry.getGpuTemp());
    }

    public void manualUpdate(FPSEntry entry){
        JProgressBar fpsview = this.viewer.getProgressBar1();
        fpsview.setMaximum(maximum);
        fpsview.setString("FPS: " + entry.getFps());
        fpsview.setValue(entry.getFps());

        JProgressBar gpuUseview = this.viewer.getProgressBar2();
        gpuUseview.setMaximum(100);
        gpuUseview.setValue(entry.getGpuUse());
        gpuUseview.setString("GPU " + entry.getGpuUse() + "%");

        JProgressBar gpuVram = this.viewer.getGpuvram();
        gpuVram.setMaximum(4096);
        gpuVram.setValue(entry.getVram());
        gpuVram.setString("VRAM: " + entry.getVram());

        JProgressBar gpuTemp = this.viewer.getTemperature();
        gpuTemp.setMaximum(100);
        gpuTemp.setValue(entry.getGpuTemp());
        gpuTemp.setString("GPU Temp: " + entry.getGpuTemp() + "º");
    }

    public void init(){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        while (true) {
            this.uiDetectors();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                this.sugaHTML("http://127.0.0.1:96/mahm");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Document doc = (Document) dBuilder.parse(new InputSource(new StringReader(this.xml)));

                //System.out.println("Root do elemento: " + doc.getDocumentElement().getNodeName());
                NodeList nList = doc.getElementsByTagName("HardwareMonitorEntries");

                //System.out.println("----------------------------");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    //System.out.println("\nElemento corrente :" + nNode.getNodeName());
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        //System.out.println("Id : " + (temp + 1));
                        //System.out.println("Primeiro nome.: " + eElement.getElementsByTagName("HardwareMonitorEntry").item(17).getFirstChild().getTextContent());
                        String fpsCount = eElement.getElementsByTagName("HardwareMonitorEntry").item(17).getChildNodes().item(5).getTextContent();
                        String gpuUse = eElement.getElementsByTagName("HardwareMonitorEntry").item(1).getChildNodes().item(5).getTextContent();
                        String gpuVram = eElement.getElementsByTagName("HardwareMonitorEntry").item(3).getChildNodes().item(5).getTextContent();
                        String gpuTemp = eElement.getElementsByTagName("HardwareMonitorEntry").item(0).getChildNodes().item(5).getTextContent();
                        try {
                            float format = (float) Double.parseDouble(fpsCount);
                            int fps = (int) format;
                            if(fps > maximum){
                                maximum = fps;
                            }

                            int gpu = (int) Double.parseDouble(gpuUse);
                            int vram = (int) Double.parseDouble(gpuVram);
                            int tempr = (int) Double.parseDouble(gpuTemp);

                            entry.setFps(fps);
                            entry.setGpuUse(gpu);
                            entry.setVram(vram);
                            entry.setGpuTemp(tempr);

                            //System.out.println(entry.toString());

                            media.put(atualId++, (long) fps);
                        } catch (NumberFormatException e){

                        }
                        //System.out.println(count);
                    }
                }
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            long sum = 0;
            long count = 0;
            for(long key : media.keySet()){
                count++;
                sum += media.get(key);
            }
            if(count <= 0){
                this.lastMedia = 0;
            } else {
                this.lastMedia = sum / count;
                entry.setMedia((int) this.lastMedia);
            }
            lastClear--;
            if(lastClear <= 0){
                media.clear();
                atualId = 0;
                lastClear = 30;
                maximum = 0;
            }
        }
    }

    public void sugaHTML(String urlSt) throws MalformedURLException, IOException {
        URL url = new URL(urlSt);
        Authenticator.setDefault(new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("MSIAfterburner", "17cc95b4017d496f82".toCharArray());
            }
        });

        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String linha = "";
        String xmlfinal = "";
        while ((linha = reader.readLine()) != null) {
            xmlfinal += linha;
        }
        reader.close();
        this.xml = xmlfinal;
    }

}
