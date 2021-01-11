package com.example.rabittmqdemo.mapper;

import com.example.rabittmqdemo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface MessageMapper extends JpaRepository<Message, String> {
    @Modifying
    @Query(value="update message set status = ?1 where id = ?2",nativeQuery=true)
    int updateById(Integer status, String id);

    @Query(value="select * from  message  where id = ?1",nativeQuery=true)
    Message findByIds(String id);

    @Modifying
    @Query(value="update message set status = ?1,retry_times= ?2 where id = ?3",nativeQuery=true)
    int update(int status, int retryTimes, String id);


    @Query(value="select * from  message  where status = ?1 and next_retry_date_time <= ?2",nativeQuery=true)
    List<Message> selectByStatus(Integer status, Date date);
}
