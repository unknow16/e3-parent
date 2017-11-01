package com.fuyi.e3.test;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext-activemq.xml"})
public class TestActiveMQ {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ActiveMQQueue activeMQQueue;

    @Autowired
    private ActiveMQTopic activeMQTopic;

    @Test
    public void testQueueSend() {
        jmsTemplate.send(activeMQQueue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {

                TextMessage textMessage = session.createTextMessage("hello234");
                return textMessage;
            }
        });
    }

    @Test
    public void testTopicSend() {
        jmsTemplate.send(activeMQTopic, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage("hellow topic");
                return textMessage;
            }
        });
    }

    @Test
    public void testQueueRead() throws Exception {
        System.in.read();
    }
}
