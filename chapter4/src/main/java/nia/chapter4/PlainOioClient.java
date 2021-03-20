package nia.chapter4;

import io.netty.channel.local.LocalAddress;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author chenyuqun
 * @date 2021/3/17 6:38 下午
 */
public class PlainOioClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 8888);
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        List<String> messages = new ArrayList<>();
        // TODO: 这里的后面一定要加"\n"，因为readLine是以\n为结尾的。
        messages.add("hello\n");
        messages.add("world\n");
        messages.add("bye\n");
        for (String message : messages) {
            System.out.println("send message:" + message + "...");
            writer.write(message);
            writer.flush();
            System.out.println("send message over");
            if ("bye\n".equals(message)) {
                break;
            }
            try {
                String echo = reader.readLine();
                System.out.println("receive from server:" + echo);
            } catch (SocketTimeoutException e) {
                System.out.println("Time out, No response");
            }

        }
        //如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
        socket.close(); //只关闭socket，其关联的输入输出流也会被关闭
    }
}
