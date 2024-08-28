package vn.dev.managementsystem.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.dev.managementsystem.Entity.User;
import vn.dev.managementsystem.Repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserByName(username);
		try {
			System.out.println("Load User By Name Successfully: " + user.getUsername());
			return user;
		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
		}
	
		return new User();
	}

}
