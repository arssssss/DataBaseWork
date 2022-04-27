package zyc.work.databasework.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zyc.work.databasework.mapper.ManagerMapper;
import zyc.work.databasework.pojo.*;

import java.util.List;

@Service
public class ManagerService {
    @Autowired
    public ManagerMapper managerMapper;

    public String login(String name, String password) {
        return managerMapper.selectIdByNaAndPw(name, password);
    }

    public PageInfo getStations(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectStation(), startPage);
    }

    public boolean addStation(Station station) {
        return managerMapper.addStation(station) > 0;
    }

    public boolean deleteStation(String s_id) {
        return managerMapper.deleteStation(s_id) > 0;
    }

    public PageInfo getTrains(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectTrain(), startPage);
    }

    public boolean addTrain(Train train) {
        return managerMapper.addTrain(train) > 0;
    }

    public boolean deleteTrain(String tr_id) {
        return managerMapper.deleteTrain(tr_id) > 0;
    }

    public PageInfo getRouterTrains(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectRouterTrain(), startPage);
    }

    public List<RouterTrain> getRouterTrainByTr_id(String tr_id){
        return managerMapper.selectRouterTrainByTr_id(tr_id);
    }

    public boolean addRouterTrain(RouterTrain routerTrain) {
        return managerMapper.addRouterTrain(routerTrain) > 0;
    }

    public boolean deleteRouterTrain(String tr_id,String r_id) {
        return managerMapper.deleteRouterTrain(tr_id,r_id) > 0;
    }

    public PageInfo getRouters(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectRouter(), startPage);
    }

    public boolean addRouter(Router router) {
        return managerMapper.addRouter(router) > 0;
    }

    public boolean deleteRouter(String r_id) {
        return managerMapper.deleteRouter(r_id) > 0;
    }

    public PageInfo getVehicleModels(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectVehicleModel(), startPage);
    }

    public boolean addVehicleModel(VehicleModel vehicleModel) {
        return managerMapper.addVehicleModel(vehicleModel) > 0;
    }

    public boolean deleteVehicleModel(String vm_id) {
        return managerMapper.deleteVehicleModel(vm_id) > 0;
    }
}
