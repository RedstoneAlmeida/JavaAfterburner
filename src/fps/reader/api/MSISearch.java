package fps.reader.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

public class MSISearch {

    private String xml;

    private String user;
    private String password;

    public MSISearch(String user, String password){
        this.user = user;
        this.password = password;
    }


    public NodeList getObjectData(String name){
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            this.receiveXML("http://127.0.0.1:96/mahm");
        } catch (IOException e) {
            e.printStackTrace();
            try {
                this.receiveXML("http://127.0.0.1:96/mahm");
            } catch (IOException e1){
                e1.printStackTrace();
            }
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
                    if(eElement.getElementsByTagName(name) != null)
                        return eElement.getElementsByTagName(name);
                    return null;
                    //System.out.println(count);
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void receiveXML(String urlSt) throws MalformedURLException, IOException {
        URL url = new URL(urlSt);
        Authenticator.setDefault(new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MSISearch.this.user, MSISearch.this.password.toCharArray());
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
