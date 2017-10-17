package com.szbk.Controller;

import com.szbk.Model.Entity.Type;
import com.szbk.Model.TypeRepository;
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
public class TypeController {
    @Autowired
    TypeRepository repo;

    private List<Type> typesList;
    private long count;

    public List<Type> getAllTypes() {
        return repo.findAll();
    }

    public List<String> getTypeNamesAndPricesAsStrings() {
        typesList = repo.findAll();
        List<String> values = new ArrayList<>();
        String value;

        for (Type item : typesList) {
            value = item.getName() + " - " + item.getPrice() + " Ft";
            values.add(value);
        }

        return values;
    }

    public boolean saveType(Type t) {
        count = repo.count();
        repo.save(t);

        return count < repo.count();
    }
}
