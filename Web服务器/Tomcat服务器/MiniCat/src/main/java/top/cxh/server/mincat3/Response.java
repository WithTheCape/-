package top.cxh.server.mincat3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 封装Response对象，需要依赖于OutputStream
 * <p>
 * 该对象需要提供核心方法，输出html
 */
public class Response {
    private OutputStream outputStream;

    public Response() {

    }

    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * 对外输出内容
     *
     * @param content
     * @throws IOException
     */
    public void output(String content) throws IOException {
        outputStream.write(content.getBytes());
    }

    public void outputHtml(String path) throws IOException {
        // 获取静态资源文件的绝对路径
        String absoluteResourcePath = StaticResourceUtil.getAbsolutePath(path);
        File file = new File(absoluteResourcePath);
        if (file.exists() && file.isFile()) {
            //存在就读取静态文件，输出内容
            StaticResourceUtil.outputStaticResource(new FileInputStream(file), outputStream);
        } else {
            //不存在返回404
            output(HttpProtocolUtil.getHttpHeader404());
        }
    }
}
