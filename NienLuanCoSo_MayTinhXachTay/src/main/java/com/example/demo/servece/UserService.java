package com.example.demo.servece;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public boolean login(String username, String password) {
	    User user = userRepository.findByUsername(username);

	    if (user == null) {
	        return false;
	    }

	    return passwordEncoder.matches(password, user.getPassword());
	}
}
