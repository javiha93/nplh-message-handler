package org.example.domain.message.entity.list;

import lombok.Data;
import org.example.domain.message.Reflection;
import org.example.domain.message.entity.Order;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderList extends Reflection implements Cloneable {
    private List<Order> orderList;

    public void mergeOrder(Order newOrder) {
        boolean isNewOrder = true;
        if ((orderList == null) || (orderList.isEmpty())) {
            orderList = new ArrayList<>();
            orderList.add(newOrder);
        }
        for(Order order : orderList) {
            if (order.getSampleId().equals(newOrder.getSampleId())) {
                isNewOrder = false;
                List<Specimen> specimenList = newOrder.getAllSpecimen();
                for (Specimen specimen : specimenList) {
                    order.getSpecimens().mergeSpecimen(specimen);
                }
            }
        }
        if (isNewOrder) {
            orderList.add(newOrder);
        }
    }

    @Override
    public OrderList clone() {
        try {
            OrderList cloned = (OrderList) super.clone();
            if (orderList == null) {
                return cloned;
            }
            List<Order> clonedOrderList = new ArrayList<>();
            for (Order order : this.orderList) {
                clonedOrderList.add(order.clone());
            }
            cloned.setOrderList(clonedOrderList);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for OrderList", e);
        }
    }
}
