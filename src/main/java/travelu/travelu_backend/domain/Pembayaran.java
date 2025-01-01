package travelu.travelu_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import travelu.travelu_backend.model.PaymentMethod;
import travelu.travelu_backend.model.StatusPembayaran;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Pembayaran {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentMethod metode;

    @Column
    private Double harga;

    @Column
    private String noInvoice;

    @Column
    private String name;

    @Column
    private String noTelp;

    @Column
    private String email;

    @Column
    private String address;

    @Column
    private String invoice;

    @Column
    private String vAnumber;

    @Column
    private LocalDateTime expiredDate;

    @Column
    @Enumerated(EnumType.STRING)
    private StatusPembayaran status;

    @OneToMany(mappedBy = "pembayaranId")
    private Set<Pemesanan> listPemesanan;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
