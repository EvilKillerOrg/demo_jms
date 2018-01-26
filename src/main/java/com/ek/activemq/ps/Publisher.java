package com.ek.activemq.ps;

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
 * 消息发布者
 * Publish/Subscribe 模式需要先订阅,才能收到发布的消息,先运行订阅者
 * 
 * @ClassName: Publisher
 * @Description: TODO
 * @author: ek
 * @date: 2018年1月26日 下午11:09:29
 */

public class Publisher {

  private static Logger log = LoggerFactory.getLogger(Publisher.class);

  private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // 默认的连接用户名
  private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // 默认的连接密码
  private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL; // 默认的连接地址
  private static final int SENDNUM = 10; // 发布的消息数量

  /* 创建发布者 */
  public static void main(String[] args) {

    ConnectionFactory connectionFactory; // JMS连接工厂
    Connection connection = null; // JMS连接
    Session session; // JMS会话 接收订阅或者发布消息的线程
    Destination destination; // JMS消息的目的地
    MessageProducer messageProducer; // JMS消息发布者

    // 实例化连接工厂
    connectionFactory = new ActiveMQConnectionFactory(Publisher.USERNAME, Publisher.PASSWORD, Publisher.BROKERURL);

    try {
      // 通过连接工厂获取连接
      connection = connectionFactory.createConnection();
      // 启动连接
      connection.start();
      // 创建session.参数1:是否加事务,参数2:确认客户收到订阅消息的方式
      session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
      // 创建消息主题
      destination = session.createTopic("FristTopic1");
      // 创建消息发布者
      messageProducer = session.createProducer(destination);
      // 发布消息
      Publisher.sendMessage(session, messageProducer);
      // session加了事务,所以这要提交一下才会真正发布.
      session.commit();
    } catch (JMSException e) {
      e.printStackTrace();
    } finally {
      if (connection != null) {
        try {
          // 关闭连接(严格意义上所有的都要关闭)
          connection.close();
        } catch (JMSException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /* 发布消息 */
  public static void sendMessage(Session session, MessageProducer messageProducer) throws JMSException {
    for (int i = 0; i < Publisher.SENDNUM; i++) {
      TextMessage message = session.createTextMessage("ActiveMQ消息-" + i);
      log.info("发布的消息: ActiveMQ消息-" + i);
      messageProducer.send(message); // 发布消息
    }

  }

}
