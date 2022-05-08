package zyc.work.databasework.Exception;

public class MissingPhoneException extends Exception{

    @Override
    public String toString() {
        return "无效的电话号码";
    }
}
