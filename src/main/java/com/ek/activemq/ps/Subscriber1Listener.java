package com.ek.activemq.ps;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订阅者1 消息监听器
 * 
 * @ClassName: SubscriberListener
 * @Description: TODO
 * @author: ek
 * @date: 2018年1月27日 上午3:01:58
 */
public class Subscriber1Listener implements MessageListener {

  private Logger log = LoggerFactory.getLogger(Subscriber1Listener.class);

  // 消息监听
  @Override
  public void onMessage(Message msg) {
    try {
      TextMessage message = (TextMessage) msg;
      log.info("订阅者1 收到的消息: " + message.getText());
    } catch (JMSException e) {
      e.printStackTrace();
    }
  }

}
