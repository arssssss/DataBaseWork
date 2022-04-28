package zyc.work.databasework.util;

import com.fasterxml.jackson.databind.ObjectMapper;


import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class SendMsgUtil {

    /**
     * 发送消息 text/html;charset=utf-8
     * @param response
     * @param str
     * @throws Exception
     */
    public static void sendMessage(HttpServletResponse response, String str) throws Exception {
        response.setContentType("text/html; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(str);
        writer.close();
        response.flushBuffer();
    }

    /**
     * 将某个对象转换成json格式并发送到客户端
     * @param response
     * @param obj
     * @throws Exception
     */
    public static <T> void sendJsonMessage(HttpServletResponse response, T obj) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(new ObjectMapper().writer().writeValueAsString(obj));
        writer.close();
        response.flushBuffer();
    }
}
