package com.example.HealPoint.mapper;

import com.example.HealPoint.entity.Inventory;
import com.example.HealPoint.model.InventoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    InventoryModel inventoryToInventoryModel(Inventory inventory);

    Inventory inventoryModelToInventory(InventoryModel inventoryModel);

    List<InventoryModel> inventoryListToInventoryModelList(List<Inventory> inventoryList);

    Inventory updateInventoryFromInventoryModel(InventoryModel inventoryModel,@MappingTarget Inventory inventory);
}
