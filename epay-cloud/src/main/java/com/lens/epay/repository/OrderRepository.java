package com.lens.epay.repository;

import com.lens.epay.enums.OrderStatus;
import com.lens.epay.enums.PaymentType;
import com.lens.epay.model.entity.Order;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface OrderRepository extends EpayRepository<Order, UUID>, JpaSpecificationExecutor<Order> {

    List<Order> findOrdersByUserId(UUID id);

    Page<Order> findOrdersByUserId(Pageable pageable, UUID id);

    Page<Order> findOrdersByOrderStatusAndCargoFirmAndRemittanceBankAndPaymentTypeAndPaid(Pageable pageable,
                                                                                          @Nullable OrderStatus orderStatus,
                                                                                          @Nullable String cargoFirm,
                                                                                          @Nullable String remittanceBank,
                                                                                          @Nullable PaymentType paymentType,
                                                                                          @Nullable Boolean paid);
//    Page<Order> findOrdersByOrderStatusAndCargoFirmAndRemittanceBankAndPaymentTypeAndPaid(Pageable pageable,
//                                                                                          Example<Order> example);
//    UUID userId,
//    int pageNumber,
//    Boolean desc,
//    String sortBy,
//    String startDate,
//    String endDate,
//    OrderStatus orderStatus,
//    PaymentType paymentType,
//    String cargoFirm,
//    String remittanceBank


}
