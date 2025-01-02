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
import travelu.travelu_backend.domain.Cabang;
import travelu.travelu_backend.model.DestinasiItinerariDTO;
import travelu.travelu_backend.repos.CabangRepository;
import travelu.travelu_backend.service.DestinasiItinerariService;
import travelu.travelu_backend.util.CustomCollectors;
import travelu.travelu_backend.util.WebUtils;


@Controller
@RequestMapping("/destinasiItineraris")
public class DestinasiItinerariController {

    private final DestinasiItinerariService destinasiItinerariService;
    private final CabangRepository cabangRepository;

    public DestinasiItinerariController(final DestinasiItinerariService destinasiItinerariService,
            final CabangRepository cabangRepository) {
        this.destinasiItinerariService = destinasiItinerariService;
        this.cabangRepository = cabangRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("cabangIdValues", cabangRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Cabang::getId, Cabang::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("destinasiItineraris", destinasiItinerariService.findAll());
        return "destinasiItinerari/list";
    }

    @GetMapping("/add")
    public String add(
            @ModelAttribute("destinasiItinerari") final DestinasiItinerariDTO destinasiItinerariDTO) {
        return "destinasiItinerari/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("destinasiItinerari") @Valid final DestinasiItinerariDTO destinasiItinerariDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "destinasiItinerari/add";
        }
        destinasiItinerariService.create(destinasiItinerariDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("destinasiItinerari.create.success"));
        return "redirect:/destinasiItineraris";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("destinasiItinerari", destinasiItinerariService.get(id));
        return "destinasiItinerari/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("destinasiItinerari") @Valid final DestinasiItinerariDTO destinasiItinerariDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "destinasiItinerari/edit";
        }
        destinasiItinerariService.update(id, destinasiItinerariDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("destinasiItinerari.update.success"));
        return "redirect:/destinasiItineraris";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        destinasiItinerariService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("destinasiItinerari.delete.success"));
        return "redirect:/destinasiItineraris";
    }

}
