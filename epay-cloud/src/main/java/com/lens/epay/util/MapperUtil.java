package com.lens.epay.util;

import com.lens.epay.exception.BadRequestException;
import com.lens.epay.mapper.ProductMapper;
import com.lens.epay.model.entity.Address;
import com.lens.epay.model.entity.Product;
import com.lens.epay.model.entity.User;
import com.lens.epay.model.resource.product.ProductResource;
import com.lens.epay.repository.AddressRepository;
import com.lens.epay.repository.ProductRepository;
import com.lens.epay.repository.UserRepository;
import com.lens.epay.security.JwtResolver;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.lens.epay.constant.ErrorConstants.ID_IS_NOT_EXIST;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */
@Component
public class MapperUtil {

    @Autowired
    private JwtResolver resolver;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProductMapper productMapper;

    @Named("tokenToUser")
    public User tokenToUser(String token) {
        return userRepository.findUserById(resolver.getIdFromToken(token));
    }

    public Map<Product, Short> basketToEntity(Map<UUID, Short> productIdQuantity) {
        Map<Product, Short> productQuantity = new HashMap<>();
        productIdQuantity.entrySet().forEach(x -> {
            Product product = productRepository.findOneById(x.getKey());
            productQuantity.put(product, x.getValue());
        });
        return productQuantity;
    }

//    @Named("entityToBasket")
    public Map<ProductResource, Short> entityToBasket(Map<Product, Short> productQuantity) {
        Map<ProductResource, Short> productQuantityResource = new HashMap<>();
        productQuantity.entrySet().forEach(x -> {
            productQuantityResource.put(productMapper.toResource(x.getKey()), x.getValue());
        });
        return productQuantityResource;
    }

//    @Named("IdToAddress")
    public Address IdToAddress(UUID id) {
        Address address;
        try {
            address = addressRepository.findOneById(id);
        } catch (NullPointerException e) {
            throw new BadRequestException(ID_IS_NOT_EXIST);
        }
        return address;
    }
}
