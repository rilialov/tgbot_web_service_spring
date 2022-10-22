package tgbot.web_service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tgbot.web_service.service.ReportClient;
import tgbot.web_service.service.Response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value = "/reports")
public class ReportsController {

    private final ReportClient reportClient;

    public ReportsController(ReportClient reportClient) {
        this.reportClient = reportClient;
    }

    @GetMapping
    public String reportsList(Integer page, Integer size, Model model) {
        if (page == null) {
            page = 1;
        }
        if (size == null) {
            size = 10;
        }
        Response response = reportClient.getAllReports(page - 1, size);
        model.addAttribute("reports", response.getContent());
        model.addAttribute("totalPages", response.getTotalPages());
        List<Integer> pageNumbers;
        if (page < response.getTotalPages()) {
            if (page > 1) {
                pageNumbers = IntStream.rangeClosed(page - 1, page + 1).boxed().collect(Collectors.toList());
            } else pageNumbers = IntStream.rangeClosed(page, page + 1).boxed().collect(Collectors.toList());
        } else {
            if (response.getTotalPages() == 1) {
                pageNumbers = IntStream.rangeClosed(page, page).boxed().collect(Collectors.toList());
            } else pageNumbers = IntStream.rangeClosed(page - 1, page).boxed().collect(Collectors.toList());
        }
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("page", page);
        List<Integer> sizes = Arrays.asList(10, 15, 20);
        model.addAttribute("sizes", sizes);
        model.addAttribute("pageSize", size);
        return "report/reportsList";
    }
}
