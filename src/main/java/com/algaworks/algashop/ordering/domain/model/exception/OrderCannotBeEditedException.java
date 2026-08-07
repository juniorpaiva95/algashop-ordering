package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;

public class OrderCannotBeEditedException extends DomainException{

    public OrderCannotBeEditedException(String message) {
        super(message);
    }

    public OrderCannotBeEditedException(OrderId id, OrderStatus orderStatus) {
        this(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED, id, orderStatus));
    }
}
