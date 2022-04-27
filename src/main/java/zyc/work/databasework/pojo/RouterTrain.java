package zyc.work.databasework.pojo;

import lombok.Data;
import sun.net.idn.Punycode;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class RouterTrain {
    public String tr_id;
    public String r_id;
    public LocalTime rtr_departure_time;
    public LocalTime rtr_arrival_time;
    public LocalDate rer_date;
}
