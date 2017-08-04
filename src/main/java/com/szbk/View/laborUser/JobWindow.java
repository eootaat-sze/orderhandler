package com.szbk.View.laborUser;

import com.szbk.Model.Entity.CustomerOrder;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Set;

public class JobWindow extends Window {
    private VerticalLayout layout;

    private Label siteTitle;
    private Grid jobGrid;
    private Button synFileGenerationButton;

    private Set ordersForJob;

    public JobWindow(Set<CustomerOrder> orders) {
        this.ordersForJob = orders;
        this.layout = new VerticalLayout();
        this.siteTitle = new Label();
        this.jobGrid = new Grid<>(CustomerOrder.class);
        this.synFileGenerationButton = new Button();

        setupContent();
    }

    private void setupContent() {
        //setup the window
        setModal(true);
        setClosable(true);
        setResizable(false);
        setDraggable(false);
        setCaption("A kiválasztott elemek");
        setWidth(80, Unit.PERCENTAGE);

        siteTitle.setCaption("A job-ba kiválasztott elemek az alábbiak");
        siteTitle.setStyleName(ValoTheme.LABEL_H2);

        synFileGenerationButton.setCaption("Syn fájl generálás");
        synFileGenerationButton.addClickListener(e -> {
            //TODO generate syn file
            Notification.show("Generating syn file...");
        });

        //setup the grid
        jobGrid.setItems(ordersForJob);
        jobGrid.setSizeFull();
        jobGrid.setColumns("sequence", "scale", "purification", "type");

        layout.addComponents(siteTitle, jobGrid, synFileGenerationButton);
        layout.setComponentAlignment(siteTitle, Alignment.TOP_CENTER);
        layout.setComponentAlignment(synFileGenerationButton, Alignment.BOTTOM_CENTER);

        setContent(layout);
    }
}
