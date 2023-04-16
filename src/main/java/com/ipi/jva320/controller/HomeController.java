package com.ipi.jva320.controller;

import java.io.Console;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.ipi.jva320.exception.NoSalarieFoundException;
import com.ipi.jva320.exception.SalarieException;
import com.ipi.jva320.model.SalarieAideADomicile;
import com.ipi.jva320.service.SalarieAideADomicileService;

// HomeController.java :
@Controller
public class HomeController {

    @Autowired
    SalarieAideADomicileService salarieAideADomicileService;

    @GetMapping(value = "/")
    public String home(
            final ModelMap model) throws EntityExistsException, SalarieException {
        model.put(
                "nombre",
                salarieAideADomicileService.countSalaries());

        if (salarieAideADomicileService.countSalaries() == 0) {
            SalarieAideADomicile aide = new SalarieAideADomicile("Jean", LocalDate.now(), LocalDate.now(),
                    10, 0,
                    80, 5, 1);
            salarieAideADomicileService.creerSalarieAideADomicile(aide);
        }

        return "home";
    }

    @GetMapping(value = "/salaries/{id}")
    public String salarieDesc(
            final ModelMap model, @PathVariable Long id) {
        SalarieAideADomicile salarieAideADomicile = salarieAideADomicileService.getSalarie(id);
        model.put(
                "modif", false);
        model.put(
                "salarie", salarieAideADomicile);
        return "detail_Salarie";
    }

    @GetMapping(value = "/salarie/{id}/delete")
    public String salarieDelete(
            final ModelMap model, @PathVariable Long id) throws EntityExistsException, SalarieException {

        salarieAideADomicileService.deleteSalarieAideADomicile(id);

        return "redirect:/salaries";
    }

    @GetMapping(value = "/salaries")
    public String salarieList(
            final ModelMap model,
            @RequestParam(value = "nom", defaultValue = "") String nom) throws NoSalarieFoundException {
        List<SalarieAideADomicile> salariesAideADomicile;
        if (nom.isEmpty()) {
            salariesAideADomicile = salarieAideADomicileService.getSalaries();
        } else {
            salariesAideADomicile = salarieAideADomicileService.getSalaries(nom);
            if (salariesAideADomicile.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, nom + " : not found");
            }
        }
        model.put("salaries", salariesAideADomicile);
        return "list";
    }

    @GetMapping(value = "/salaries/aide/new")
    public String salarieNew(
            final ModelMap model) {

        SalarieAideADomicile salarieAideADomicile = new SalarieAideADomicile();

        model.put(
                "modif", true);
        model.put(
                "salarie",
                salarieAideADomicile);
        return "detail_Salarie";
    }

    @PostMapping(value = "/salarie/save")
    public String salarieSave(SalarieAideADomicile salarieAideADomicile)
            throws EntityExistsException, SalarieException {
        salarieAideADomicileService.creerSalarieAideADomicile(salarieAideADomicile);

        return "redirect:/salaries/" + salarieAideADomicile.getId();

    }

    @PostMapping(value = "/salarie/update/{id}")
    public String salarieSave(SalarieAideADomicile salarieAideADomicile, @PathVariable Long id)
            throws EntityExistsException, SalarieException {

        salarieAideADomicileService.updateSalarieAideADomicile(salarieAideADomicile);

        return "redirect:/salaries/" + id;

    }
}