package in.contactManager.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.contactManager.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	/* User getUserByUserName(String username); */
	
	@Query("select u from User u where u.email = :email ") 
	public User getUserByUserName(@Param("email") String email);

}
	