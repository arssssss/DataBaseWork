package zyc.work.databasework.Exception;

public class LoginFailException extends Exception{

    @Override
    public String toString() {
        return "登陆失败，账号密码错误";
    }
}
