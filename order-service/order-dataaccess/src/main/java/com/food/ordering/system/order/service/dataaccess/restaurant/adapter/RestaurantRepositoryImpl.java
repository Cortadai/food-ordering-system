package com.food.ordering.system.order.service.dataaccess.restaurant.adapter;

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.ordering.system.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private RestaurantDataAccessMapper restaurantDataAccessMapper;
    private RestaurantJpaRepository restaurantJpaRepository;

    public RestaurantRepositoryImpl(RestaurantDataAccessMapper restaurantDataAccessMapper,
                                    RestaurantJpaRepository restaurantJpaRepository) {
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
        this.restaurantJpaRepository = restaurantJpaRepository;
    }

    @Override
    public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
        List<UUID> restaurantProducts =
                restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        Optional<List<RestaurantEntity>> restaurantEntities = restaurantJpaRepository
                .findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), restaurantProducts);
        return restaurantEntities.map(restaurantEntity -> restaurantDataAccessMapper
                .restaurantEntityToRestaurant(restaurantEntity));
    }
}
