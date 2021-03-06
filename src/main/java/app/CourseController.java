package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faisal on 2018-03-20.
 */
@Controller
public class CourseController {
    @Autowired
    private CourseRepository courseRepo;
    @Autowired
    private LearningOutcomeRepository loRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private ProgramRepository programRepo;



    @RequestMapping("/pickCourse")
    public String pickCourse(Model model){
        Course course = new Course();
        model.addAttribute("course", course);
        model.addAttribute("courses", courseRepo.findAll());
        return "pickCourse";
    }

    @RequestMapping("/listCourses")
    public String listCourses(@SessionAttribute("user") User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("courses", courseRepo.findAll());
        return "listCourses";
    }

    @RequestMapping("/listCoursesByCategory")
    public String listCoursesByCategory(@SessionAttribute("user") User user, @ModelAttribute("category") Category category, Model model){
        List<LearningOutcome> los = loRepo.findByCategory(category);
        List<Course> courses = new ArrayList<>();
        for(LearningOutcome lo : los)
            courses.add(lo.getCourse());
        model.addAttribute("user", user);
        model.addAttribute("courses", courses);
        return "listCourses";
    }

    @GetMapping("/newCourse")
    public String newCourse(Model model){
        ArrayList years = new ArrayList();
        for(int i=0; i < AcademicYear.values().length; i++) {
            years.add(AcademicYear.values()[i].toString());
        }
        Course course = new Course();

        model.addAttribute("programs", programRepo.findAll());
        model.addAttribute("courseAndYear", new CourseAndYearForm());
        model.addAttribute("course", course);
        model.addAttribute("years", years);
        return "newCourseForm";
    }

    @PostMapping("/createCourse")
    public String createCourse(@SessionAttribute("user") User user, @ModelAttribute("courseAndYear") CourseAndYearForm course, Model model){
        AcademicYear year = null;
        for(int i=0; i < AcademicYear.values().length; i++) {
            if(AcademicYear.values()[i].toString().equals(course.getYear())){
                year = AcademicYear.values()[i];
            }
        }
        Course currentCourse = new Course(course.getCourse().getName(), course.getCourse().getDescription(), year);

        for(Program p : programRepo.findByName(course.getProgram().toString())){
            p.addCourse(currentCourse);
            currentCourse.addProgram(p);
        }

        Course c = courseRepo.save(currentCourse);
        model.addAttribute("user", user);
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("newCourse", c);
        return "listCourses";
    }

    @PostMapping("/displayLearningOutcomesForCourse")
    public String displayLearningOutcomesForCourse(@SessionAttribute("user") User user, @ModelAttribute("course")Course course, BindingResult p, Model m) {

        List<LearningOutcome> finalizedListoflearningOutcomes = loRepo.findByCourse(course);
        m.addAttribute("user", user);
        m.addAttribute("learningOutcomes", finalizedListoflearningOutcomes);
        m.addAttribute("learningOutcome", new LearningOutcome());
        return "displayLearningOutcomesForCourse";
    }

    @GetMapping("/editCourse/{courseId}")
    public String editCategory(@PathVariable Long courseId, Model model){
        ArrayList years = new ArrayList();
        for(int i=0; i < AcademicYear.values().length; i++) {
            years.add(AcademicYear.values()[i].toString());
        }

        Course course = courseRepo.findOne(courseId);
        CourseAndYearForm courseAndYear = new CourseAndYearForm();
        courseAndYear.setCourse(course);

        model.addAttribute("courseAndYear", courseAndYear);
        model.addAttribute("course", course);
        model.addAttribute("years", years);
        return "editCourseForm";
    }

    @PostMapping("/updateCourse/{courseId}")
    public String updateProgram(@SessionAttribute("user") User user, @PathVariable Long courseId, @ModelAttribute("courseAndYear") CourseAndYearForm course, Model model) {
        AcademicYear year = null;
        for(int i=0; i < AcademicYear.values().length; i++) {
            if(AcademicYear.values()[i].toString().equals(course.getYear())){
                year = AcademicYear.values()[i];
            }
        }
        Course currentCourse = course.getCourse();
        currentCourse.setYear(year);
        currentCourse.setId(courseId);

        Course updatedCourse = courseRepo.save(currentCourse);
        model.addAttribute("user", user);
        model.addAttribute("courses", courseRepo.findAll());
        model.addAttribute("updatedCourse", updatedCourse);
        return "listCourses";
    }

}
