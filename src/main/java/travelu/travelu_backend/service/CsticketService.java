package travelu.travelu_backend.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import travelu.travelu_backend.domain.Csticket;
import travelu.travelu_backend.domain.Pelanggan;
import travelu.travelu_backend.domain.Pemesanan;
import travelu.travelu_backend.model.CsticketDTO;
import travelu.travelu_backend.repos.AdminRepository;
import travelu.travelu_backend.repos.CsticketRepository;
import travelu.travelu_backend.repos.PelangganRepository;
import travelu.travelu_backend.repos.PemesananRepository;
import travelu.travelu_backend.util.NotFoundException;


@Service
@Transactional
public class CsticketService {

    private final CsticketRepository csticketRepository;
    private final PemesananRepository pemesananRepository;
    private final PelangganRepository pelangganRepository;
    private final AdminRepository adminRepository;

    public CsticketService(final CsticketRepository csticketRepository,
            final PemesananRepository pemesananRepository,
            final PelangganRepository pelangganRepository, final AdminRepository adminRepository) {
        this.csticketRepository = csticketRepository;
        this.pemesananRepository = pemesananRepository;
        this.pelangganRepository = pelangganRepository;
        this.adminRepository = adminRepository;
    }

    public List<CsticketDTO> findAll() {
        final List<Csticket> cstickets = csticketRepository.findAll(Sort.by("id"));
        return cstickets.stream()
                .map(csticket -> mapToDTO(csticket, new CsticketDTO()))
                .toList();
    }

    public CsticketDTO get(final Long id) {
        return csticketRepository.findById(id)
                .map(csticket -> mapToDTO(csticket, new CsticketDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CsticketDTO csticketDTO) {
        final Csticket csticket = new Csticket();
        mapToEntity(csticketDTO, csticket);
        return csticketRepository.save(csticket).getId();
    }

    public void update(final Long id, final CsticketDTO csticketDTO) {
        final Csticket csticket = csticketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(csticketDTO, csticket);
        csticketRepository.save(csticket);
    }

    public void delete(final Long id) {
        final Csticket csticket = csticketRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        adminRepository.findAllByListComplain(csticket)
                .forEach(admin -> admin.getListComplain().remove(csticket));
        csticketRepository.delete(csticket);
    }

    private CsticketDTO mapToDTO(final Csticket csticket, final CsticketDTO csticketDTO) {
        csticketDTO.setId(csticket.getId());
        csticketDTO.setRating(csticket.getRating());
        csticketDTO.setContent(csticket.getContent());
        csticketDTO.setStatus(csticket.getStatus());
        csticketDTO.setTitle(csticket.getTitle());
        csticketDTO.setResponse(csticket.getResponse());
        csticketDTO.setPemesananId(csticket.getPemesananId() == null ? null : csticket.getPemesananId().getId());
        csticketDTO.setPelangganId(csticket.getPelangganId() == null ? null : csticket.getPelangganId().getId());
        return csticketDTO;
    }

    private Csticket mapToEntity(final CsticketDTO csticketDTO, final Csticket csticket) {
        csticket.setRating(csticketDTO.getRating());
        csticket.setContent(csticketDTO.getContent());
        csticket.setStatus(csticketDTO.getStatus());
        csticket.setTitle(csticketDTO.getTitle());
        csticket.setResponse(csticketDTO.getResponse());
        final Pemesanan pemesananId = csticketDTO.getPemesananId() == null ? null : pemesananRepository.findById(csticketDTO.getPemesananId())
                .orElseThrow(() -> new NotFoundException("pemesananId not found"));
        csticket.setPemesananId(pemesananId);
        final Pelanggan pelangganId = csticketDTO.getPelangganId() == null ? null : pelangganRepository.findById(csticketDTO.getPelangganId())
                .orElseThrow(() -> new NotFoundException("pelangganId not found"));
        csticket.setPelangganId(pelangganId);
        return csticket;
    }

}
