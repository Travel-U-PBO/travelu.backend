package travelu.travelu_backend.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import travelu.travelu_backend.domain.Diskon;
import travelu.travelu_backend.domain.Jadwal;
import travelu.travelu_backend.domain.Pelanggan;
import travelu.travelu_backend.domain.Pembayaran;
import travelu.travelu_backend.model.PemesananDTO;
import travelu.travelu_backend.repos.DiskonRepository;
import travelu.travelu_backend.repos.JadwalRepository;
import travelu.travelu_backend.repos.PelangganRepository;
import travelu.travelu_backend.repos.PembayaranRepository;
import travelu.travelu_backend.service.PemesananService;
import travelu.travelu_backend.util.CustomCollectors;
import travelu.travelu_backend.util.ReferencedWarning;
import travelu.travelu_backend.util.WebUtils;


@Controller
@RequestMapping("/pemesanans")
public class PemesananController {

    private final PemesananService pemesananService;
    private final PelangganRepository pelangganRepository;
    private final PembayaranRepository pembayaranRepository;
    private final DiskonRepository diskonRepository;
    private final JadwalRepository jadwalRepository;

    public PemesananController(final PemesananService pemesananService,
            final PelangganRepository pelangganRepository,
            final PembayaranRepository pembayaranRepository,
            final DiskonRepository diskonRepository, final JadwalRepository jadwalRepository) {
        this.pemesananService = pemesananService;
        this.pelangganRepository = pelangganRepository;
        this.pembayaranRepository = pembayaranRepository;
        this.diskonRepository = diskonRepository;
        this.jadwalRepository = jadwalRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("pelangganIdValues", pelangganRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Pelanggan::getId, Pelanggan::getName)));
        model.addAttribute("pembayaranIdValues", pembayaranRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Pembayaran::getId, Pembayaran::getId)));
        model.addAttribute("listDiskonValues", diskonRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Diskon::getId, Diskon::getNama)));
        model.addAttribute("jadwalIdValues", jadwalRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Jadwal::getId, Jadwal::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("pemesanans", pemesananService.findAll());
        return "pemesanan/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("pemesanan") final PemesananDTO pemesananDTO) {
        return "pemesanan/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("pemesanan") @Valid final PemesananDTO pemesananDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pemesanan/add";
        }
        pemesananService.create(pemesananDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("pemesanan.create.success"));
        return "redirect:/pemesanans";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("pemesanan", pemesananService.get(id));
        return "pemesanan/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("pemesanan") @Valid final PemesananDTO pemesananDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pemesanan/edit";
        }
        pemesananService.update(id, pemesananDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("pemesanan.update.success"));
        return "redirect:/pemesanans";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = pemesananService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            pemesananService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("pemesanan.delete.success"));
        }
        return "redirect:/pemesanans";
    }

}
