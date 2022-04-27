package zyc.work.databasework.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Price {

    public String p_id;

    public String vm_id;

    public String p_seat_style;

    public BigDecimal p_price;

    public String p_holiday;

}
