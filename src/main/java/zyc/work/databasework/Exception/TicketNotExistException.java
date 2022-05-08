package zyc.work.databasework.Exception;

public class TicketNotExistException extends Exception{
    @Override
    public String toString() {
        return "票不存在";
    }
}
