package zyc.work.databasework.pojo;

import lombok.Data;

@Data
public class Router {
    public String r_id;
    public String r_current_station;
    public String r_next_station;
    public String r_instructions;
    public Double r_distance;
}
