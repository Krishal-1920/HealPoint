package com.example.HealPoint.mapper;

import com.example.HealPoint.entity.Billing;
import com.example.HealPoint.entity.BillingItem;
import com.example.HealPoint.entity.Cart;
import com.example.HealPoint.model.BillingItemsModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BillingMapper {

    BillingMapper INSTANCE = Mappers.getMapper(BillingMapper.class);

    @Mapping(target = "productName", source = "inventory.productName")
    @Mapping(target = "productQuantity", source = "quantity")
    @Mapping(target = "totalProductPrice", source = "inventory.itemPrice")
    BillingItemsModel billingItemtoBillingItemsModel(BillingItem billingItem);

    @Mapping(target = "billing", source = "billing")
    @Mapping(target = "inventory", source = "cart.inventory")
    @Mapping(target = "productName", source = "cart.inventory.productName")
    @Mapping(target = "priceAtPurchase", source = "cart.inventory.itemPrice")
    @Mapping(target = "quantity", source = "cart.quantity")
    BillingItem updateToBillingItem(Cart cart, Billing billing);


    @Mapping(target = "productName", source = "cart.inventory.productName")
    @Mapping(target = "productQuantity", source = "cart.quantity")
    @Mapping(target = "totalProductPrice", expression = "java(cart.getInventory().getItemPrice() * cart.getQuantity())")
    BillingItemsModel cartToBillingItemsModel(Cart cart);

}
