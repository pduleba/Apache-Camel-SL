package com.pluralsight.processor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pluralsight.processor.entity.CatalogItemEntity;

/**
 * Repository access for CatalogItemEntity data.
 * 
 * @author Michael Hoffman, Pluralsight
 *
 */
public interface CatalogItemRepository extends JpaRepository<CatalogItemEntity, Long> {
   
}
