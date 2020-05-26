package top.cxh.server.mincat3;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.xml.parsers.SAXParser;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Minicat的主启动类
 */
public class Bootstrap {

    /**
     * 定义定义socket监听的端口号
     */
    private int port = 8080;

    private Map<String, MyCoreServlet> servletMap = new HashMap<>();

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void start() throws Exception {

        loadServlet();
        /**
         * 完成Minicat 3.0版本
         * 需求：可以请求动态资源（Servlet）
         */
        ServerSocket serverSocket = new ServerSocket(port); //服务端socket连接
        System.out.println("Minicat start on port====>" + port);

        while (true) {
            Socket socket = serverSocket.accept();

            Request request = new Request(socket.getInputStream());
            Response response = new Response(socket.getOutputStream());

            if (servletMap.get(request.getUrl()) == null) {
                //如果不是请求servlet，那就是请求静态资源
                response.outputHtml(request.getUrl());
            } else {
                MyCoreServlet myCoreServlet = servletMap.get(request.getUrl());
                myCoreServlet.service(request, response);
            }
            socket.close();
        }

    }

    /**
     * 解析web.xml，初始化servlet
     */
    private void loadServlet() {
        //根据
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        //xml解析器
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(inputStream);    //将整个xml解析为文档对象
            Element rootElement = document.getRootElement();    //获取根节点
            List<Element> selectNodes = rootElement.selectNodes("//servlet"); //一个xml可以有多个servlet标签
            selectNodes.stream().forEach(item -> {
//                解析<servlet-name>myCoreApp</servlet-name>
                Element serlvetNameElement = (Element) item.selectSingleNode("servlet-name");
                String servletName = serlvetNameElement.getStringValue();

//                解析<servlet-class>top.cxh.server.mincat3.MyCoreServlet</servlet-class>
                Element servletClassElement = (Element) item.selectSingleNode("servlet-class");
                String servletClass = servletClassElement.getStringValue();

//                根据servlet-name找到url-pattern
                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
                try {
                    servletMap.put(urlPattern, (MyCoreServlet) Class.forName(servletClass).getDeclaredConstructor().newInstance());
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });

        } catch (DocumentException e) {
            e.printStackTrace();
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
