package zyc.work.databasework.pojo;

import lombok.Data;

import java.time.LocalTime;
@Data
public class RobTicket {

    public String rob_id;

    public String u_id;

    public String tr_id;

    public String r_departure_id;

    public String  r_terminal_id;

    public LocalTime r_time;

    public Integer r_seat;

    public String p_id;
}
