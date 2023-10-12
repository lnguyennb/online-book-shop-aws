package com.kas.online_book_shop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kas.online_book_shop.enums.AccountState;
import com.kas.online_book_shop.enums.Role;
import com.kas.online_book_shop.exception.ResourceNotFoundException;
import com.kas.online_book_shop.model.User;
import com.kas.online_book_shop.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Người dùng tương ứng không tồn tại"));
    }

    @Override
    public Page<User> getUserByRole(Role role, Pageable page) {
        // TODO
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tai khoan."));
    }

    @Override
    public User updateUser(User user) {
        var existingUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Khong tim thay user tuong ung"));
        existingUser.setAddress(user.getAddress());
        existingUser.setFullName(user.getFullName());
        existingUser.setProvince(user.getProvince());
        existingUser.setDistrict(user.getDistrict());
        existingUser.setWard(user.getWard());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        return existingUser;
    }

    @Override
    public void setAccountState(Long userId, String state) {
        var existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tai khoan."));
        existingUser.setState(AccountState.valueOf(state));
    }

    @Override
    public void setRole(Long userId, String role) {
        var existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tai khoan."));
        existingUser.setRole(Role.valueOf(role));
    }

    @Override
    public Page<User> getCustomerByState(AccountState state, Pageable pageable) {
        return userRepository.findByRoleAndState(Role.USER, state, pageable);
    }

    
}
