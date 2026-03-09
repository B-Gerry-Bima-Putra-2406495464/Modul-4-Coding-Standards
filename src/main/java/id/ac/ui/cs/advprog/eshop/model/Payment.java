package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import java.util.Arrays;
import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, String method, String status, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.paymentData = paymentData;

        String[] validStatus = {"WAITING_PAYMENT", "SUCCESS", "REJECTED"};
        if (Arrays.stream(validStatus).noneMatch(s -> s.equals(status))) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }
}
