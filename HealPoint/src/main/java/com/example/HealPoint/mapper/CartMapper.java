package com.example.HealPoint.mapper;

import com.example.HealPoint.entity.Cart;
import com.example.HealPoint.model.CartModel;
import com.example.HealPoint.model.ItemListModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartModel cartToCartModel(Cart cart);

    Cart cartModelToCart(CartModel cartModel);

    @Mapping(source = "inventory.productName", target = "productName")
    @Mapping(source = "inventory.itemCategory", target = "itemCategory")
    @Mapping(expression = "java(String.valueOf(cart.getQuantity()))", target = "itemQuantity")
    @Mapping(expression = "java(String.valueOf(cart.getInventory().getItemPrice()))", target = "itemPrice")
    ItemListModel cartToItemListModel(Cart cart);

}
