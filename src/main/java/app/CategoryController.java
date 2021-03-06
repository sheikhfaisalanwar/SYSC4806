package app;

import app.Program;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import app.ProgramRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.ArrayList;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepo;

    @RequestMapping("/pickCategory")
    public String pickCategory(Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        model.addAttribute("categories", categoryRepo.findAll());
        return "pickCategory";
    }

    @RequestMapping("/listCategories")
    public String listCategories(@SessionAttribute("user") User user, @RequestParam(value = "newCategory", required = false) Category newCategory,  Model model){
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("user", user);
        if (newCategory != null)
            model.addAttribute("newCategory", newCategory);
        return "listCategories";
    }

    @GetMapping("/addCategory")
    public String categoryForm(Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        return "categoryForm";
    }

    @PostMapping("/addCategory")
    public String categorySubmit(@ModelAttribute("category") Category category, Model model) {
        Category c = categoryRepo.save(category);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("newCategory", c);
        return "listCategories";
    }

    @GetMapping("/editCategory/{categoryId}")
    public String editCategory(@PathVariable Long categoryId, Model model){
        Category category = categoryRepo.findOne(categoryId);
        model.addAttribute("category", category);
        return "editCategoryForm";
    }

    @PostMapping("/updateCategory/{categoryId}")
    public String updateProgram(@PathVariable Long categoryId, @ModelAttribute("category") Category category, Model model) {
        category.setId(categoryId);
        Category updatedCategory = categoryRepo.save(category);
        model.addAttribute("categories", categoryRepo.findAll());
        model.addAttribute("updatedCategory", updatedCategory);
        return "listCategories";
    }
}
