package com.ohgiraffers.hitechautoworks.repair.dao;

import com.ohgiraffers.hitechautoworks.auth.dto.UserDTO;
import com.ohgiraffers.hitechautoworks.part.dto.PartDTO;
import com.ohgiraffers.hitechautoworks.repair.dto.Repair2DTO;
import com.ohgiraffers.hitechautoworks.repair.dto.RepairDTO;
import com.ohgiraffers.hitechautoworks.repair.dto.RepairPartDTO;
import com.ohgiraffers.hitechautoworks.repair.dto.WorkerDTO;
import com.ohgiraffers.hitechautoworks.res.dto.ResDTO;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface RepairMapper {

    List<Map<String,Object>> findAllRepair();
    List<Map<String,Object>> SearchByresCode(int resCode);

    List<Map<String,Object>> SearchByworkerName(String worker);

    RepairDTO selectRepair(int resCode);

    void modifyRepair(Map<String,Object> info);

    List<Integer>  selectUserCodeByUserName(List<String> userName);

    List<Integer> selectPartCodeByPartName(List<String> partName);

    void modifyRepairWorker(List<Integer>  newUserCode, int resCode);

    void modifyRepairPart(List<Integer>  newPartCode, int resCode);

    void deleteRepair(int resCode);

    List<PartDTO> findPartList();

    List<Map<String,Object>> findWorkerList(Object code);

    List<ResDTO> findResList();

    void addRepair(int resCode, String content, String status, String date, int extraTime);

    void addRepairPart(List<Integer>  newPartCode, int resCode);

    void addRepairWorker(List<Integer>  newUserCode, int resCode);

    List<RepairPartDTO> selectRepairPart(int resCode);

    List<WorkerDTO> selectWorker(int resCode);


    void deleteOldWorker( int resCode);

    void deleteOldPart( int resCode);

    Map<String, Object> getDate(Object code);

    List<Map<String, Object>> modalClick(Object resCode);


    int selectPartStock(Object partCode);

    void modifyPartStock(Object partCode, int modifyStock);

    void modifyStatus(Object resCode);


    int repairChart(int i);

    List<Map<String, Object>> workerChart();

    List<Map<String, Object>> searchAllRepairComments(int resCode);

    int registComment(Map<String, Object> comment);

    int editComment(Map<String, Object> info);

    int deleteComment(Map<String, Object> info);

    List<Map<String, Object>> searchAllReplyComments(int resCode);

    int submitRepairReply(Map<String, Object> info);

    Map<String, Object> searchRepairReply(Object replyCode);

    int editRepairReplyComment(Map<String, Object> info);

    int deleteRepairReplyCommen(Map<String, Object> info);
}
