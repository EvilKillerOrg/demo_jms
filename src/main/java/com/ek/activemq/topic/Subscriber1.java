package com.ek.activemq.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息订阅者1
 * 
 * @ClassName: Subscriber
 * @Description: TODO
 * @author: ek
 * @date: 2018年1月27日 上午2:39:41
 */
public class Subscriber1 {

  private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // 默认的连接用户名
  private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // 默认的连接密码
  private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL; // 默认的连接地址

  /* 创建订阅者 */
  public static void main(String[] args) throws InterruptedException {

    ConnectionFactory connectionFactory; // JMS连接工厂
    Connection connection = null; // JMS连接
    Session session = null; // JMS会话 接收订阅或者发布消息的线程
    Destination destination; // JMS消息的目的地
    MessageConsumer messageConsumer; // JMS消息订阅者

    // 实例化连接工厂
    connectionFactory = new ActiveMQConnectionFactory(Subscriber1.USERNAME, Subscriber1.PASSWORD, Subscriber1.BROKERURL);

    try {
      // 通过连接工厂获取连接
      connection = connectionFactory.createConnection();
      // 启动连接
      connection.start();
      // 创建session.参数1:订阅者不加事务,参数2:确认客户收到订阅消息的方式
      session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
      // 创建消息队列要和发布的消息队列名称对应
      destination = session.createTopic("FristTopic1");
      // 创建消息订阅者
      messageConsumer = session.createConsumer(destination);
      // 接收订阅的消息(注册消息监听,使用监听器,生产者生产出来就通知订阅者去拿)
      messageConsumer.setMessageListener(new Subscriber1Listener());
      while (true) {
        // 这不暂停一下订阅不上
        Thread.sleep(1000);
      }
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

}
