package com.springapp.mvc;

import com.springapp.biz.TxService;
import com.springapp.biz.UserService;
import com.springapp.entity.TransferForm;
import com.springapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("user")
public class TxController {

    @Autowired
    private TxService txService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/dotx", method = RequestMethod.POST)
    public String transferAmt(@ModelAttribute TransferForm transferForm, Model model, HttpServletRequest request) {
        String receiverEmail = transferForm.getReceiver();
        User receiver = userService.getUserByEmail(receiverEmail);
        if(receiver != null) {
            int txId = 0;
            User currentUser = (User)request.getSession().getAttribute("user");
            if(userService.hasSufficientBalance(currentUser.getId(), transferForm.getAmount())) {
                txId = txService.initTransfer(currentUser.getId(), receiver.getId(), transferForm.getAmount());
                model.addAttribute("txId", txId);
            }
            else {
                model.addAttribute("error", "Insufficient balance");
            }

            model.addAttribute("success", "Transfer successful with id:" +txId);
            currentUser.setBalance(currentUser.getBalance()-transferForm.getAmount());
        }
        else {
            model.addAttribute("error", "Invalid user");
        }
        return "dashboard";
    }

}
