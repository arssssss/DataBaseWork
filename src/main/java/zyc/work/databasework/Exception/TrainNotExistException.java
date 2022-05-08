package zyc.work.databasework.Exception;

public class TrainNotExistException extends Exception{
    @Override
    public String toString() {
        return "该车次不存在";
    }
}
