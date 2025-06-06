package com.example.HealPoint.mapper;

import com.example.HealPoint.entity.BillingItem;
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

}
