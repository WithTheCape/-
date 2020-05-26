package top.cxh.server.minicat2;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Minicat的主启动类
 */
public class Bootstrap {

    /**
     * 定义定义socket监听的端口号
     */
    private int port = 8080;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        /*
            完成Minicat 1.0版本
            需求：浏览器请求http://localhost:8080,返回一个固定的字符串到页面"Hello Minicat!"
         */
        ServerSocket serverSocket = new ServerSocket(port); //服务端socket连接
        System.out.println("Minicat start on port====>" + port);

//        while (true) {
//            Socket socket = serverSocket.accept();
//            OutputStream outputStream = socket.getOutputStream();//接受到请求后，获取输出流
//            String msg = "hello, my minicat!";
//            String responseText = HttpProtocolUtil.getHttpHeader200(msg.getBytes().length) + msg;
//            outputStream.write(responseText.getBytes()); //输出流向浏览器发出数据
//            socket.close();
//        }

        /**
         * 完成Minicat 2.0版本
         * 需求：封装Request和Response对象，返回html静态资源文件
         */
        while (true) {
            Socket socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // 将请求带过来的流封装Request对象和Response对象
            Request request = new Request(inputStream);
            Response response = new Response(outputStream);
            //向外输出静态资源
            response.outputHtml(request.getUrl());
            socket.close();
        }
    }

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
