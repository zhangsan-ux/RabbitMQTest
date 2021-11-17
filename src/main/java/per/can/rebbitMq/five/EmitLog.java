package per.can.rebbitMq.five;

import com.rabbitmq.client.Channel;
import per.can.rebbitMq.utils.RabbitMqUtil;

import java.util.Scanner;

public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtil.getChannel("192.168.1.128","admin","123");
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入信息");
        while (sc.hasNext()) {
            String message = sc.nextLine();
            channel.basicPublish(EXCHANGE_NAME, "1", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息" + message);
        }

    }
}
