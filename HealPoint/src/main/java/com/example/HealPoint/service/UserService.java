package com.example.HealPoint.service;

import com.example.HealPoint.entity.Role;
import com.example.HealPoint.entity.User;
import com.example.HealPoint.entity.UserRole;
import com.example.HealPoint.exceptions.DataNotFoundException;
import com.example.HealPoint.exceptions.DataValidationException;
import com.example.HealPoint.mapper.RoleMapper;
import com.example.HealPoint.mapper.UserMapper;
import com.example.HealPoint.model.RoleModel;
import com.example.HealPoint.model.UserModel;
import com.example.HealPoint.repository.RoleRepository;
import com.example.HealPoint.repository.UserRepository;
import com.example.HealPoint.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
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

    public UserModel signUp(UserModel userModel) {
        User addUser = userMapper.userModelToUser(userModel);
        addUser = userRepository.save(addUser);

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

        List<Role> saveRoles =roleInDb.stream().filter(r -> roleIdsFromModel.contains(r.getRoleId())).toList();

        for(Role role : saveRoles){
            UserRole userRole = new UserRole();
            userRole.setUser(addUser);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        UserModel userModelToReturn = userMapper.userToUserModel(addUser);
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
            List<UserRole> userRoles = userRoleRepository.findByUserUserId(user.getUserId());
            List<RoleModel> roleModelList = userRoles.stream().map(r -> roleMapper.roleToRoleModel(r.getRole())).toList();
            userModel.setRoles(roleModelList);
            return userModel;
        }).toList();
        return userModelList;
    }

    public String deleteUser(String userId) {
        userRepository.deleteById(userId);
        return "User deleted successfully";
    }


    @Transactional
    public UserModel updateUser(String userId, UserModel userModel) {

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        userMapper.updateUserModel(userModel, existingUser);
        existingUser.setUserId(userId);

        User savedUser = userRepository.save(existingUser);

        // Fetching Role Id
        List<String> incomingRoleIdsFromModel = userModel.getRoles().stream()
                .map(u -> u.getRoleId()).distinct().toList();

        // Roles From Db
        List<Role> roleInDb = roleRepository.findAllByRoleIdIn(incomingRoleIdsFromModel);
        List<String> roleIdsInDb = roleInDb.stream()
                .map(r -> r.getRoleId())
                .toList();


        // Fetch Existing Roles from user Database
        List<UserRole> existingRoles = userRoleRepository.findByUserUserId((savedUser.getUserId()));
        List<String> existingRoleIds = existingRoles.stream()
                .map(r -> r.getRole().getRoleId())
                .toList();

        // Determine Roles To Remove

        List<String> removeRoleIds = new ArrayList<>();

        for(String roleId : existingRoleIds){
            if(!incomingRoleIdsFromModel.contains(roleId)){
                removeRoleIds.add(roleId);
            }
        }

        if(!removeRoleIds.isEmpty()){
            userRoleRepository.deleteByRoleRoleIdInAndUserUserId(removeRoleIds, savedUser.getUserId());
        }

        // Roles To Add
        List<String> nonAllocateRoleIds = incomingRoleIdsFromModel.stream()
                .filter(roleId -> !existingRoleIds.contains(roleId))
                .toList();

        List<String> invalidRoleIds = new ArrayList<>();
        if (!nonAllocateRoleIds.isEmpty()) {
            for(String roleId : nonAllocateRoleIds) {
                if(!roleIdsInDb.contains(roleId)) {
                    invalidRoleIds.add(roleId);
                }
            }
        }

        if (!invalidRoleIds.isEmpty()) {
            throw new DataValidationException("Invalid Roles" + invalidRoleIds);
        }

        List<Role> updatedRoles = roleInDb.stream().filter(rd -> nonAllocateRoleIds.contains(rd.getRoleId())).toList();

        for (Role role : updatedRoles) {
            if (existingRoleIds.contains(role.getRoleId())) continue;
            UserRole updatedUser = new UserRole();
            updatedUser.setUser(savedUser);
            updatedUser.setRole(role);
            userRoleRepository.save(updatedUser);
        }

        UserModel updatedUserModel = userMapper.userToUserModel(savedUser);

        // Updated List
        List<UserRole> updatedUserRoles = userRoleRepository.findByUserUserId(userId);

        List<RoleModel> updatedRoleModels = updatedUserRoles.stream()
                .map(userRole -> roleMapper.roleToRoleModel(userRole.getRole()))
                .toList();

        updatedUserModel.setRoles(updatedRoleModels);

        return updatedUserModel;
    }

}
