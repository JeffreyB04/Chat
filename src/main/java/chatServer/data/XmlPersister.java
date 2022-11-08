package chatServer.data;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.MarshalException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
// lista de contactos de cada cliente
// para cada contacto una lista de mensajes
public class XmlPersister {
    private String path;
    private static XmlPersister theInstance;

    public static XmlPersister instance(){
        if (theInstance == null){
            theInstance =  new XmlPersister("Usuarios.xml");
        }
        return theInstance;
    }

    public XmlPersister(String p){
        path = p;
    }

    public Data load() throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
        FileInputStream is = new FileInputStream(path);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Data result = (Data) unmarshaller.unmarshal(is);
        is.close();
        return result;
    }

    public void store(Data d) throws Exception{
        JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
        FileOutputStream os = new FileOutputStream(path);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(d,os);
        os.flush();
        os.close();
    }
}
