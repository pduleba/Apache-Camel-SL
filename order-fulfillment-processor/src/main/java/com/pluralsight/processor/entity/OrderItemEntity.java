package com.pluralsight.processor.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Order item entity
 * 
 * @author Michael Hoffman
 * 
 */
@Entity
@Table(name = "orderItem")
@SequenceGenerator(name = "orderitem_id_seq", sequenceName = "orderitem_id_seq")
public class OrderItemEntity implements Serializable {

   private static final long serialVersionUID = -3950813201243788920L;

   private long id;
   private OrderDataEntity order;
   private CatalogItemEntity catalogItem;
   private String status;
   private BigDecimal price;
   private Date lastUpdate;
   private int quantity;

   public OrderItemEntity() {

   }

   public OrderItemEntity(long id, OrderDataEntity order, CatalogItemEntity catalogItem,
         String status, Date lastUpdate, int quantity) {
      this.id = id;
      this.order = order;
      this.catalogItem = catalogItem;
      this.status = status;
      this.lastUpdate = lastUpdate;
      this.quantity = quantity;
   }

   public OrderItemEntity(long id, OrderDataEntity order, CatalogItemEntity catalogItem,
         String status, Date lastUpdate, int quantity, BigDecimal price) {
      this.id = id;
      this.order = order;
      this.catalogItem = catalogItem;
      this.status = status;
      this.lastUpdate = lastUpdate;
      this.quantity = quantity;
      this.price = price;
   }

   /**
    * @return the id
    */
   @Id
   @Column(name = "id")
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orderitem_id_seq")
   public long getId() {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId(long id) {
      this.id = id;
   }

   /**
    * @return the order
    */
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "order_id", nullable = false)
   public OrderDataEntity getOrder() {
      return order;
   }

   /**
    * @param order
    *           the order to set
    */
   public void setOrder(OrderDataEntity order) {
      this.order = order;
   }

   /**
    * @return the catalogItem
    */
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "catalogItem_id", nullable = false)
   public CatalogItemEntity getCatalogItem() {
      return catalogItem;
   }

   /**
    * @param catalogItem
    *           the catalogItem to set
    */
   public void setCatalogItem(CatalogItemEntity catalogItem) {
      this.catalogItem = catalogItem;
   }

   /**
    * @return the status
    */
   @Column(name = "status", nullable = false)
   public String getStatus() {
      return status;
   }

   /**
    * @param status
    *           the status to set
    */
   public void setStatus(String status) {
      this.status = status;
   }

   /**
    * @return the price
    */
   @Column(name = "price", precision = 20, scale = 5)
   public BigDecimal getPrice() {
      return price;
   }

   /**
    * @param price
    *           the price to set
    */
   public void setPrice(BigDecimal price) {
      this.price = price;
   }

   /**
    * @return the lastUpdate
    */
   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "lastUpdate", nullable = false, length = 29)
   public Date getLastUpdate() {
      return lastUpdate;
   }

   /**
    * @param lastUpdate
    *           the lastUpdate to set
    */
   public void setLastUpdate(Date lastUpdate) {
      this.lastUpdate = lastUpdate;
   }

   /**
    * @return the quantity
    */
   @Column(name = "quantity", nullable = false)
   public int getQuantity() {
      return quantity;
   }

   /**
    * @param quantity
    *           the quantity to set
    */
   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

}
