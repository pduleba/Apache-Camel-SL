package com.pluralsight.processor.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pluralsight.processor.dto.CatalogItemDTO;
import com.pluralsight.processor.dto.CustomerDTO;
import com.pluralsight.processor.dto.OrderDataDTO;
import com.pluralsight.processor.dto.OrderItemDTO;
import com.pluralsight.processor.dto.OrderStatusDTO;
import com.pluralsight.processor.entity.CatalogItemEntity;
import com.pluralsight.processor.entity.CustomerEntity;
import com.pluralsight.processor.entity.OrderDataEntity;
import com.pluralsight.processor.entity.OrderItemEntity;
import com.pluralsight.processor.generated.FulfillmentCenter;
import com.pluralsight.processor.generated.ObjectFactory;
import com.pluralsight.processor.generated.Order;
import com.pluralsight.processor.generated.OrderItemType;
import com.pluralsight.processor.generated.OrderType;
import com.pluralsight.processor.repository.OrderItemRepository;
import com.pluralsight.processor.repository.OrderRepository;

/**
 * Services related to orders
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
@Transactional
@Service
public class OrderServiceImpl implements OrderService {
	private static final Logger log = LoggerFactory
			.getLogger(OrderServiceImpl.class);

	@Inject
	private OrderRepository orderRepository;

	@Inject
	private OrderItemRepository orderItemRepository;

	@Inject
	private FulfillmentService fulfillmentProcessor;

	@Override
	public List<OrderDataDTO> getOrderDetails() {
		List<OrderDataDTO> orders = new ArrayList<OrderDataDTO>();

		try {
			populateOrderDetails(orders, orderRepository.findAll());
		} catch (Exception e) {
			log.error(
					"An error occurred while retrieving all orders: "
							+ e.getMessage(), e);
		}

		return orders;
	}

	@Override
	public void processOrderFulfillment() {
		try {
			fulfillmentProcessor.run();
		} catch (Exception e) {
			log.error(
					"An error occurred during the execution of order fulfillment processing: "
							+ e.getMessage(), e);
		}
	}

	@Override
	public List<OrderDataDTO> getOrderDetails(OrderStatusDTO orderStatus, int fetchSize) {
		List<OrderDataDTO> orders = new ArrayList<OrderDataDTO>();

		try {
			populateOrderDetails(orders, orderRepository.findByStatus(
					orderStatus.getCode(), new PageRequest(0, fetchSize)));
		} catch (Exception e) {
			log.error(
					"An error occurred while getting orders by order status: "
							+ e.getMessage(), e);
		}

		return orders;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public void processOrderStatusUpdate(List<OrderDataDTO> orders,
			OrderStatusDTO orderStatus) throws Exception {
		List<Long> orderIds = new ArrayList<Long>();
		for (OrderDataDTO order : orders) {
			orderIds.add(order.getId());
		}
		orderRepository.updateStatus(orderStatus.getCode(),
				new Date(System.currentTimeMillis()), orderIds);
		orderItemRepository.updateStatus(orderStatus.getCode(),
				new Date(System.currentTimeMillis()), orderIds);
		for (OrderDataDTO order : orders) {
			order.setStatus(orderStatus.getCode());
		}
	}

	@Override
	public List<OrderItemDTO> getOrderItems(long id) {
		List<OrderItemDTO> orderItems = new ArrayList<OrderItemDTO>();

		try {
			List<OrderItemEntity> orderItemEntities = orderItemRepository
					.findByOrderId(id);
			populateOrderItems(orderItems, orderItemEntities);
		} catch (Exception e) {
			log.error(
					"An error occurred while retrieving order items for the order id |"
							+ id + "|: " + e.getMessage(), e);
		}
		return orderItems;
	}

	/**
	 * Populate the list of orders based on order entity details.
	 * 
	 * @param orders
	 * @param orderEntities
	 */
	private void populateOrderDetails(List<OrderDataDTO> orders,
			Iterable<OrderDataEntity> orderEntities) {
		for (Iterator<OrderDataEntity> iterator = orderEntities.iterator(); iterator
				.hasNext();) {
			OrderDataEntity entity = iterator.next();
			CustomerEntity customerEntity = entity.getCustomer();
			CustomerDTO customer = new CustomerDTO(customerEntity.getId(),
					customerEntity.getFirstName(),
					customerEntity.getLastName(), customerEntity.getEmail());
			orders.add(new OrderDataDTO(entity.getId(), customer, entity
					.getOrderNumber(), entity.getTimeOrderPlaced(), entity
					.getLastUpdate(), OrderStatusDTO.getOrderStatusByCode(
					entity.getStatus()).getDescription()));
		}
	}

	private void populateOrderItems(List<OrderItemDTO> orderItems,
			Iterable<OrderItemEntity> orderItemEntities) {
		for (Iterator<OrderItemEntity> iterator = orderItemEntities.iterator(); iterator
				.hasNext();) {
			OrderItemEntity entity = iterator.next();
			CatalogItemEntity catalogItemEntity = entity.getCatalogItem();
			CatalogItemDTO catalogItem = new CatalogItemDTO(
					catalogItemEntity.getId(),
					catalogItemEntity.getItemNumber(),
					catalogItemEntity.getItemName(),
					catalogItemEntity.getItemType());
			orderItems.add(new OrderItemDTO(entity.getId(), catalogItem, entity
					.getStatus(), entity.getPrice(), entity.getLastUpdate(),
					entity.getQuantity()));
		}
	}

	@Override
	public String processCreateOrderMessage(Long id) throws Exception {
		// Retrieve the order from the database using the order ID passed.
		OrderDataEntity orderEntity = orderRepository.findOne(id);
		// Map the order database data to a schema generated Order.
		Order order = buildOrderXmlType(orderEntity);

		// Marshall the Order into an XML string.
		JAXBContext context = JAXBContext
				.newInstance(Order.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter writer = new StringWriter();
		marshaller.marshal(order, writer);
		return writer.toString();
	}

	/**
	 * Accepts an OrderEntity and maps the data contents to an order type from
	 * the schema.
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	private Order buildOrderXmlType(
			OrderDataEntity order) throws Exception {
		ObjectFactory objectFactory = new ObjectFactory();
		OrderType orderType = objectFactory.createOrderType();
		orderType.setFirstName(order.getCustomer().getFirstName());
		orderType.setLastName(order.getCustomer().getLastName());
		orderType.setEmail(order.getCustomer().getEmail());
		// Default to ABC_FULFILLMENT_CENTER. All web orders will be fulfilled
		// through this endpoint.
		orderType
				.setFulfillmentCenter(FulfillmentCenter.ABC_FULFILLMENT_CENTER);
		orderType.setOrderNumber(order.getOrderNumber());
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(order.getTimeOrderPlaced());
		orderType.setTimeOrderPlaced(DatatypeFactory.newInstance()
				.newXMLGregorianCalendar(cal));
		for (OrderItemEntity orderItem : order.getOrderItems()) {
			OrderItemType orderItemType = objectFactory.createOrderItemType();
			orderItemType.setItemNumber(orderItem.getCatalogItem()
					.getItemNumber());
			orderItemType.setPrice(orderItem.getPrice());
			orderItemType.setQuantity(orderItem.getQuantity());
			orderType.getOrderItems().add(orderItemType);
		}
		Order orderElement = objectFactory
				.createOrder();
		orderElement.setOrderType(orderType);
		return orderElement;
	}
}
