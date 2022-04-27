package zyc.work.databasework.mapper;

import org.apache.ibatis.annotations.Mapper;
import zyc.work.databasework.pojo.*;

import javax.swing.*;
import java.util.List;

@Mapper
public interface ManagerMapper {

    public String selectIdByNaAndPw(String name,String password);

    public List<Station> selectStation();

    public Integer addStation(Station station);

    public Integer deleteStation(String s_id);

    public List<Train> selectTrain();

    public Integer addTrain(Train train);

    public Integer deleteTrain(String tr_id);

    public List<RouterTrain> selectRouterTrain();

    public List<RouterTrain> selectRouterTrainByTr_id(String tr_id);

    public Integer addRouterTrain(RouterTrain routerTrain);

    public Integer deleteRouterTrain(String tr_id,String r_id);

    public List<Router> selectRouter();

    public Integer addRouter(Router router);

    public Integer deleteRouter(String r_id);

    public List<VehicleModel> selectVehicleModel();

    public Integer addVehicleModel(VehicleModel vehicleModel);

    public Integer deleteVehicleModel(String vm_id);
}
