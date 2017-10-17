package com.szbk.Controller;

import com.szbk.Model.Entity.Purification;
import com.szbk.Model.PurificationRepository;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dante on 2017.10.10..
 */
@UIScope
@SpringComponent
public class PurificationController {
    @Autowired
    PurificationRepository repo;

    private List<Purification> purificationList;
    private long count;

    public List<Purification> getAllPurifications() {
        return repo.findAll();
    }

    public List<String> getPurificationNamesAndPricesAsStrings() {
        purificationList = repo.findAll();
        List<String> stringValues = new ArrayList<>();

        String value;

        for (Purification item : purificationList) {
            value = item.getName() + " - " + item.getPrice() + " Ft";
            stringValues.add(value);
        }

        return stringValues;
    }

    public boolean savePurification(Purification p) {
        count = repo.count();
        repo.save(p);

        return count < repo.count();
    }
}
