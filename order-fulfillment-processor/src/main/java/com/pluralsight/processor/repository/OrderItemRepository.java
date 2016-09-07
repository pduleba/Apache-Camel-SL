package com.pluralsight.processor.repository;

import java.util.*;

import org.springframework.data.jpa.repository.*;

import com.pluralsight.processor.entity.OrderItemEntity;

/**
 * Repository for OrderItemEntity data.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

   @Query("select oi from OrderItemEntity oi where oi.order.id = ?1")
   List<OrderItemEntity> findByOrderId(long orderId);

   @Modifying
   @Query("update OrderItemEntity oi set oi.status = ?1, oi.lastUpdate = ?2 where oi.order.id in (?3)")
   int updateStatus(String code, Date lastUpdate, List<Long> orderIds);

}
