package com.zwt.repo.sqlite.mapper;

//import com.smart.disguiser.agent.disguiser.agent.dao.sqlite.model.DeviceAccountTask;
import com.zwt.repo.sqlite.model.DeviceAccountTask;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @Date: 2024/5/9 18:04
 */
public interface DeviceAccountTaskMapper extends JpaRepository<DeviceAccountTask, Long> {

    List<DeviceAccountTask> findByDtBetweenAndRegisterStatus(Integer startDt, Integer endDt, Integer registerStatus);

    List<DeviceAccountTask> findByDtBetween(Integer startDt, Integer endDt, Sort sort);

    List<DeviceAccountTask> findByDtBetween(Integer startDt, Integer endDt);

    @Modifying
    @Transactional
    @Query("UPDATE DeviceAccountTask d SET  d.getVerificationRetryCount = :getVerificationRetryCount WHERE d.id = :id")
    void updateGetVerificationRetryCountById(@Param("id") Long id, @Param("getVerificationRetryCount") Integer registerStatus);

    @Modifying
    @Transactional
    @Query("UPDATE DeviceAccountTask d SET  d.getPhoneRetryCount = :getPhoneRetryCount WHERE d.id = :id")
    void updateGetPhoneRetryCountById(@Param("id") Long id, @Param("getPhoneRetryCount") Integer registerStatus);
}
