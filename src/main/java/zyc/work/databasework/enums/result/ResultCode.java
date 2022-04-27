package zyc.work.databasework.enums.result;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ResultCode {
    ERROR(400),OK(200);

    int value;

    ResultCode(int i) {
        value=i;
    }
}
