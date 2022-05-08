package zyc.work.databasework.Exception;

public class UserExistException extends Exception{

    @Override
    public String toString() {
        return "用户已存在";
    }
}
