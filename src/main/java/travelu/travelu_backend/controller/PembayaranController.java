package travelu.travelu_backend.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import travelu.travelu_backend.model.PaymentMethod;
import travelu.travelu_backend.model.PembayaranDTO;
import travelu.travelu_backend.service.PembayaranService;
import travelu.travelu_backend.util.ReferencedWarning;
import travelu.travelu_backend.util.WebUtils;


@Controller
@RequestMapping("/pembayarans")
public class PembayaranController {

    private final PembayaranService pembayaranService;

    public PembayaranController(final PembayaranService pembayaranService) {
        this.pembayaranService = pembayaranService;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("metodeValues", PaymentMethod.values());
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("pembayarans", pembayaranService.findAll());
        return "pembayaran/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("pembayaran") final PembayaranDTO pembayaranDTO) {
        return "pembayaran/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("pembayaran") @Valid final PembayaranDTO pembayaranDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pembayaran/add";
        }
        pembayaranService.create(pembayaranDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("pembayaran.create.success"));
        return "redirect:/pembayarans";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("pembayaran", pembayaranService.get(id));
        return "pembayaran/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("pembayaran") @Valid final PembayaranDTO pembayaranDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "pembayaran/edit";
        }
        pembayaranService.update(id, pembayaranDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("pembayaran.update.success"));
        return "redirect:/pembayarans";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = pembayaranService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            pembayaranService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("pembayaran.delete.success"));
        }
        return "redirect:/pembayarans";
    }

}
