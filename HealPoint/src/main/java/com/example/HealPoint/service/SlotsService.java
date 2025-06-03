package com.example.HealPoint.service;

import com.example.HealPoint.entity.Slots;
import com.example.HealPoint.entity.User;
import com.example.HealPoint.enums.Status;
import com.example.HealPoint.mapper.SlotsMapper;
import com.example.HealPoint.model.SlotsModel;
import com.example.HealPoint.repository.SlotsRepository;
import com.example.HealPoint.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotsService {

    private final UserRepository userRepository;

    private final SlotsMapper slotMapper;

    private final SlotsRepository slotsRepository;


    public SlotsModel createSlot(SlotsModel slotsModel) {
        User user = userRepository.findById(slotsModel.getProviderId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Slots slot = slotMapper.slotsModelToSlots(slotsModel);

        slot.setUser(user);
        slot.setProviderUsername(user.getUsername());
        slot.setStatus(Status.AVAILABLE);

        slotsRepository.save(slot);

        return slotMapper.slotsToSlotsModel(slot);
    }


    public List<SlotsModel> getAllSlots(String search) {
        List<Slots> slots = slotsRepository.searchSlots(search);
        return slots.stream()
                .map(slot -> slotMapper.slotsToSlotsModel(slot)).toList();
    }

    public String deleteSlot(String slotId) {
        Slots slot = slotsRepository.findById(slotId)
               .orElseThrow(() -> new RuntimeException("Slot Not Found"));
        slotsRepository.deleteById(slotId);
        return "Slot of Dr. " + slot.getProviderUsername() + " deleted Successfully";
    }


    public SlotsModel updateSlot(String slotId, SlotsModel slotsModel) {
        Slots slots = slotsRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slots Not found"));

        slotMapper.updateSlotsModel(slotsModel, slots);

        User user = userRepository.findById(slotsModel.getProviderId())
                .orElseThrow(() -> new RuntimeException("User Not found"));

        slots.setUser(user);
        slots.setProviderUsername(user.getUsername());
        slots.setStatus(Status.AVAILABLE);

        Slots updatedSlots = slotsRepository.save(slots);
        return slotMapper.slotsToSlotsModel(updatedSlots);
    }
}
