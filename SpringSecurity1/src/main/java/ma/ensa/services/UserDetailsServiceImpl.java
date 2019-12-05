package ma.ensa.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ma.ensa.dao.EnsaRoleDao;
import ma.ensa.dao.EnsaUserDao;
import ma.ensa.entities.EnsaUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private EnsaUserDao EnsaUserDAO;
	@Autowired
	private EnsaRoleDao ensaRoleDAO;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		EnsaUser EnsaUser = this.EnsaUserDAO.findUserAccount(userName);
		if (EnsaUser == null) {
			System.out.println("User not found! " + userName);
			throw new UsernameNotFoundException("User " + userName + " was not found in the database");
		}
		System.out.println("Found User: " + EnsaUser);
		List<String> roleNames = this.ensaRoleDAO.getRoleNames(EnsaUser.getUserId());
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (roleNames != null) {
			for (String role : roleNames) {
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grantList.add(authority);
			}
		}
		UserDetails userDetails = (UserDetails) new User(EnsaUser.getUserName(), EnsaUser.getEncrytedPassword(),
				grantList);
		return userDetails;
	}
}
