package travelu.travelu_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
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

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String noTelp;

    @Size(max = 255)
    private String email;

    @Size(max = 255)
    private String address;

    @Size(max = 255)
    private String invoice;

    @Size(max = 255)
    @JsonProperty("vAnumber")
    private String vAnumber;

    private LocalDateTime expiredDate;

    private StatusPembayaran status;

}
