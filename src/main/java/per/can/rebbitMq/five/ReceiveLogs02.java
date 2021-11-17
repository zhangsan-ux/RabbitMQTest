package per.can.rebbitMq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import per.can.rebbitMq.utils.RabbitMqUtil;

public class ReceiveLogs02 {

    private static final String EXCHANGE_NAME = "logs";

 public static void main(String[] argv) throws Exception {
     Channel channel = RabbitMqUtil.getChannel("192.168.1.128","admin","123");
     //声明交换机
     channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
     //产生临时队列
     String queueName = channel.queueDeclare().getQueue();
     //交换机和队列绑定
     channel.queueBind(queueName,EXCHANGE_NAME,"1");
     System.out.println("等待接收消息,把接收到的消息打印在屏幕.....");

     DeliverCallback deliverCallback = (consumerTag, delivery) -> {
         String message = new String(delivery.getBody());
         System.out.println("ReceiveLogs02 控制台打印接收到的消息"+message);
     };

     channel.basicConsume(queueName, true, deliverCallback, cancelCallback->{});



 }
}
