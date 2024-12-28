package travelu.travelu_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import travelu.travelu_backend.model.StatusPembayaran;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class InvoicePembayaran {

    @Id
    @Column(nullable = false, updatable = false)
    private String noInvoice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPembayaran status;

    @Column(nullable = false)
    private Double harga;

    @OneToMany(mappedBy = "noInvoice")
    private Set<Pemesanan> listPemesanan;

    @OneToOne(mappedBy = "noInvoice", fetch = FetchType.LAZY)
    private Pembayaran pembayaranId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
