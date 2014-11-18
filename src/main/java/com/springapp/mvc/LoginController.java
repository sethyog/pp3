package com.springapp.mvc;

import com.springapp.PaypalException;
import com.springapp.biz.UserService;
import com.springapp.entity.TransferForm;
import com.springapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("user")
public class LoginController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String loginForm(Model model, HttpServletRequest request) {
		User user = null;
		if(null != request.getSession(true).getAttribute("user")) {
			user = (User) request.getSession(true).getAttribute("user");
			model.addAttribute("user", user);
			model.addAttribute("transferForm", new TransferForm());
			return "dashboard";
		}
		model.addAttribute("user", new User());
		return "login";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String doLogin(@ModelAttribute User user, Model model, HttpServletRequest request) {

		User thisUser = this.userService.getUser(user.getEmail(), user.getPwd());
		if(null == thisUser){
			model.addAttribute("user", user);
			model.addAttribute("error", "Invalid credentials");
			return "login";
		}
		model.addAttribute("user", thisUser);
		model.addAttribute("transferForm", new TransferForm());
		request.getSession(true).setAttribute("user", thisUser);
		return "dashboard";
	}




}