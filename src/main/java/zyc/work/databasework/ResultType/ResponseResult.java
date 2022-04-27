package zyc.work.databasework.ResultType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zyc.work.databasework.enums.result.ResultCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> {
    int code;

    T message;

}
