package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private CategoryDao categoryDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");
        model.addAttribute(new Cheese());
        model.addAttribute("categories", categoryDao.findAll());
        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors, Model model, @RequestParam int categoryId) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "cheese/add";
        }


        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);
        cheeseDao.save(newCheese);

        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }

    @RequestMapping(value= "category/{id}", method = RequestMethod.GET)
    public String category(Model model, @PathVariable int id) {

        Category category = categoryDao.findOne(id);
        List<Cheese> cheeses = category.getCheese();
        model.addAttribute("cheeses", cheeses);

        model.addAttribute("title", category.getName() + " Cheeses");

        return"cheese/index";
    }

    @RequestMapping(value="edit/{id}", method = RequestMethod.GET)
    public String displayEditCheeseForm(Model model, @PathVariable int id) {
        Cheese cheese = cheeseDao.findOne(id);
        model.addAttribute("cheese", cheese);
        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute("title", "Edit " + cheese.getName());

        return "cheese/edit";
    }

    @RequestMapping(value="edit/{id}", method = RequestMethod.POST)
    public String processEditCheeseForm(Model model, @Valid @ModelAttribute Cheese cheese, Errors errors, @RequestParam int categoryId, @PathVariable int id) {
        Category cat = categoryDao.findOne(categoryId);
        cheese.setCategory(cat);

        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit " + cheese.getName());
            model.addAttribute("cheese", cheese);
            model.addAttribute("categories", categoryDao.findAll());
            return "cheese/edit";
        }

        Cheese ch = cheeseDao.findOne(id);
        String chName = cheese.getName();
        ch.setName(chName);
        ch.setDescription(cheese.getDescription());
        cheeseDao.save(ch);



        return "redirect:/cheese/";
    }
}
