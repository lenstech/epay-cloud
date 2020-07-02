package com.lens.epay.service;

import com.iyzipay.Options;
import com.iyzipay.model.Address;
import com.iyzipay.model.*;
import com.iyzipay.request.CreateCancelRequest;
import com.iyzipay.request.CreatePaymentRequest;
import com.iyzipay.request.RetrieveInstallmentInfoRequest;
import com.iyzipay.request.RetrievePaymentRequest;
import com.lens.epay.exception.NotFoundException;
import com.lens.epay.model.dto.CreditCardInstallmentCheckDto;
import com.lens.epay.model.dto.sale.OrderDto;
import com.lens.epay.model.entity.*;
import com.lens.epay.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.lens.epay.constant.ErrorConstants.ADDRESS_NOT_EXIST;

/**
 * Created by Emir Gökdemir
 * on 19 Nis 2020
 */

@Service
public class PaymentService {

    @Autowired
    private AddressRepository addressRepository;

    public InstallmentInfo checkCardInstallment(CreditCardInstallmentCheckDto dto) {
        RetrieveInstallmentInfoRequest request = new RetrieveInstallmentInfoRequest();
        request.setLocale(Locale.TR.getValue());
        request.setBinNumber(dto.getCardBinNumber());
        request.setPrice(dto.getPrice());
        request.setConversationId("123456789");
        Options options = new Options();
        InstallmentInfo installmentInfo = InstallmentInfo.retrieve(request, setOptions(options));
        return installmentInfo;
    }

    private Options setOptions(Options options) {
        options.setApiKey("sandbox-FXQgNXYvyZtFAJf1ZokfE8i7kA1KbVUQ");
        options.setSecretKey("sandbox-1lXTgvsDQZ5LgKezahaNUYRbMkORnyGV");
        options.setBaseUrl("https://sandbox-api.iyzipay.com");
        return options;
    }

    public Payment getPayment(CreditCardTransaction creditCardTransaction) {
        RetrievePaymentRequest retrievePaymentRequest = new RetrievePaymentRequest();
        retrievePaymentRequest.setPaymentId(creditCardTransaction.getIyzicoPaymentId());
        Options options = new Options();
        return Payment.retrieve(retrievePaymentRequest, setOptions(options));
    }

    public Payment payByCard(OrderDto orderDto, User user, Order order) {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setLocale(Locale.TR.getValue());
        request.setConversationId(order.getId().toString());
        request.setPrice(new BigDecimal(orderDto.getTotalPrice().toString()));
        request.setPaidPrice(new BigDecimal(orderDto.getTotalPrice().toString()));
        request.setCurrency(orderDto.getCurrency().name());
        request.setInstallment(orderDto.getInstallmentNumber());
        request.setBasketId(order.getBasketObjects().get(0).getId().toString());
        request.setPaymentChannel(PaymentChannel.WEB.name());
        request.setPaymentGroup(PaymentGroup.PRODUCT.name());

        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardHolderName(orderDto.getCreditCardHolderName());
        paymentCard.setCardNumber(orderDto.getCreditCardNumber());
        paymentCard.setExpireMonth(orderDto.getExpireMonth());
        paymentCard.setExpireYear(orderDto.getExpireYear());
        paymentCard.setCvc(orderDto.getCvc());
        paymentCard.setRegisterCard(0);
        request.setPaymentCard(paymentCard);

        com.lens.epay.model.entity.Address deliveryAddress = addressRepository.findOneById(orderDto.getDeliveryAddressId());
        com.lens.epay.model.entity.Address invoiceAddress = addressRepository.findOneById(orderDto.getInvoiceAddressId());
        if (deliveryAddress == null || invoiceAddress == null) {
            throw new NotFoundException(ADDRESS_NOT_EXIST);
        }
        Buyer buyer = new Buyer();
        buyer.setId(user.getId().toString());
        buyer.setName(user.getName());
        buyer.setSurname(user.getSurname());
        buyer.setGsmNumber(user.getSurname());
        buyer.setEmail(user.getEmail());
        buyer.setIdentityNumber(invoiceAddress.getIdentityNo());
        buyer.setRegistrationAddress(invoiceAddress.toStringForTurkishAddress());
        buyer.setIp(orderDto.getIpAddress());
        buyer.setCity(invoiceAddress.getCity());
        buyer.setCountry(invoiceAddress.getCountry());
        request.setBuyer(buyer);

        Address shippingAddress = new Address();
        shippingAddress.setContactName(deliveryAddress.getName());
        shippingAddress.setCity(deliveryAddress.getCity());
        shippingAddress.setCountry(deliveryAddress.getCountry());
        shippingAddress.setAddress(deliveryAddress.toStringForTurkishAddress());
        request.setShippingAddress(shippingAddress);

        Address billingAddress = new Address();
        billingAddress.setContactName(invoiceAddress.getName());
        billingAddress.setCity(invoiceAddress.getCity());
        billingAddress.setCountry(invoiceAddress.getCountry());
        billingAddress.setAddress(invoiceAddress.toStringForTurkishAddress());
        request.setBillingAddress(billingAddress);

        List<BasketItem> basketItems = new ArrayList<BasketItem>();
        for (BasketObject object : order.getBasketObjects()) {
            for (int i = 0; i < object.getProductQuantity(); i++) {
                basketItems.add(productToBasketItem(object.getProduct()));
            }
        }
        request.setBasketItems(basketItems);
        Options options = new Options();
        return Payment.create(request, setOptions(options));
    }

    private BasketItem productToBasketItem(Product product) {
        BasketItem basketItem = new BasketItem();
        basketItem.setId(product.getId().toString());
        basketItem.setName(product.getName());
        basketItem.setCategory1(product.getCategory().getName());
        basketItem.setItemType(BasketItemType.PHYSICAL.name());
        basketItem.setPrice(new BigDecimal(product.getPrice().toString()));
        return basketItem;
    }

    public Cancel repayByCard(Order order) {
        CreateCancelRequest request = new CreateCancelRequest();
        request.setLocale(Locale.TR.getValue());
        request.setConversationId(order.getId().toString());
        CreditCardTransaction transaction = order.getCreditCardTransaction();
        request.setPaymentId(transaction.getIyzicoPaymentId());
        request.setIp(transaction.getIpAddress());
        Options options = new Options();
        return Cancel.create(request, setOptions(options));
    }
}
