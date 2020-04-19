package com.lens.epay.service;

import com.iyzipay.Options;
import com.iyzipay.model.InstallmentInfo;
import com.iyzipay.model.Locale;
import com.iyzipay.request.RetrieveInstallmentInfoRequest;
import com.lens.epay.enums.PaymentType;
import com.lens.epay.model.dto.CreditCardInstallmentCheckDto;
import com.lens.epay.model.dto.sale.OrderDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 19 Nis 2020
 */

@Service
public class PaymentService {

    public InstallmentInfo checkCardInstallment(CreditCardInstallmentCheckDto dto){
        RetrieveInstallmentInfoRequest request = new RetrieveInstallmentInfoRequest();
        request.setLocale(Locale.TR.getValue());
        request.setBinNumber(dto.getCardBinNumber());
        request.setPrice(dto.getPrice());
        request.setConversationId("123456789");
        Options options = new Options();
        InstallmentInfo installmentInfo = InstallmentInfo.retrieve(request,setOptions(options));
        return installmentInfo;
    }

    private Options setOptions(Options options){
        options.setApiKey("sandbox-MBXppVf6qEt2hR0iOH8jvQABNSnvxhYb");
        options.setSecretKey("sandbox-OREs9JPYjXy84X9pP487L8tQhMlF1JW8");
        options.setBaseUrl("https://sandbox-api.iyzipay.com");
        return options;
    }

    public boolean payByCard(OrderDto orderDto, UUID userId){
        // TODO: 19 Nis 2020 pos sistemine göre yapılacak
        return true;
    }

    public boolean repayByCard( UUID orderId){
        // TODO: 19 Nis 2020 pos sistemine göre yapılacak
        return true;
    }
}
