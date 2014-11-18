package com.springapp.mvc;

import com.springapp.biz.UserService;
import com.springapp.entity.Transaction;
import com.springapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class LedgerController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/ledger", method = RequestMethod.GET)
    public String ledgerView(Model model, HttpServletRequest request) {
        User user = (User)request.getSession().getAttribute("user");

        List<Transaction> transactions = userService.getLedgerForUser(user.getId());

        model.addAttribute("ledger", transactions);

        return "ledger";


    }
}
