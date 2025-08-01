package com.zwt.repo.sqlite.mapper;

import com.zwt.repo.sqlite.model.AccountDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface AccountDetailMapper extends JpaRepository<AccountDetail, Long> {

    List<AccountDetail> findByPhoneNum(String phoneNum);

    List<AccountDetail> findByBatchAndCompetitorCodeAndAccountSource(String batch, String competitorCode, String accountSource);

    AccountDetail findTopByAccountSourceAndRegisterStatus(String accountSource, Integer registerStatus);

    @Modifying
    @Transactional
    @Query("UPDATE AccountDetail s SET  s.registerStatus = :registerStatus WHERE s.id = :id")
    void updateById(@Param("id") Long id, @Param("registerStatus") Integer registerStatus);

    AccountDetail findByPhoneNumAndCompetitorCodeAndAccountSource(@Param("phoneNum") String phoneNum, @Param("competitorCode") String competitorCode, @Param("accountSource") String accountSource);
}
