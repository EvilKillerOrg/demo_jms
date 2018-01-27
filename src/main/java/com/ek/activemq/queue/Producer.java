package com.ek.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息生产者
 * p2p模式是先生产消息再消费消息,先运行生产者
 * 
 * @ClassName: Producer
 * @Description: TODO
 * @author: ek
 * @date: 2018年1月26日 下午11:09:29
 */

public class Producer {

  private static Logger log = LoggerFactory.getLogger(Producer.class);

  private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // 默认的连接用户名
  private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // 默认的连接密码
  private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL; // 默认的连接地址
  private static final int SENDNUM = 10; // 发送的消息数量

  /* 创建生产者 */
  public static void main(String[] args) {

    ConnectionFactory connectionFactory; // JMS连接工厂
    Connection connection = null; // JMS连接
    Session session = null; // JMS会话 消费或者发送消息的线程
    Destination destination; // JMS消息的目的地
    MessageProducer messageProducer; // JMS消息生产者

    // 实例化连接工厂
    connectionFactory = new ActiveMQConnectionFactory(Producer.USERNAME, Producer.PASSWORD, Producer.BROKERURL);
    //connectionFactory = new ActiveMQConnectionFactory("tcp://10.0.0.77:61616");
     
    try {
      // 通过连接工厂获取连接
      connection = connectionFactory.createConnection();
      // 启动连接
      connection.start();
      // 创建session.参数1:是否加事务,参数2:确认客户收到消息的方式
      session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
      // 创建消息队列(目的地是Queue,目的地一把指Queue或Topic)
      destination = session.createQueue("FristQueue1");
      // 创建消息生产者
      messageProducer = session.createProducer(destination);
      // 发送消息
      Producer.sendMessage(session, messageProducer);
      // session加了事务,所以这要提交一下才会真正发送.
      session.commit();
    } catch (JMSException e) {
      e.printStackTrace();
    } finally {
      if (session != null) {
        try {
          session.close();
        } catch (JMSException e) {
          e.printStackTrace();
        }
      }
      if (connection != null) {
        try {
          connection.close();
        } catch (JMSException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /* 发送消息 */
  public static void sendMessage(Session session, MessageProducer messageProducer) throws JMSException {
    for (int i = 0; i < Producer.SENDNUM; i++) {
      TextMessage message = session.createTextMessage("ActiveMQ消息-" + i);
      log.info("发送的消息: ActiveMQ消息-" + i);
      messageProducer.send(message); // 发送消息
    }

  }

}
