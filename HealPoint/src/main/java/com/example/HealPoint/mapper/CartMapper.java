package com.example.HealPoint.mapper;

import com.example.HealPoint.entity.Cart;
import com.example.HealPoint.model.CartModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);

    CartModel cartToCartModel(Cart cart);

    Cart cartModelToCart(CartModel cartModel);

}
