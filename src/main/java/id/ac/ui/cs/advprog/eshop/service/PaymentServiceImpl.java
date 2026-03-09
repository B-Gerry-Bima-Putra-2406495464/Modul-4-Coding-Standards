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
        String status = "SUCCESS";

        if (method.equals("VOUCHER")) {
            String voucherCode = paymentData.get("voucherCode");
            if (voucherCode == null || voucherCode.length() != 16 || !voucherCode.startsWith("ESHOP")) {
                status = "REJECTED";
            } else {
                long digitCount = voucherCode.chars().filter(Character::isDigit).count();
                if (digitCount != 8) {
                    status = "REJECTED";
                }
            }
        } else if (method.equals("BANK_TRANSFER")) {
            String bankName = paymentData.get("bankName");
            String referenceCode = paymentData.get("referenceCode");
            if (bankName == null || bankName.isBlank() || referenceCode == null || referenceCode.isBlank()) {
                status = "REJECTED";
            }
        }

        Payment payment = new Payment(UUID.randomUUID().toString(), method, status, paymentData);
        return paymentRepository.save(payment);
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