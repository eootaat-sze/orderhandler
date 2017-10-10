package com.szbk.View.laborUser;

import com.szbk.Model.Entity.CustomerOrder;
import com.szbk.Model.Entity.LaborUser;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.simplefiledownloader.SimpleFileDownloader;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Set;

public class JobWindow extends Window {
    private VerticalLayout layout;

    private Label siteTitle;
    private Grid jobGrid;
    private Button synFileGenerationButton;

    private Set<CustomerOrder> ordersForJob;

    private SimpleFileDownloader fileDownloader;

    private LaborUserUI ui;

    public JobWindow(LaborUserUI ui, Set<CustomerOrder> orders) {
        this.ui = ui;
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
            ui.getOrderController().saveManyOrder(changeOrderStatus());
            getUI().removeWindow(this);
        });

        //setup the grid
        jobGrid.setItems(ordersForJob);
        jobGrid.setSizeFull();
        jobGrid.setColumns("customerInnerName", "sequence", "scale", "purification", "type");

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

        final StreamResource resource = new StreamResource(
            () -> new ByteArrayInputStream(builder.toString().getBytes()),
            createFileName()
        );

        fileDownloader.setFileDownloadResource(resource);
        fileDownloader.download();
    }

    private String createFileName() {
        LocalDateTime dateTime = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(dateTime.getYear()));

        if (dateTime.getMonthValue() < 10) {
            sb.append("0");
        }

        sb.append(String.valueOf(dateTime.getMonthValue()));
        sb.append(String.valueOf(dateTime.getDayOfMonth()));
        sb.append("-");
        sb.append(String.valueOf(dateTime.getHour()));

        //For whatever reason it is a dash (-) in the filename.
        sb.append(":");

        if (dateTime.getMinute() < 10) {
            sb.append("0");
        }

        sb.append(String.valueOf(dateTime.getMinute()));
        sb.append(".syn");

        return sb.toString();
    }

    private Set<CustomerOrder> changeOrderStatus() {
        for (CustomerOrder order : this.ordersForJob) {
            order.setStatus("Folyamatban");
        }

        return ordersForJob;
    }

    public void setOrdersForJob(Set<CustomerOrder> orders) {
        this.ordersForJob = orders;
    }
}
