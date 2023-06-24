package in.contactManager.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.aspectj.apache.bcel.util.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.contactManager.helper.Message;
import in.contactManager.model.Contact;
import in.contactManager.model.User;
import in.contactManager.repository.ContactRepository;
import in.contactManager.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	
//	get the user using username
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {
		String username = principal.getName();
		System.out.println("username :"+username);
		
		User user = userRepository.getUserByUserName(username);
		System.out.println(user);
		model.addAttribute("user",user);
		
	}
	
	@GetMapping("/index")
	public String user(Model model, Principal principal) {
		return "normal/userDashboard";
	}
	
//	Open add form handler
	@GetMapping("add-contact")
	public String openAddContactForm(Model model) {
		model.addAttribute("title","Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
	
	/* processing Add Contact */
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, @RequestParam("prfImage") MultipartFile file, Principal principal, HttpSession session) {
		 try {
		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
//		pricessing and uploading img
		
		if(file.isEmpty()) {
//			if file is empty then Try Our msg
			System.out.println("file is Empty");
			contact.setImage("contact.png");
		}else {
//			file to folder and upadte the name to contact
			contact.setImage(file.getOriginalFilename());
			File savefile = new ClassPathResource("static/img").getFile();
			
			Path path = Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			Files.copy(file.getInputStream(),path ,StandardCopyOption.REPLACE_EXISTING);
			
			System.out.println("Image is Uploaded");
			
		}
		contact.setUser(user);
		user.getContacts().add(contact);
		System.out.println("Data :"+contact);
		
		this.userRepository.save(user);
		
		session.setAttribute("message", new Message("Your Data saveed SuccessFully!!", "success"));
		 }catch(Exception e) {
			 System.out.println("Error"+e.getMessage());
			 session.setAttribute("message", new Message("Something Went Wrong! try again!!", "danger"));
		 }
		return "normal/add_contact_form";
	}
	
	@GetMapping("/view-contact")
	public String showContacts(Model model, Principal principal) {
		model.addAttribute("title", "Show user Contact");
		String userName = principal.getName();
		
		User user = this.userRepository.getUserByUserName(userName);
		
		List<Contact> contacts = this.contactRepository.findContactByUser(user.getId());
		model.addAttribute("contacts", contacts);
		
		
		
		return "normal/view_Contacts";
	}
	
	//show Particular Contact Details
	
	@RequestMapping("/{cId}/contact")
	public String showContactDetails(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		
		model.addAttribute("title", "Contact-details");
	 Optional<Contact> contaOptional =  contactRepository.findById(cId);
	 
	 Contact contact =  contaOptional.get();
	 String username =  principal.getName();
	 
	 User user = this.userRepository.getUserByUserName(username);
	 
	 if(user.getId() == contact.getUser().getId()) {
		 model.addAttribute("contact",contact);
	 }
	 
	
		return "normal/contact_details";
	}
	
	@GetMapping("/delete/{cId}")
	public String delete(@PathVariable("cId") Integer cId, Model model, HttpSession session, Principal principal) {
		
//		 Optional<Contact> optional =  this.contactRepository.findById(cId);
		 
		 Contact contact =this.contactRepository.findById(cId).get();
//		 contact.setUser(null);
		 
		 String username = principal.getName();
		 
		 User user = this.userRepository.getUserByUserName(username);
		 
		 user.getContacts().remove(contact);
		 
		 
		 
		 this.contactRepository.save(contact);
		 System.out.println("Deleted");
		 
		 session.setAttribute("message", new Message("Contact Deleted SuccessFully", "success"));
		
		return "redirect:/user/view-contact";
	}
	
	@PostMapping("/update/{cId}")
	public String updateForm(@ModelAttribute("cId") Integer cId , Model model) {
		Contact contact = this.contactRepository.findById(cId).get();
		
		model.addAttribute("contact",contact);
		
		
		return "normal/update";
	}
	@PostMapping("/update-contact")
	public String updateprocess(@ModelAttribute Contact contact, @RequestParam("prfImage") MultipartFile file , 
			Model model ,HttpSession session , Principal principle) {
		try {
			
			
			  Contact oldContact = this.contactRepository.findById(contact.getcId()).get();
			  System.out.println("oldContact------------->"+oldContact);
			 
			
			if(!file.isEmpty()) {
				
//				delete old photo
				File deleteFile = new ClassPathResource("/static/img").getFile();
				File deleteprocess = new File(deleteFile, oldContact.getImage());
				deleteprocess.delete();
				
				
//				update new photo
				
				
				  File savefile = new ClassPathResource("static/img").getFile();
				  
				  Path path =
				  Paths.get(savefile.getAbsolutePath()+File.separator+file.getOriginalFilename(
				  ));
				  
				  Files.copy(file.getInputStream(),path ,StandardCopyOption.REPLACE_EXISTING);
				  contact.setImage(file.getOriginalFilename());
				 
				
			}else {
			contact.setImage(oldContact.getImage());
			}
			
			User user = this.userRepository.getUserByUserName(principle.getName());
			contact.setUser(user);
			this.contactRepository.save(contact);
			
			session.setAttribute("message", new Message("Your Contact is Upadated....", "success"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/user/"+contact.getcId()+"/contact";
	}
	
//	profile handler
	@GetMapping("/profile")
	public String profilePage(Model model) {
		model.addAttribute("title", "Your Profie");
		
		
		return "normal/profile";
	}
	

}
