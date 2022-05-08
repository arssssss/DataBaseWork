package zyc.work.databasework.Exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TimeOutException extends Exception{
    String message="时间错误";

    public TimeOutException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
