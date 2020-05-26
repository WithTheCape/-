package top.cxh.server.mincat3;

/**
 * http请求的协议处理类
 */
public class HttpProtocolUtil {

    public static String getHttpHeader200(long contentLength) {
        StringBuilder builder = new StringBuilder();
        builder.append("HTTP/1.1 200 OK \n");
        builder.append("Content-Type: text/html \n");
        builder.append("Content-Length: " + contentLength + " \n");
        builder.append("\r\n");
        return builder.toString();
    }

    public static String getHttpHeader404() {
        StringBuilder builder = new StringBuilder();
        String str404 = "<h1 style='text-align: center'>404 not found</h1>";
        builder.append("HTTP/1.1 404 NOT Found \n");
        builder.append("Content-Type: text/html \n");
        builder.append("Content-Length: " + str404.getBytes().length + " \n");
        builder.append("\r\n" + str404);
        return builder.toString();
    }

}
