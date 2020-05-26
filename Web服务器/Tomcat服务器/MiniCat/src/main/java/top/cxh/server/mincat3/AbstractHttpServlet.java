package top.cxh.server.mincat3;

public abstract class AbstractHttpServlet implements Servlet {

    private static final String METHOD_GET = "GET";

    private static final String METHOD_POST = "POST";

    public abstract void doGet(Request request, Response response);

    public abstract void doPost(Request request, Response response);

    @Override
    public void service(Request request, Response response) throws Exception {
        if (METHOD_GET.equals(request.getMethod())) doGet(request, response);
        else doPost(request, response);
    }
}
