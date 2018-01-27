package com.ek.activemq.queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息消费者
 * 实际开发不用Receive,这种就不停的去接收(同步消费)
 * 应该使用监听器(异步消费)
 * 
 * @ClassName: ConsumerOfReceive
 * @Description: TODO
 * @author: ek
 * @date: 2018年1月27日 上午2:39:41
 */
public class ConsumerOfReceive {

  private static Logger log = LoggerFactory.getLogger(Producer.class);

  private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // 默认的连接用户名
  private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // 默认的连接密码
  private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL; // 默认的连接地址

  /* 创建消费者 */
  public static void main(String[] args) {

    ConnectionFactory connectionFactory; // JMS连接工厂
    Connection connection = null; // JMS连接
    Session session = null; // JMS会话 消费或者发送消息的线程
    Destination destination; // JMS消息的目的地
    MessageConsumer messageConsumer; // JMS消息消费者

    // 实例化连接工厂
    connectionFactory = new ActiveMQConnectionFactory(ConsumerOfReceive.USERNAME, ConsumerOfReceive.PASSWORD, ConsumerOfReceive.BROKERURL);

    try {
      // 通过连接工厂获取连接
      connection = connectionFactory.createConnection();
      // 启动连接
      connection.start();
      // 创建session.参数1:消费者不加事务,参数2:确认客户收到消息的方式
      session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
      // 创建消息队列要和生产的消息队列名称对应,目的地也一样
      destination = session.createQueue("FristQueue1");
      // 创建消息消费者
      messageConsumer = session.createConsumer(destination);
      // 接收消息(实际开发不用receive,这种就不停的去接收,应该使用监听器)
      while (true) {
        // 10000毫秒接收一次
        TextMessage textMessage = (TextMessage) messageConsumer.receive(10000);
        if (textMessage != null) {
          log.info("收到的消息: " + textMessage.getText());
        } else {
          break;
        }
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
