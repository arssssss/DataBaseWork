package zyc.work.databasework.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatInfo {

    public List<Boolean> booleans;

    public Integer seatSum;

    public Boolean havingSeat;

    public Integer firstClassNum;

    public Integer secondClassNum;

    public String vm_id;

}
