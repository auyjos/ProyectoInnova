package com.innova.restaurant.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innova.restaurant.controller.RestaurantController;
import com.innova.restaurant.exception.ResourceNotFoundException;
import com.innova.restaurant.model.entity.Restaurant;
import com.innova.restaurant.model.entity.User;
import com.innova.restaurant.repository.jpa.RestaurantRepository;
import com.innova.restaurant.repository.jpa.UserRepository;
import com.innova.restaurant.service.RestaurantService;

/**
 * Implementación del servicio de restaurantes con query methods sin @Query
 */
@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Page<Restaurant> findAllRestaurants(Pageable pageable) {
        return restaurantRepository.findAll(pageable);
    }

    @Override
    public Restaurant findRestaurantById(Long id) {
        return restaurantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado con ID: " + id));
    }

    @Override
    public Restaurant createRestaurant(RestaurantController.CreateRestaurantRequest request) {
        // Validar que el propietario existe
        User owner = userRepository.findById(request.getOwnerId())
            .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con ID: " + request.getOwnerId()));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setPhone(request.getPhone());
        restaurant.setEmail(request.getEmail());
        restaurant.setOpeningTime(request.getOpeningTime());
        restaurant.setClosingTime(request.getClosingTime());
        restaurant.setMaxCapacity(request.getMaxCapacity());
        restaurant.setOwner(owner);
        restaurant.setIsActive(true);
        restaurant.setCreatedAt(LocalDateTime.now());
        restaurant.setUpdatedAt(LocalDateTime.now());

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long id, RestaurantController.UpdateRestaurantRequest request) {
        Restaurant restaurant = findRestaurantById(id);
        
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setPhone(request.getPhone());
        restaurant.setEmail(request.getEmail());
        restaurant.setOpeningTime(request.getOpeningTime());
        restaurant.setClosingTime(request.getClosingTime());
        restaurant.setMaxCapacity(request.getMaxCapacity());
        restaurant.setUpdatedAt(LocalDateTime.now());

        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant patchRestaurant(Long id, RestaurantController.PatchRestaurantRequest request) {
        Restaurant restaurant = findRestaurantById(id);

        // Solo actualizar campos no nulos
        if (request.getName() != null) {
            restaurant.setName(request.getName());
        }
        if (request.getDescription() != null) {
            restaurant.setDescription(request.getDescription());
        }
        if (request.getAddress() != null) {
            restaurant.setAddress(request.getAddress());
        }
        if (request.getPhone() != null) {
            restaurant.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            restaurant.setEmail(request.getEmail());
        }
        if (request.getOpeningTime() != null) {
            restaurant.setOpeningTime(request.getOpeningTime());
        }
        if (request.getClosingTime() != null) {
            restaurant.setClosingTime(request.getClosingTime());
        }
        if (request.getMaxCapacity() != null) {
            restaurant.setMaxCapacity(request.getMaxCapacity());
        }
        if (request.getIsActive() != null) {
            restaurant.setIsActive(request.getIsActive());
        }

        restaurant.setUpdatedAt(LocalDateTime.now());
        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setIsActive(false); // Soft delete
        restaurant.setUpdatedAt(LocalDateTime.now());
        restaurantRepository.save(restaurant);
    }

    @Override
    public List<Restaurant> searchByName(String name, boolean activeOnly) {
        if (activeOnly) {
            // Query method sin @Query
            return restaurantRepository.findByNameContainingIgnoreCaseAndIsActive(name, true);
        } else {
            return restaurantRepository.findByNameContainingIgnoreCase(name);
        }
    }

    @Override
    public List<Restaurant> findByOwner(Long ownerId) {
        // Validar que el propietario existe
        userRepository.findById(ownerId)
            .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con ID: " + ownerId));

        // Query method sin @Query
        return restaurantRepository.findByOwnerIdAndIsActive(ownerId, true);
    }

    @Override
    public Restaurant updateStatus(Long id, boolean active) {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setIsActive(active);
        restaurant.setUpdatedAt(LocalDateTime.now());
        return restaurantRepository.save(restaurant);
    }
}