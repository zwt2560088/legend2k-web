package com.zwt.service;


import com.zwt.model.account.AddRecordDTO;
import com.zwt.repo.sqlite.mapper.AccountRecordMapper;
import com.zwt.repo.sqlite.model.AccountRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author:
 * @Date: 2024/5/28 10:30
 */
@Service
@Slf4j
public class AccountRecordService {

    @Resource
    private AccountRecordMapper accountRecordMapper;

    public static final String LOGIN_CHANNEL_FIELD = "loginChannel";

    @Transactional
    public AccountRecord insertRecord(AddRecordDTO addRecordDTO) {
        if (Objects.isNull(addRecordDTO.getLoginChannel())) {
            addRecordDTO.setLoginChannel(LoginChannelEnum.ANDROID.getValue());
        }
        JSONObject jsonObject = JSON.parseObject(addRecordDTO.getLoginHandle());
        jsonObject.put(LOGIN_CHANNEL_FIELD, addRecordDTO.getLoginChannel());
        AccountRecord accountRecord = new AccountRecord();
        BeanUtils.copyProperties(addRecordDTO, accountRecord);
        long now = System.currentTimeMillis();
        accountRecord.setCtime(now);
        accountRecord.setUtime(now);
        accountRecord.setLoginHandle(jsonObject.toJSONString());
        return accountRecordMapper.save(accountRecord);
    }

    public List<RecordData> listRecord(QueryRecordDTO queryRecordDTO) {
        Integer startDt = queryRecordDTO.getStartDt();
        if (Objects.isNull(startDt)) {
            startDt = Integer.valueOf(DateUtils.getTodayDt());
        }
        Integer endDt = queryRecordDTO.getEndDt();
        if (Objects.isNull(endDt)) {
            endDt = Integer.valueOf(DateUtils.getTodayDt());
        }

        Long startTime = 0L;
        Long endTime = 0L;
        try {
            startTime = DateUtils.getStartTimeOfDt(startDt);
            endTime = DateUtils.getEndTimeOfDt(endDt);
        } catch (Exception e) {
            log.error("查询accountRecord时，日期转化出错,queryRecordDTO:[{}]", JSON.toJSONString(queryRecordDTO), e);
            throw new BizException(String.format("查询accountRecord时，日期转化出错,queryRecordDTO:[%s]", JSON.toJSONString(queryRecordDTO)));
        }
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Page page = queryRecordDTO.getPage();
        Long startId = Optional.ofNullable(page.getStartId()).orElse(0L);
        Integer pageSize = Optional.ofNullable(page.getPageSize()).orElse(200);
        PageRequest pageRequest = PageRequest.of(0, pageSize, sort);
        List<AccountRecord> accountRecordList = accountRecordMapper.findAllByCompetitorCodeAndLoginChannelAndIdGreaterThanAndAccountTimeBetween(
                queryRecordDTO.getCompetitorCode(),
                queryRecordDTO.getLoginChannel(),
                startId,
                startTime,
                endTime,
                pageRequest);

        return ListUtils.emptyIfNull(accountRecordList).stream()
                .map(it -> {
                    RecordData recordData = new RecordData();
                    BeanUtils.copyProperties(it, recordData);
                    return recordData;
                }).collect(Collectors.toList());
    }


}
