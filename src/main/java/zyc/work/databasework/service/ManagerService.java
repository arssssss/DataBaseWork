package zyc.work.databasework.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zyc.work.databasework.mapper.ManagerMapper;
import zyc.work.databasework.pojo.*;

import java.util.List;

@Service
public class ManagerService {
    @Autowired
    public ManagerMapper managerMapper;

    @Transactional(rollbackFor = {Exception.class})
    public String login(String name, String password) {
        return managerMapper.selectIdByNaAndPw(name, password);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo getStations(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectStation(), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addStation(Station station) {
        managerMapper.addStation(station);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteStation(String s_id) {
        managerMapper.deleteStation(s_id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo getTrains(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectTrain(), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addTrain(Train train) {
        managerMapper.addTrain(train);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteTrain(String tr_id) {
        managerMapper.deleteTrain(tr_id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo getRouterTrains(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectRouterTrain(), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public List<RouterTrain> getRouterTrainByTr_id(String tr_id){
        return managerMapper.selectRouterTrainByTr_id(tr_id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addRouterTrain(RouterTrain routerTrain) {
        managerMapper.addRouterTrain(routerTrain);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteRouterTrain(String tr_id,String r_id) {
        managerMapper.deleteRouterTrain(tr_id,r_id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo getRouters(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectRouter(), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addRouter(Router router) {
        managerMapper.addRouter(router);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteRouter(String r_id) {
        managerMapper.deleteRouter(r_id);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PageInfo getVehicleModels(Integer startPage, Integer pageSize) {
        PageHelper.startPage(startPage, pageSize);
        return new PageInfo<>(managerMapper.selectVehicleModel(), startPage);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void addVehicleModel(VehicleModel vehicleModel) {
        managerMapper.addVehicleModel(vehicleModel);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void deleteVehicleModel(String vm_id) { managerMapper.deleteVehicleModel(vm_id);
    }
}
