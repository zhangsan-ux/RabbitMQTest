package per.can.rebbitMq.four;

import com.rabbitmq.client.Channel;
import per.can.rebbitMq.utils.RabbitMqUtil;

import java.util.UUID;


@SuppressWarnings("Duplicates")
public class PublishMessage {

    public static int  MESSAGE_COUNT =1000;

    public static void main(String[] args) throws Exception {
        //单个确认发布
       // publishMessageIndividually();
        //批量确认发布
      // publishMessageAll();
        publishMessageBatch();
    }




    public static  void  publishMessageIndividually() throws Exception {

        Channel channel = RabbitMqUtil.getChannel("192.168.1.128","admin","123");
        //声明队列
        String  queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName,false,false,false,null);

        //开启发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {

            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //服务端返回 false 或超时时间内未返回，生产者可以消息重发
            boolean flag = channel.waitForConfirms();
            if(flag){
                System.out.println("消息发送成功");
            }

        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时" + (end - begin) /1000+
                "s");
    }

    public static  void  publishMessageAll() throws Exception {

        Channel channel = RabbitMqUtil.getChannel("192.168.1.128","admin","123");
        //声明队列
        String  queueName = UUID.randomUUID().toString();

        channel.queueDeclare(queueName,false,false,false,null);

        //开启发布确认
        channel.confirmSelect();
        //批量确认消息大小
        int batchSize = 100;
        //未确认消息个数
        int outstandingMessageCount = 0;


        long begin = System.currentTimeMillis();
        boolean flagSubmit =false;
        int j =0;
        for (int i = 0; i < MESSAGE_COUNT; i++) {

            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            outstandingMessageCount++;
            if (outstandingMessageCount==batchSize){
                flagSubmit=  channel.waitForConfirms();
                outstandingMessageCount = 0;
                j ++;
            }
            if (flagSubmit){
                System.out.println("批量确认100条信息成功！+"+j);
                long endHun = System.currentTimeMillis();
                System.out.println("批量确认100条信息当前秒数 ："+endHun);

            }

        }

        //为了确保还有剩余没有确认消息 再次确认
        if (outstandingMessageCount > 0) {
            channel.waitForConfirms();
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时" + (end - begin) +
                "ms");
    }

    public static void publishMessageBatch() throws Exception {
        try (Channel channel = RabbitMqUtil.getChannel("192.168.1.128","admin","123");) {
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, false, false, false, null);
            //开启发布确认
            channel.confirmSelect();
            //批量确认消息大小
            int batchSize = 100;
            //未确认消息个数
            int outstandingMessageCount = 0;
            long begin = System.currentTimeMillis();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = i + "";
                channel.basicPublish("", queueName, null, message.getBytes());
                outstandingMessageCount++;
                if (outstandingMessageCount == batchSize) {
                    channel.waitForConfirms();
                    outstandingMessageCount = 0;
                }
            }
            //为了确保还有剩余没有确认消息 再次确认
            if (outstandingMessageCount > 0) {
                channel.waitForConfirms();
            }
            long end = System.currentTimeMillis();
            System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时" + (end - begin) +
                    "ms");
        } }


}
