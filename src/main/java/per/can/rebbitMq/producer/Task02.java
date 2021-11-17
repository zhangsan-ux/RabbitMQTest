package per.can.rebbitMq.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import per.can.rebbitMq.utils.RabbitMqUtil;

import java.util.Scanner;


@SuppressWarnings("Duplicates")
public class Task02 {


        private static final String QUEUE_NAME="ack_success";
        public static void main(String[] args) throws Exception {
        try(Channel channel= RabbitMqUtil.getChannel("192.168.1.128","admin","123")) {
            boolean d =true;
            channel.queueDeclare(QUEUE_NAME,d,false,false,null);
            //从控制台当中接受信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()){
                String message = scanner.next();
                channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
                System.out.println("发送消息完成:"+message);
            }
        }
    }
}
