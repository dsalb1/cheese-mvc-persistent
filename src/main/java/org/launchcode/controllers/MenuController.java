package org.launchcode.controllers;



import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 6/21/2017.
 */

@Controller
@RequestMapping(value="menu")
public class MenuController {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value="")
    public String index(Model model) {
        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "All Menus");

        return "menu/index";
    }

    @RequestMapping(value="add", method= RequestMethod.GET)
    public String addMenuForm(Model model) {
        model.addAttribute("menu", new Menu());
        model.addAttribute("title", "Add Menu");

        return "menu/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String processMenuForm(Model model, @Valid @ModelAttribute Menu newMenu, Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");

            return "menu/add";
        }

        menuDao.save(newMenu);

        return "redirect:view/" + newMenu.getId();
    }

    @RequestMapping(value="view/{id}", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int id) {
        Menu menu = menuDao.findOne(id);
        model.addAttribute("menu", menu);
        model.addAttribute("title", menu.getName());

        return "menu/view";
    }

    @RequestMapping(value="add-item/{id}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int id) {
        Menu menu = menuDao.findOne(id);
        model.addAttribute("form", new AddMenuItemForm(menu, cheeseDao.findAll()));
        model.addAttribute("title", "Add item to Menu: " + menu.getName());

        return "menu/add-item";
    }

    @RequestMapping(value="add-item/{id}", method = RequestMethod.POST)
    public String addItem(Model model, @Valid @ModelAttribute AddMenuItemForm newForm, Errors errors) {
        Menu theMenu = menuDao.findOne(newForm.getMenuId());

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add item to menu" + theMenu.getName());

            return "menu/add-item";
        }

        Cheese newItem = cheeseDao.findOne(newForm.getCheeseId());
        theMenu.addItem(newItem);
        menuDao.save(theMenu);

        return "redirect:/menu/view/" + theMenu.getId();
    }
}
