package travelu.travelu_backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import travelu.travelu_backend.domain.Pelanggan;


public interface PelangganRepository extends JpaRepository<Pelanggan, Long> {
}
