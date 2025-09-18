package com.innova.restaurant.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.innova.restaurant.controller.RestaurantTableController.CreateTableRequest;
import com.innova.restaurant.controller.RestaurantTableController.UpdateTableRequest;
import com.innova.restaurant.exception.DuplicateResourceException;
import com.innova.restaurant.exception.ResourceNotFoundException;
import com.innova.restaurant.model.entity.Restaurant;
import com.innova.restaurant.model.entity.RestaurantTable;
import com.innova.restaurant.model.enums.TableStatus;
import com.innova.restaurant.repository.jpa.RestaurantRepository;
import com.innova.restaurant.repository.jpa.RestaurantTableRepository;
import com.innova.restaurant.service.RestaurantTableService;

/**
 * Implementación del servicio para gestión de mesas de restaurantes
 */
@Service
@Transactional
public class RestaurantTableServiceImpl implements RestaurantTableService {

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantTable> findByRestaurantId(Long restaurantId) {
        // Verificar que el restaurante existe
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurante no encontrado con ID: " + restaurantId);
        }
        return restaurantTableRepository.findByRestaurantIdOrderByTableNumberAsc(restaurantId);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantTable findTableById(Long tableId, Long restaurantId) {
        RestaurantTable table = restaurantTableRepository.findById(tableId)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + tableId));
        
        // Verificar que la mesa pertenece al restaurante
        if (!table.getRestaurant().getId().equals(restaurantId)) {
            throw new DuplicateResourceException("La mesa no pertenece al restaurante especificado");
        }
        
        return table;
    }

    @Override
    public RestaurantTable createTable(Long restaurantId, CreateTableRequest request) {
        // Buscar el restaurante
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante no encontrado con ID: " + restaurantId));

        // Verificar que no existe ya una mesa con ese número en el restaurante
        if (restaurantTableRepository.existsByRestaurantIdAndTableNumber(restaurantId, request.getTableNumber())) {
            throw new DuplicateResourceException("Ya existe una mesa con el número " + request.getTableNumber() + " en este restaurante");
        }

        // Crear nueva mesa
        RestaurantTable table = new RestaurantTable();
        table.setTableNumber(request.getTableNumber());
        table.setCapacity(request.getCapacity());
        table.setStatus(request.getStatus() != null ? request.getStatus() : TableStatus.AVAILABLE);
        table.setRestaurant(restaurant);
        table.setCreatedAt(LocalDateTime.now());
        table.setUpdatedAt(LocalDateTime.now());

        return restaurantTableRepository.save(table);
    }

    @Override
    public RestaurantTable updateTable(Long tableId, Long restaurantId, UpdateTableRequest request) {
        // Buscar la mesa y verificar que pertenece al restaurante
        RestaurantTable table = findTableById(tableId, restaurantId);

        // Validar número de mesa si se está cambiando
        if (request.getTableNumber() != null && !request.getTableNumber().equals(table.getTableNumber())) {
            if (restaurantTableRepository.existsByRestaurantIdAndTableNumber(restaurantId, request.getTableNumber())) {
                throw new DuplicateResourceException("Ya existe una mesa con el número " + request.getTableNumber() + " en este restaurante");
            }
            table.setTableNumber(request.getTableNumber());
        }

        // Actualizar campos si se proporcionan
        if (request.getCapacity() != null) {
            table.setCapacity(request.getCapacity());
        }

        if (request.getStatus() != null) {
            table.setStatus(request.getStatus());
        }

        table.setUpdatedAt(LocalDateTime.now());

        return restaurantTableRepository.save(table);
    }

    @Override
    public void deleteTable(Long tableId, Long restaurantId) {
        // Buscar la mesa y verificar que pertenece al restaurante
        RestaurantTable table = findTableById(tableId, restaurantId);

        // Verificar que la mesa no tenga reservas activas
        // Esto podríamos implementarlo más adelante si es necesario

        restaurantTableRepository.delete(table);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantTable> findAvailableTablesByRestaurant(Long restaurantId) {
        // Verificar que el restaurante existe
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurante no encontrado con ID: " + restaurantId);
        }
        return restaurantTableRepository.findByRestaurantIdAndStatusOrderByTableNumberAsc(restaurantId, TableStatus.AVAILABLE);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdAndRestaurantId(Long tableId, Long restaurantId) {
        return restaurantTableRepository.existsByIdAndRestaurantId(tableId, restaurantId);
    }
}