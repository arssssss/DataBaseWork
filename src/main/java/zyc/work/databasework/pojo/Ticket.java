package zyc.work.databasework.pojo;

import lombok.Data;
import sun.net.idn.Punycode;
@Data
public class Ticket {

    public String ti_id;

    public String u_id;

    public String tr_id;

    public String p_id;

    public String ti_departure_id;

    public String ti_terminal_id;

    public Integer ti_seat;
}
