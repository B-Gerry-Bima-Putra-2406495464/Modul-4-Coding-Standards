package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String status = validatePayment(method, paymentData);
        Payment payment = new Payment(UUID.randomUUID().toString(), method, status, paymentData);
        return paymentRepository.save(payment);
    }

    private String validatePayment(String method, Map<String, String> paymentData) {
        if (method.equals("VOUCHER")) {
            return isVoucherValid(paymentData.get("voucherCode")) ? "SUCCESS" : "REJECTED";
        } else if (method.equals("BANK_TRANSFER")) {
            return isBankTransferValid(paymentData) ? "SUCCESS" : "REJECTED";
        }
        return "SUCCESS";
    }

    private boolean isVoucherValid(String code) {
        return code != null &&
                code.length() == 16 &&
                code.startsWith("ESHOP") &&
                code.chars().filter(Character::isDigit).count() == 8;
    }

    private boolean isBankTransferValid(Map<String, String> data) {
        String bank = data.get("bankName");
        String ref = data.get("referenceCode");
        return bank != null && !bank.isBlank() && ref != null && !ref.isBlank();
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment = new Payment(payment.getId(), payment.getMethod(), status, payment.getPaymentData());
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}