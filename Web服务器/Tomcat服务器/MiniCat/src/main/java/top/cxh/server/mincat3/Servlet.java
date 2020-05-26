package top.cxh.server.mincat3;

public interface Servlet {
    void init() throws Exception;

    void destory() throws Exception;

    void service(Request request, Response response) throws Exception;
}
