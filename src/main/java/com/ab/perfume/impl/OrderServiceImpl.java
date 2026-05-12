package com.ab.perfume.impl;

import com.ab.perfume.dto.OrderDetailResponseDTO;
import com.ab.perfume.dto.OrderItemRequestDTO;
import com.ab.perfume.dto.OrderRequestDTO;
import com.ab.perfume.dto.OrderResponseDTO;
import com.ab.perfume.entity.Order;
import com.ab.perfume.entity.OrderDetail;
import com.ab.perfume.entity.Product;
import com.ab.perfume.entity.ProductVariant;
import com.ab.perfume.enums.OrderStatus;
import com.ab.perfume.exception.BadRequestException;
import com.ab.perfume.exception.ResourceNotFoundException;
import com.ab.perfume.repository.OrderDetailRepository;
import com.ab.perfume.repository.OrderRepository;
import com.ab.perfume.repository.ProductRepository;
import com.ab.perfume.repository.ProductVariantRepository;
import com.ab.perfume.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public OrderResponseDTO create(OrderRequestDTO request) {

        BigDecimal total = BigDecimal.ZERO;
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .total(BigDecimal.ZERO)
                .status(OrderStatus.PENDING)
                .build();

        order = orderRepository.save(order);
        List<OrderDetailResponseDTO> detailResponses = new ArrayList<>();
        for (OrderItemRequestDTO item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Producto no encontrado")
                    );

            ProductVariant variant = null;
            BigDecimal price;
            if (item.getVariantId() != null) {
                variant = productVariantRepository.findById(item.getVariantId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Variante no encontrada")
                        );

                if (variant.getStock() < item.getQuantity()) {
                    throw new BadRequestException(
                            "Stock insuficiente para la variante"
                    );
                }

                price = variant.getPrice();
                variant.setStock(
                        variant.getStock() - item.getQuantity()
                );

                productVariantRepository.save(variant);
            } else {
                if (product.getStock() < item.getQuantity()) {
                    throw new BadRequestException(
                            "Stock insuficiente para el producto"
                    );

                }
                price = product.getPrice();
                product.setStock(
                        product.getStock() - item.getQuantity()
                );

                productRepository.save(product);

            }

            BigDecimal subtotal = price.multiply(
                    BigDecimal.valueOf(item.getQuantity())
            );

            total = total.add(subtotal);
            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .variant(variant)
                    .quantity(item.getQuantity())
                    .price(price)
                    .subtotal(subtotal)
                    .build();
            orderDetailRepository.save(detail);
            detailResponses.add(
                    OrderDetailResponseDTO.builder()
                            .productId(product.getId())
                            .productName(product.getName())
                            .variantId(
                                    variant != null ? variant.getId() : null
                            )
                            .variantName(
                                    variant != null ? variant.getVariantName() : null
                            )
                            .quantity(item.getQuantity())
                            .price(price)
                            .subtotal(subtotal)
                            .build()
            );

        }

        order.setTotal(total);
        orderRepository.save(order);
        return OrderResponseDTO.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .phone(order.getPhone())
                .address(order.getAddress())
                .total(order.getTotal())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .details(detailResponses)
                .build();

    }

    @Override
    public List<OrderResponseDTO> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public OrderResponseDTO findById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Pedido no encontrado")
                );

        return toResponse(order);

    }

    private OrderResponseDTO toResponse(Order order) {
        List<OrderDetailResponseDTO> details =
                orderDetailRepository.findByOrderId(order.getId())
                        .stream()
                        .map(detail ->
                                OrderDetailResponseDTO.builder()
                                        .productId(detail.getProduct().getId())
                                        .productName(detail.getProduct().getName())
                                        .variantId(
                                                detail.getVariant() != null
                                                        ? detail.getVariant().getId()
                                                        : null
                                        )
                                        .variantName(
                                                detail.getVariant() != null
                                                        ? detail.getVariant().getVariantName()
                                                        : null
                                        )
                                        .quantity(detail.getQuantity())
                                        .price(detail.getPrice())
                                        .subtotal(detail.getSubtotal())
                                        .build()
                        )
                        .toList();

        return OrderResponseDTO.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .phone(order.getPhone())
                .address(order.getAddress())
                .total(order.getTotal())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .details(details)
                .build();
    }

}
