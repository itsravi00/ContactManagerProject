package in.contactManager.Controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.contactManager.helper.Message;
import in.contactManager.model.User;
import in.contactManager.repository.UserRepository;

@Controller
public class HomeController {
	 
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("title","Home - Contact Manager");
		return "home";
	}
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About - Contact Manager");
		return "about";
	}
	@GetMapping("/signUp")
	public String signUp(Model model) {
		model.addAttribute("title","Register - Contact Manager");
		model.addAttribute("users", new User());
		return "register";
	}
	
//	Handling Signup details
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("users") User user ,@RequestParam(value ="agreement" ,defaultValue = "false") boolean agreement ,
			Model model, BindingResult result, HttpSession session) {
		try {
			if(!agreement) {
				System.out.println("you have not agreed Terms and Condition");
				throw new Exception("you have not agreed Terms and Condition");
			}
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			System.out.println("Agreement :"+agreement);
			System.out.println("User :"+user);
			this.userRepository.save(user);
			 model.addAttribute("users", new User());	
			 session.setAttribute("message", new Message("SuccessFully Registered!", "alert-success"));
			 return "register";	
			
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("users", user);
			session.setAttribute("message", new Message("Something went Wrong !!"+ e.getMessage(), "alert-danger"));
			return "register";
		}
		
	}
	
	
	// handling for custome login
	@RequestMapping("/signin")
	public String customeLogin(Model model) {
		
		model.addAttribute("title","Sign in");
		return "login";
	}
	
	
	
	
	
	
	
}
