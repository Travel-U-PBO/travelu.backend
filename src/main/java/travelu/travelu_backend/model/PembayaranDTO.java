package travelu.travelu_backend.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PembayaranDTO {

    private Long id;

    private PaymentMethod metode;

    private Double harga;

    @Size(max = 255)
    private String noInvoice;

}
