package nia.chapter4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 参考:
 * 1、https://www.liaoxuefeng.com/wiki/1252599548343744/1305207629676577
 * 2、https://wiki.jikexueyuan.com/project/java-socket/tcp.html
 * <p>
 * 实现的功能是：客户端发送一条信息后，服务器将这条信息读出来，再返回一条信息给客户端。
 * 如此来回三次，直到客户端发送"bye"，断开连接。
 *
 * @author chenyuqun
 * @date 2021/3/17 6:34 下午
 */
public class PlainOioServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket client;
        while (true) {
            //等待客户端的连接，如果没有获取连接
            client = serverSocket.accept();
            System.out.println("与客户端连接成功！");
            //为每个客户端连接开启一个线程
            new Thread(new ServerThread(client)).start();
        }
    }

    public static class ServerThread implements Runnable {

        private Socket client;

        public ServerThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                //获取Socket的输出流，用来向客户端发送数据
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                //获取Socket的输入流，用来接收从客户端发送过来的数据
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                for (; ; ) {
                    //接收从客户端发送过来的数据
                    String str = reader.readLine();
                    System.out.println("receive form client,  message: " + str);
                    if (str == null || "".equals(str)) {
                        break;
                    }
                    if ("bye".equals(str)) {
                        writer.write("bye\n");
                        writer.flush();
                        break;
                    }
                    //将接收到的字符串前面加上echo，发送到对应的客户端
                    System.out.println("send to client...");
                    writer.write("send to client: " + str + "\n");
                    System.out.println("send over");
                    writer.flush();
                }
                writer.close();
                client.close();
                System.out.println("与客户端断开连接，正在等等待新的客户端连接...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

