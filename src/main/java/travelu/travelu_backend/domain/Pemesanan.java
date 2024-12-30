package travelu.travelu_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class Pemesanan {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String namaCustomer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPembayaran statusPembayaran;

    @Column
    private String panggilan;

    @Column
    private String noTelp;

    @Column
    private String alamat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pelanggan_id_id", nullable = false)
    private Pelanggan pelangganId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pembayaran_id_id")
    private Pembayaran pembayaranId;

    @ManyToMany
    @JoinTable(
            name = "DiskonPemesanan",
            joinColumns = @JoinColumn(name = "pemesananId"),
            inverseJoinColumns = @JoinColumn(name = "diskonId")
    )
    private Set<Diskon> listDiskon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jadwal_id_id", nullable = false)
    private Jadwal jadwalId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
