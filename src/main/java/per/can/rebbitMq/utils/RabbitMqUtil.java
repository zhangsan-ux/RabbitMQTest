package per.can.rebbitMq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqUtil {


    //得到一个连接的 channel
    public static Channel getChannel(String host,String rabbitMqUserName,String rabbitMqPwd) throws Exception{
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername(rabbitMqUserName);
        factory.setPassword(rabbitMqPwd);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }
}
