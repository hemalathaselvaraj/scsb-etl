package org.recap.model.jaxb;

import org.recap.RecapConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pvsubrah on 6/21/16.
 */
public class JAXBHandler {

    private static final Logger logger = LoggerFactory.getLogger(JAXBHandler.class);

    private static JAXBHandler jaxbHandler;
    private Map<String, Unmarshaller> unmarshallerMap;
    private Map<String, Marshaller> marshallerMap;

    private JAXBHandler() {

    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static JAXBHandler getInstance() {
        if (null == jaxbHandler) {
            jaxbHandler = new JAXBHandler();
        }
        return jaxbHandler;
    }

    /**
     * Marshal string.
     *
     * @param object the object
     * @return the string
     */
    public String marshal(Object object) {
        StringWriter stringWriter = new StringWriter();
        try {
            Marshaller marshaller = getMarshaller(object.getClass());
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(object, stringWriter);
        } catch (JAXBException e) {
            logger.error(RecapConstants.ERROR,e);
        }
        return stringWriter.toString();
    }

    private Marshaller getMarshaller(Class cl) throws JAXBException {
        if (getMarshallerMap().containsKey(cl.getName())) {
            return getMarshallerMap().get(cl.getName());
        } else {
            JAXBContext jaxbContext = JAXBContextHandler.getInstance().getJAXBContextForClass(cl);
            Marshaller marshaller = jaxbContext.createMarshaller();
            getMarshallerMap().put(cl.getName(), marshaller);
        }
        return getMarshallerMap().get(cl.getName());
    }

    /**
     * Unmarshal object.
     *
     * @param content the content
     * @param cl      the cl
     * @return the object
     * @throws JAXBException the jaxb exception
     */
    public synchronized Object unmarshal(String content, Class cl) throws JAXBException  {
        Object object;
        Unmarshaller unmarshaller = getUnmarshaller(cl);
        object = unmarshaller.unmarshal(new StringReader(content));
        return object;
    }

    private Unmarshaller getUnmarshaller(Class cl) throws JAXBException {
        if (getUnmarshallerMap().containsKey(cl.getName())) {
            return getUnmarshallerMap().get(cl.getName());
        } else {
            JAXBContext jaxbContextForClass = JAXBContextHandler.getInstance().getJAXBContextForClass(cl);
            Unmarshaller unmarshaller = jaxbContextForClass.createUnmarshaller();
            getUnmarshallerMap().put(cl.getName(), unmarshaller);
        }
        return getUnmarshallerMap().get(cl.getName());
    }

    /**
     * Gets unmarshaller map.
     *
     * @return the unmarshaller map
     */
    public Map<String, Unmarshaller> getUnmarshallerMap() {
        if (null == unmarshallerMap) {
            unmarshallerMap = new HashMap<>();
        }
        return unmarshallerMap;
    }

    /**
     * Sets unmarshaller map.
     *
     * @param unmarshallerMap the unmarshaller map
     */
    public void setUnmarshallerMap(Map<String, Unmarshaller> unmarshallerMap) {
        this.unmarshallerMap = unmarshallerMap;
    }

    /**
     * Gets marshaller map.
     *
     * @return the marshaller map
     */
    public Map<String, Marshaller> getMarshallerMap() {
        if (marshallerMap == null) {
            marshallerMap = new HashMap<>();
        }
        return marshallerMap;
    }

    /**
     * Sets marshaller map.
     *
     * @param marshallerMap the marshaller map
     */
    public void setMarshallerMap(Map<String, Marshaller> marshallerMap) {
        this.marshallerMap = marshallerMap;
    }
}
