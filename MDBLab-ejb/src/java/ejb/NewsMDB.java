/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Komp
 */
@JMSDestinationDefinition(name = "java:app/jms/NewsQueue", interfaceName = "javax.jms.Queue", resourceAdapter = "jmsra", destinationName = "NewsQueue")
        @MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:app/jms/NewsQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})


public class NewsMDB implements MessageListener {
    
@PersistenceContext
private EntityManager em;

    public NewsMDB() {
    }

    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;
        if (message instanceof TextMessage) {
            try {
                msg = (TextMessage) message;
                String txt = msg.getText();
                String[] txtDiv = txt.split("\\|", 2);
                NewsItem e = new NewsItem();
                e.setHeading(txtDiv[0]);
                e.setBody(txtDiv[1]);
                em.persist(e);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }

    }

}
