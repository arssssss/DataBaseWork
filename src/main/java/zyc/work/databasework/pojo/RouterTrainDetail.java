package zyc.work.databasework.pojo;

import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.security.PublicKey;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RouterTrainDetail {

    public String tr_id;
    public String r_id;
    public String tr_name;
    public String vm_id;
    public LocalDate rtr_date;
    public LocalTime rtr_departure_time;
    public LocalTime rtr_arrival_time;
    public String r_instructions;
    public Double r_distance;
    public String start_s_id;
    public String end_s_id;
    public String start_s_name;
    public String end_s_name;
    public String start_s_address;
    public String end_s_address;
}
