package com.example.sayapker.service.impl;

import com.example.sayapker.dto.request.UserRequest;
import com.example.sayapker.dto.response.UserResponse;
import com.example.sayapker.exception.NotFoundException;
import com.example.sayapker.mapper.UserMapper;
import com.example.sayapker.model.entity.User;
import com.example.sayapker.repository.UserRepository;
import com.example.sayapker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public UserResponse create(UserRequest request) {
        User user = UserMapper.toEntity(request);
        return UserMapper.toResponse(repository.save(user));
    }

    @Override
    public UserResponse getById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return UserMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
