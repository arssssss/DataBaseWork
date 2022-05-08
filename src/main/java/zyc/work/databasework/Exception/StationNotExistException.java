package zyc.work.databasework.Exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class StationNotExistException extends Exception{
    String message="站点不存在";

    public StationNotExistException(String message) {
        super();
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
