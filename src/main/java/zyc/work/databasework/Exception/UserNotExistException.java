package zyc.work.databasework.Exception;

public class UserNotExistException extends Exception{
    @Override
    public String toString() {
        return "用户不存在";
    }
}
