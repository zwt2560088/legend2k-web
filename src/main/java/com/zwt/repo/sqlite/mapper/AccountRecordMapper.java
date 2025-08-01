package com.zwt.repo.sqlite.mapper;

import com.zwt.repo.sqlite.model.AccountRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author:
 * @Date: 2024/5/28 10:28
 */
public interface AccountRecordMapper extends JpaRepository<AccountRecord, Long> {

    List<AccountRecord> findAllByCompetitorCodeAndLoginChannelAndIdGreaterThanAndAccountTimeBetween(
            String competitorCode,
            Integer loginChannel,
            Long startId,
            Long startTime,
            Long endTime,
            Pageable pageable);


}
