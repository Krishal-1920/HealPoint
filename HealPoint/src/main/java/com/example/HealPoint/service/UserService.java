package com.example.HealPoint.service;

import com.example.HealPoint.entity.*;
import com.example.HealPoint.enums.Status;
import com.example.HealPoint.exceptions.DataValidationException;
import com.example.HealPoint.mapper.RoleMapper;
import com.example.HealPoint.mapper.SpecialistMapper;
import com.example.HealPoint.mapper.UserMapper;
import com.example.HealPoint.model.RoleModel;
import com.example.HealPoint.model.SpecialistModel;
import com.example.HealPoint.model.UserModel;
import com.example.HealPoint.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final RoleMapper roleMapper;

    private final PasswordEncoder passwordEncoder;

    private final SpecialistRepository specialistRepository;

    private final UserSpecialistRepository userSpecialistRepository;

    private final SpecialistMapper specialistMapper;


    public UserModel signUp(UserModel userModel) {
        User addUser = userMapper.userModelToUser(userModel);
        addUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
        addUser = userRepository.save(addUser);


        // Specialist
        List<String> specialistIdsFromModel = userModel.getSpeciality().stream().map(s -> s.getSpecialistId()).toList();

        List<Specialist> specialistInDb = specialistRepository.findAllBySpecialistIdIn(specialistIdsFromModel);

        List<String> specialistIdsInDb = specialistInDb.stream().map(s -> s.getSpecialistId()).toList();
        List<String> invalidSpecialists = new ArrayList<>();

        for(String specialistId : specialistIdsFromModel){
            if(!specialistIdsInDb.contains(specialistId)){
                invalidSpecialists.add(specialistId);
            }
        }

        if(!invalidSpecialists.isEmpty()){
            throw new DataValidationException("Invalid Specialists : " + invalidSpecialists);
        }

        List<Specialist> saveSpecialists = specialistInDb.stream().filter(s -> specialistIdsFromModel.contains(s.getSpecialistId())).toList();

        for(Specialist specialist : saveSpecialists){
            UserSpecialist userSpecialist = new UserSpecialist();
            userSpecialist.setUser(addUser);
            userSpecialist.setSpecialist(specialist);
            userSpecialistRepository.save(userSpecialist);
        }

        // Role
        List<String> roleIdsFromModel = userModel.getRoles().stream().map(r -> r.getRoleId()).toList();

        List<Role> roleInDb = roleRepository.findAllByRoleIdIn(roleIdsFromModel);

        List<String> roleIdsInDb = roleInDb.stream().map(r -> r.getRoleId()).toList();
        List<String> invalidRoles = new ArrayList<>();

        for (String roleId : roleIdsFromModel) {
            if (!roleIdsInDb.contains(roleId)) {
                invalidRoles.add(roleId);
            }
        }

        if (!invalidRoles.isEmpty()) {
            throw new DataValidationException("Invalid roles: " + invalidRoles);
        }

        List<Role> saveRoles = roleInDb.stream().filter(r -> roleIdsFromModel.contains(r.getRoleId())).toList();

        for(Role role : saveRoles){
            UserRole userRole = new UserRole();
            userRole.setUser(addUser);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }


        // Response

        UserModel userModelToReturn = userMapper.userToUserModel(addUser);

        List<UserSpecialist> userSpecialist = userSpecialistRepository.findByUserUserId(addUser.getUserId());
        List<SpecialistModel> specialistList = new ArrayList<>();
        userSpecialist.forEach(us -> specialistList.add(userMapper.specialistToSpecialistModel(us.getSpecialist())));
        userModelToReturn.setSpeciality(specialistList);

        List<UserRole> byUserUserId = userRoleRepository.findByUserUserId(addUser.getUserId());
        List<RoleModel> roleList = new ArrayList<>();
        byUserUserId.forEach(ur -> roleList.add(roleMapper.roleToRoleModel(ur.getRole())));
        userModelToReturn.setRoles(roleList);

        return userModelToReturn;
    }


    public List<UserModel> getAllUsers(String search) {
        List<User> usersList = userRepository.searchUsers(search);
        List<UserModel> userModelList = usersList.stream().map(user -> {
            UserModel userModel = userMapper.userToUserModel(user);
            // Specialist List
            List<UserSpecialist> userSpecialists = userSpecialistRepository.findByUserUserId(user.getUserId());
            userSpecialists.stream()
                    .filter(s -> s.getStatus() == Status.ACTIVE)
                    .map(s -> specialistMapper.specialistToSpecialistModel(s.getSpecialist())).toList();

            // User List
            List<UserRole> userRoles = userRoleRepository.findByUserUserId(user.getUserId());
            List<RoleModel> roleModelList = userRoles.stream().map(r -> roleMapper.roleToRoleModel(r.getRole())).toList();
            userModel.setRoles(roleModelList);
            return userModel;
        }).toList();
        return userModelList;
    }


    public String deleteUser(String email) {
        User byEmail = userRepository.findByEmail(email);

        userRepository.delete(byEmail);
        return "User deleted successfully";
    }


    @Transactional
    public UserModel updateProfile(String email, UserModel userModel) {

        User existingUser = userRepository.findByEmail(email);
        userMapper.updateUserModel(userModel, existingUser);

        existingUser.setUserId(existingUser.getUserId());
        existingUser.setPassword(passwordEncoder.encode(userModel.getPassword()));
        User savedUser = userRepository.save(existingUser);

        // User - Specialist
        List<String> incomingSpecialistIdsFromModel = userModel.getSpeciality().stream()
                .map(s -> s.getSpecialistId())
                .distinct()
                .toList();

        // Fetch valid specialists from DB
        List<Specialist> specialistInDb = specialistRepository.findAllBySpecialistIdIn(incomingSpecialistIdsFromModel);
        List<String> specialistIdsInDb = specialistInDb.stream()
                .map(Specialist -> Specialist.getSpecialistId())
                .toList();

        // detect invalid specialist IDs not present in DB
        List<String> invalidSpecialistIds = incomingSpecialistIdsFromModel.stream()
                .filter(id -> !specialistIdsInDb.contains(id))
                .toList();
        if (!invalidSpecialistIds.isEmpty()) {
            throw new DataValidationException("Invalid Specialists: " + invalidSpecialistIds);
        }

        // Fetch existing UserSpecialists
        List<UserSpecialist> existingUserSpecialists = userSpecialistRepository.findByUserUserId(savedUser.getUserId());

        statusUpdate(incomingSpecialistIdsFromModel, existingUserSpecialists);

        userSpecialistRepository.saveAll(existingUserSpecialists);

        // Add new specialists if not already assigned
        List<String> existingSpecialistIds = existingUserSpecialists.stream()
                .map(u -> u.getSpecialist().getSpecialistId())
                .toList();

        List<Specialist> newSpecialistsToAdd = specialistInDb.stream()
                .filter(s -> !existingSpecialistIds.contains(s.getSpecialistId()))
                .toList();

        for (Specialist specialist : newSpecialistsToAdd) {
            UserSpecialist newUserSpecialist = new UserSpecialist();
            newUserSpecialist.setUser(savedUser);
            newUserSpecialist.setSpecialist(specialist);
            newUserSpecialist.setStatus(Status.ACTIVE);
            userSpecialistRepository.save(newUserSpecialist);
        }


        // User - Role
        List<String> incomingRoleIdsFromModel = userModel.getRoles().stream()
                .map(u -> u.getRoleId())
                .distinct()
                .toList();

        // Fetch valid Roles from DB
        List<Role> roleInDb = roleRepository.findAllByRoleIdIn(incomingRoleIdsFromModel);
        List<String> roleIdsInDb = roleInDb.stream()
                .map(Role::getRoleId)
                .toList();

        // Fetch existing roles from UserRole table
        List<UserRole> existingRoles = userRoleRepository.findByUserUserId(savedUser.getUserId());
        List<String> existingRoleIds = existingRoles.stream()
                .map(r -> r.getRole().getRoleId())
                .toList();

        // Determine roles to remove (existing but not in incoming)
        List<String> removeRoleIds = new ArrayList<>();
        for (String roleId : existingRoleIds) {
            if (!incomingRoleIdsFromModel.contains(roleId)) {
                removeRoleIds.add(roleId);
            }
        }

        if (!removeRoleIds.isEmpty()) {
            userRoleRepository.deleteByRoleRoleIdInAndUserUserId(removeRoleIds, savedUser.getUserId());
        }

        // Determine new roles to add (in incoming but not already existing)
        List<String> nonAllocatedRoleIds = incomingRoleIdsFromModel.stream()
                .filter(roleId -> !existingRoleIds.contains(roleId))
                .toList();

        // Validate new roles
        List<String> invalidRoleIds = new ArrayList<>();
        for (String roleId : nonAllocatedRoleIds) {
            if (!roleIdsInDb.contains(roleId)) {
                invalidRoleIds.add(roleId);
            }
        }

        if (!invalidRoleIds.isEmpty()) {
            throw new DataValidationException("Invalid Roles: " + invalidRoleIds);
        }

        // Save only new roles to UserRole table
        List<Role> updatedRoles = roleInDb.stream()
                .filter(rd -> nonAllocatedRoleIds.contains(rd.getRoleId()))
                .toList();

        for (Role role : updatedRoles) {
            // Skip adding if already exists
            if (existingRoleIds.contains(role.getRoleId())) continue;

            UserRole updatedUserRole = new UserRole();
            updatedUserRole.setUser(savedUser);
            updatedUserRole.setRole(role);
            userRoleRepository.save(updatedUserRole);
        }

        // Response
        UserModel updatedUserModel = userMapper.userToUserModel(savedUser);
        // Fetch updated roles using userId (not email)
        List<UserRole> updatedUserRoles = userRoleRepository.findByUserUserId(savedUser.getUserId());
        List<RoleModel> updatedRoleModels = updatedUserRoles.stream()
                .map(userRole -> roleMapper.roleToRoleModel(userRole.getRole()))
                .toList();

        updatedUserModel.setRoles(updatedRoleModels);

        // Fetch updated Specialists using userId (not email)
        List<UserSpecialist> updatedUserSpecialists = userSpecialistRepository.findByUserUserId(savedUser.getUserId());
        List<SpecialistModel> updatedSpecialistModels = updatedUserSpecialists.stream()
                .filter(s -> s.getStatus() == Status.ACTIVE)
               .map(userSpecialist -> userMapper.specialistToSpecialistModel(userSpecialist.getSpecialist()))
               .toList();
        updatedUserModel.setSpeciality(updatedSpecialistModels);

        return updatedUserModel;
    }


    public void statusUpdate(List<String> incomingSpecialistIdsFromModel, List<UserSpecialist> existingSpecialistInDb) {
        for (UserSpecialist userSpecialist : existingSpecialistInDb) {
            String specialistId = userSpecialist.getSpecialist().getSpecialistId();
            if (!incomingSpecialistIdsFromModel.contains(specialistId)) {
                userSpecialist.setStatus(Status.INACTIVE);
            }
            else {
                userSpecialist.setStatus(Status.ACTIVE);
            }
        }
    }

}
