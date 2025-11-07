package org.example.domain.message;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Order;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.domain.message.entity.list.OrderList;
import org.example.domain.message.professional.record.PersonInfo;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Patient extends PersonInfo implements Cloneable {
    private String suffix;
    private String secondSurname;
    private String dateOfBirth;
    private String sex;
    private OrderList orders;

    public static Patient Default(String sampleId) {

        Patient patient = new Patient();
        patient.setCode("MN-10000000");
        patient.setLastName("LastName");
        patient.setFirstName("FirstName");
        patient.setMiddleName("MiddleInitial");
        patient.setAddress("Iaddress");
        patient.setCity("city");
        patient.setCountry("Icountry");
        patient.setState("state");
        patient.setZip("zipcode");
        patient.setHomePhone("hometel");
        patient.setWorkPhone("worktel");
        patient.setMobile("mobiletel");
        patient.setEmail("Imail@e.com");
        patient.setSuffix("Sr.");
        patient.setSex("M");
        patient.setDateOfBirth("19800101");

        Order order = Order.OneSpecimen(sampleId);

        patient.setOrder(order);

        return patient;
    }

    public List<Slide> getAllSlides() {
        List<Order> orderList = orders.getOrderList();
        List<Slide> slidesList = new ArrayList<>();
        for (Order order : orderList) {
            List<Slide> slidesFromOrder = order.getAllSlides();
            slidesList.addAll(slidesFromOrder);
        }
        return slidesList;
    }

    public List<Block> getAllBlocks() {
        List<Order> orderList = orders.getOrderList();
        List<Block> blockList = new ArrayList<>();
        for (Order order : orderList) {
            List<Block> blocksFromOrder = order.getAllBlocks();
            blockList.addAll(blocksFromOrder);
        }
        return blockList;
    }

    public List<Specimen> getAllSpecimens() {
        List<Order> orderList = this.orders.getOrderList();
        List<Specimen> specimenList = new ArrayList<>();
        for (Order order : orderList) {
            List<Specimen> specimenFromOrder = order.getAllSpecimen();
            specimenList.addAll(specimenFromOrder);
        }
        return specimenList;
    }

    public List<Order> getAllOrders() {
        return this.orders.getOrderList();
    }

    public Order getSingleOrder() {
        if (orders.getOrderList().size() == 1) {
            return orders.getOrderList().get(0);
        } else {
            throw new RuntimeException("Not possible to return only a order. Patient contains " + orders.getOrderList().size() + " orders" );
        }
    }
    public Order getSingleOrder(String sampleId) {
        for (Order order : getAllOrders()) {
            if (order.getSampleId().equals(sampleId)) {
                return order;
            }
        }
        throw new RuntimeException("Not find order with sampleId: " + sampleId );
    }

    public void setOrder(Order order) {
        OrderList orderList = new OrderList();
        List<Order> orders = new ArrayList<>(List.of(order));
        orderList.setOrderList(orders);
        setOrders(orderList);
    }

    public Specimen getSpecimen() {
        if (getAllSpecimens().size() == 1) {
            return getAllSpecimens().get(0);
        } else {
            throw new RuntimeException("Not possible to return only a specimen. Patient contains " + orders.getOrderList().size() + " specimens" );
        }
    }

    public Specimen getSpecimen(String id) {
        for (Specimen specimen : getAllSpecimens()) {
            if ((specimen.getId() != null) && specimen.getId().equals(id)) {
                return specimen;
            }
        }
        throw new RuntimeException("Not find specimen with a id: " + id );
    }

    public void setSpecimen(Specimen specimen) {
        getSingleOrder().setSpecimen(specimen);
    }

    public Block getBlock() {
        if (getAllBlocks().size() == 1) {
            return getAllBlocks().get(0);
        } else {
            throw new RuntimeException("Not possible to return only a block. Patient contains " + orders.getOrderList().size() + " blocks" );
        }
    }

    public Slide getSlide() {
        if (getAllSlides().size() == 1) {
            return getAllSlides().get(0);
        } else {
            throw new RuntimeException("Not possible to return only a slide. Patient contains " + orders.getOrderList().size() + " slide" );
        }
    }

    public Slide getSlide(String id) {
        for (Slide slide : getAllSlides()) {
            if ((slide.getId() != null) && slide.getId().equals(id)) {
                return slide;
            }
        }
        throw new RuntimeException("Not find slide with a id: " + id );
    }

    public void setSlide(Slide slide) {
        getSingleOrder().setSlide(slide);
    }


    @Override
    public Patient clone() {
        Patient cloned = (Patient) super.clone();
        if (this.orders != null) {
            cloned.setOrders(this.orders.clone());
        } else {
            cloned.setOrders(null);
        }
        return cloned;
    }
}
