package com.szbk.Controller;

import com.szbk.Model.Entity.LaborUser;
import com.szbk.Model.LaborUserRepository;
import com.szbk.Model.Entity.User;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@UIScope
@SpringComponent
public class LaborUserController {
    @Autowired
    LaborUserRepository repo;

    public LaborUser login(User u) {
        List<LaborUser> laborUsers = repo.findAll();
//        System.out.println("controller::login " + u);
//        System.out.println("laborUsers: " + laborUsers);
        for (LaborUser lb : laborUsers) {
//            System.out.println("lb.email: " + lb.getEmail());
//            System.out.println("lb.password: " + lb.getPassword());
            if (lb.getEmail().equals(u.getEmail())) {
                if (lb.getPassword().equals(u.getPassword())) {
//                    System.out.println("lb: " + lb);
                    return lb;
                }
            }
        }

        return null;
    }

    public boolean registration(LaborUser user) {
        repo.save(user);

        return true;
    }
}
