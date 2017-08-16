package com.szbk.View.laborUser;

import com.szbk.Model.Entity.Customer;
import com.szbk.Model.Entity.CustomerOrder;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import java.io.ByteArrayInputStream;
import java.util.Set;

public class JobWindow extends Window {
    private VerticalLayout layout;

    private Label siteTitle;
    private Grid jobGrid;
    private Button synFileGenerationButton;

    private Set ordersForJob;

    private SimpleFileDownloader fileDownloader;

    public JobWindow(Set<CustomerOrder> orders) {
        this.ordersForJob = orders;
        this.layout = new VerticalLayout();
        this.siteTitle = new Label();
        this.jobGrid = new Grid<>(CustomerOrder.class);
        this.synFileGenerationButton = new Button();
        this.fileDownloader = new SimpleFileDownloader();

        setupContent();
    }

    private void setupContent() {
        //setup the window
        setModal(true);
        setClosable(true);
//        setResizable(false);
        setDraggable(false);
        setCaption("A job-ba kiválasztott elemek");
        setWidth(80, Unit.PERCENTAGE);

        //Add the file downloader extension to the UI.
        addExtension(fileDownloader);

        synFileGenerationButton.setCaption("Syn fájl generálás");
        synFileGenerationButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        synFileGenerationButton.addClickListener(e -> {
            synFileBuildAndDownload();
        });

        //setup the grid
        jobGrid.setItems(ordersForJob);
        jobGrid.setSizeFull();
        jobGrid.setColumns("sequence", "scale", "purification", "type");

        //TODO It is possible to add editor component to an existing column. An example is in the Sampler.

        layout.addComponents(siteTitle, jobGrid, synFileGenerationButton);
        layout.setComponentAlignment(siteTitle, Alignment.TOP_CENTER);
        layout.setComponentAlignment(synFileGenerationButton, Alignment.BOTTOM_CENTER);

        setContent(layout);
    }

    private void synFileBuildAndDownload() {
        StringBuilder builder = new StringBuilder();
        builder.append("\"Applied Biosystems Oligonucleotide Synthesizer Software Synthesis File\"\n");

        for (Object order : ordersForJob) {
            builder.append("\"Oligo ID: " + ((CustomerOrder) order).getId() + "\"\n");
            builder.append("\"Number of Bases: " + ((CustomerOrder) order).getSequence().length() + "\"\n");
            builder.append("\"Sequence: " + ((CustomerOrder) order).getSequence() + "\"\n");
            builder.append("\"Vial ID: " + ((CustomerOrder) order).getCustomerName() + "!#!belnév#!#"
                    + ((CustomerOrder) order).getPurification() + "\"\n");
            builder.append("\n");
        }

        System.out.println(builder.toString());

        final StreamResource resource = new StreamResource(() -> {
            return new ByteArrayInputStream(builder.toString().getBytes());
        }, "testFile.syn");

        fileDownloader.setFileDownloadResource(resource);
        fileDownloader.download();
    }
}
