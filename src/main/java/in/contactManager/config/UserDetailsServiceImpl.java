package in.contactManager.config;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import in.contactManager.model.User;
import in.contactManager.repository.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//fetching user From Database
		
		
		  User user= userRepository.getUserByUserName(username); if(user == null) {
		  throw new UsernameNotFoundException("Could Not Found UserName !!"); }
		  CustomeUserDetails customeUserDetails = new CustomeUserDetails(user);
		  
		  
		  
		  return customeUserDetails;
		 
		
		
	}

	

}
