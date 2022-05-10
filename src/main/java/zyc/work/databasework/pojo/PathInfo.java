package zyc.work.databasework.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PathInfo {

    public String tr_name;

    public String s_station_name;

    public String e_station_name;

    public LocalDate date;

    public LocalTime s_time;

    public LocalTime e_time;

}
