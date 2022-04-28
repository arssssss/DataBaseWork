package zyc.work.databasework.interceptor.token;

import org.apache.ibatis.annotations.ResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import zyc.work.databasework.ResultType.ResponseResult;
import zyc.work.databasework.annotation.toekn.LoginToken;
import zyc.work.databasework.annotation.toekn.TokenCheck;
import zyc.work.databasework.enums.result.ResultCode;
import zyc.work.databasework.enums.token.TokenType;
import zyc.work.databasework.util.SendMsgUtil;
import zyc.work.databasework.util.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    public TokenUtil tokenUtil;

    public static ThreadLocal<String> Id = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Token");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod hm = (HandlerMethod) handler;
        if (hm.getMethod().isAnnotationPresent(LoginToken.class)) {
            if (hm.getMethod().getAnnotation(LoginToken.class).required()) {
                return true;
            }
        }
        if (hm.getMethod().isAnnotationPresent(TokenCheck.class)) {
            if (!hm.getMethod().getAnnotation(TokenCheck.class).required()) {
                return true;
            }
            if (!StringUtils.hasText(token)) {
                //未携带token
                SendMsgUtil.sendJsonMessage(response,new ResponseResult<>(ResultCode.ERROR.getValue(),"未登录"));
                return false;
            }
            if (!tokenUtil.IsEffective(token)) {
                //token过期
                SendMsgUtil.sendJsonMessage(response,new ResponseResult<>(ResultCode.ERROR.getValue(),"登录已过期"));
                return false;
            }
            if (tokenUtil.getId(token, hm.getMethod().getAnnotation(TokenCheck.class).TYPE()) == null) {
                //token无效
                SendMsgUtil.sendJsonMessage(response,new ResponseResult<>(ResultCode.ERROR.getValue(),"身份验证错误,请重新登录"));
                return false;
            }
            Id.set(tokenUtil.getId(token, hm.getMethod().getAnnotation(TokenCheck.class).TYPE()));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Id.remove();
    }

}
