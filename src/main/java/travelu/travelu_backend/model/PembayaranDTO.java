package travelu.travelu_backend.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PembayaranDTO {

    private Long id;

    private PaymentMethod metode;

    private Double harga;

    @NotNull
    @Size(max = 255)
    @PembayaranNoInvoiceUnique
    private String noInvoice;

}
