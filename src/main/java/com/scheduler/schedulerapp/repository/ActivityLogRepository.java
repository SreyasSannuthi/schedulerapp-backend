package com.scheduler.schedulerapp.repository;

import com.scheduler.schedulerapp.model.ActivityLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityLogRepository extends MongoRepository<ActivityLog, String> {
    List<ActivityLog> findByEntityTypeAndEntityIdOrderByTimestampDesc(String entityType, String entityId);
    List<ActivityLog> findByEntityTypeOrderByTimestampDesc(String entityType);
    List<ActivityLog> findAllByOrderByTimestampDesc();
}