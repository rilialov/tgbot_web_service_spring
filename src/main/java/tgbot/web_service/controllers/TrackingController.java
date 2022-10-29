package tgbot.web_service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tgbot.web_service.service.Response;
import tgbot.web_service.service.TrackingClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/tracking")
public class TrackingController {

    private final TrackingClient trackingClient;

    public TrackingController(TrackingClient trackingClient) {
        this.trackingClient = trackingClient;
    }

    @GetMapping
    public String trackingList(Optional<Integer> page, Optional<Integer> size, Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);

        Response response = trackingClient.getAllTracking(currentPage-1, pageSize);
        model.addAttribute("trackings", response.getContent());
        model.addAttribute("totalPages", response.getTotalPages());

        List<Integer> pageNumbersList = ControllersUtils.getPageNumbersList(currentPage, response.getTotalPages());
        model.addAttribute("pageNumbers", pageNumbersList);
        model.addAttribute("page", currentPage);

        List<Integer> sizes = Arrays.asList(10, 15, 20);
        model.addAttribute("sizes", sizes);
        model.addAttribute("pageSize", pageSize);
        return "tracking/trackingList";
    }
}
