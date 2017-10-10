package com.szbk.View.customer;

import com.szbk.Controller.OrderController;
import com.szbk.Model.Entity.CustomerOrder;
import com.vaadin.data.*;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.OutputStream;
import java.time.LocalDate;

public class OrderWindow extends VerticalLayout {
    public static final String REQUIRED = "A mező kitöltése kötelező";
    Button simpleOrder;
    Button multiplyOrder;
    Label simpleOrderDescription;
    Label multiplyOrderDescription;
    VerticalLayout descriptions;
    HorizontalLayout buttons;

    Binder<CustomerOrder> dataBinder;
    CustomerOrder order;
    OrderController controller;

    public OrderWindow(OrderController controller) {
        dataBinder = new Binder<>(CustomerOrder.class);
        this.controller = controller;

        simpleOrderDescription = new Label("<b>Egy rendelés</b> esetén minden információt kézzel adhatsz meg," +
                "mert (feltételezem) egyetlen rendelést szeretnél leadni.</br> Ezen opció kiválasztásakor nincs lehetőség" +
                "több adatsor felvitelére!");
        simpleOrderDescription.setContentMode(ContentMode.HTML);

        multiplyOrderDescription = new Label("<b>Több rendelés</b> esetén olyan űrlapot tölthetsz ki, amelyen nem kell megadni semmit a személyes adatokon kívül<br/>" +
                "helyette egy fájlt tudsz feltölteni, amely minden információt tartalmaz a megfelelő sorrendben, amelyre szükség van.<br/>" +
                "<b>A szükséges információk</b>: sequence, scale, purification, típus.");
        multiplyOrderDescription.setContentMode(ContentMode.HTML);

        simpleOrder = new Button("Egy rendelés", e -> {
            getUI().addWindow(buildSimpleOrderForm());
        });

        multiplyOrder = new Button("Több rendelés", e -> {
            getUI().addWindow(buildMultiplyOrderForm());
        });

        descriptions = new VerticalLayout();
        descriptions.addComponents(simpleOrderDescription, multiplyOrderDescription);
        descriptions.setComponentAlignment(simpleOrderDescription, Alignment.TOP_CENTER);
        descriptions.setComponentAlignment(multiplyOrderDescription, Alignment.TOP_CENTER);
        descriptions.setSizeUndefined();

        buttons = new HorizontalLayout();
        buttons.addComponents(simpleOrder, multiplyOrder);
        buttons.setSizeUndefined();

        addComponents(descriptions, buttons);
        setComponentAlignment(descriptions, Alignment.TOP_CENTER);
        setComponentAlignment(buttons, Alignment.TOP_CENTER);
        setSizeFull();
    }

    private VerticalLayout addControlls() {
        order = new CustomerOrder();

        VerticalLayout formLayout = new VerticalLayout();
        TextField customerName = new TextField();
        customerName.setPlaceholder("Megrendelő neve");
        customerName.setWidth(100, Unit.PERCENTAGE);
        customerName.setEnabled(false);
//        customerName.setValue(String.valueOf(getSession().getAttribute("customerName")));
        customerName.setValue(String.valueOf(VaadinSession.getCurrent().getAttribute("customerName")));
        dataBinder.bind(customerName, CustomerOrder::getCustomerName, CustomerOrder::setCustomerName);

        TextField groupName = new TextField();
        groupName.setPlaceholder("Csoport neve");
        groupName.setWidth(100, Unit.PERCENTAGE);
        groupName.setEnabled(false);
//        groupName.setValue(String.valueOf(getSession().getAttribute("groupName")));
        groupName.setValue(String.valueOf(VaadinSession.getCurrent().getAttribute("groupName")));
        dataBinder.bind(groupName, CustomerOrder::getGroupName, CustomerOrder::setGroupName);

        TextField companyName = new TextField();
        companyName.setPlaceholder("Cégnév");
        companyName.setWidth(100, Unit.PERCENTAGE);
        companyName.setEnabled(false);
//        companyName.setValue(String.valueOf(getSession().getAttribute("companyName")));
        companyName.setValue(String.valueOf(VaadinSession.getCurrent().getAttribute("companyName")));
        dataBinder.bind(companyName, CustomerOrder::getCompanyName, CustomerOrder::setCompanyName);

        formLayout.setMargin(true);
        formLayout.setSpacing(true);
        formLayout.setWidth(300, Unit.PIXELS);

        formLayout.addComponents(customerName, groupName, companyName);

        return formLayout;
    }

    //The form, which appears when the user clicks the "One order" button.
    private Window buildSimpleOrderForm() {
        Window orderWindow = new Window();
        orderWindow.setSizeUndefined();
        orderWindow.setModal(true);
        orderWindow.setResizable(false);
        orderWindow.setCaption("Rendelés leadása");

        VerticalLayout formLayout = addControlls();

        TextField sequence = new TextField();
        sequence.setPlaceholder("Sequence");
        sequence.setDescription("Sequence megadása");
        sequence.setWidth(100, Unit.PERCENTAGE);
        sequence.focus();
//        dataBinder.bind(sequence, CustomerOrder::getSequence, CustomerOrder::setSequence);
        dataBinder.forField(sequence).asRequired(REQUIRED)
                .withValidator((Validator<String>) (value, context) -> {
                    if (sequence.getValue().contains(" ")) {
                        return ValidationResult.error("A szekvencia nem tartalmazhat szóközt!");
                    } else {
                        return ValidationResult.ok();
                    }
                }).bind(CustomerOrder::getSequence, CustomerOrder::setSequence);

        ComboBox<String> purification = new ComboBox<>();
        purification.setItems("Egyszerű tisztítás - 1000 Ft", "Alapos tisztítás - 5000 Ft");
        purification.setEmptySelectionAllowed(false);
        purification.setPlaceholder("Tisztítás típusa");
        purification.setDescription("Purification megadása");
        purification.setWidth(100, Unit.PERCENTAGE);
//        dataBinder.bind(purification, CustomerOrder::getPurification, CustomerOrder::setPurification);
        dataBinder.forField(purification).asRequired(REQUIRED)
                .bind(CustomerOrder::getPurification, CustomerOrder::setPurification);

        TextField scale = new TextField();
        scale.setPlaceholder("Scale");
        scale.setDescription("Scale megadása");
        scale.setWidth(100, Unit.PERCENTAGE);
        dataBinder.forField(scale).asRequired(REQUIRED)
                .withConverter(new StringToDoubleConverter("Egy tizedestörtet kell megadnia!"))
                .bind(CustomerOrder::getScale, CustomerOrder::setScale);

        ComboBox<String> type = new ComboBox<>();
        type.setItems("DNS", "RNS", "módosított");
        type.setEmptySelectionAllowed(false);
        type.setPlaceholder("Típus");
        type.setDescription("Típus megadása");
        type.setWidth(100, Unit.PERCENTAGE);
//        dataBinder.bind(type, CustomerOrder::getType, CustomerOrder::setType);
        dataBinder.forField(type).asRequired(REQUIRED).bind(CustomerOrder::getType, CustomerOrder::setType);

        //The date of the order won't be binded, because it is not possible (binder works only with strings), so I add
        //it to the order object manually, later.
        DateField orderDate = new DateField();
        orderDate.setWidth(100, Unit.PERCENTAGE);
        orderDate.setValue(LocalDate.now());
        orderDate.setEnabled(false);

        Button sendOrderBtn = new Button("Rendelés leadása", e -> {
            order.setOrderDate(orderDate.getValue());
            order.setCustomerInnerName(String.valueOf(VaadinSession.getCurrent().getAttribute("innerName")));
            order.setStatus("Elkészült");

            Notification notification = new Notification("Sikeres rendelés!");
            notification.setStyleName(ValoTheme.NOTIFICATION_SUCCESS);
            notification.setPosition(Position.TOP_CENTER);


            if (validateOrder()) {
                if (controller.saveOrder(order)) {
                    notification.show(getUI().getPage());
                    getUI().removeWindow(orderWindow);
                    System.out.println("order: " + order);
                } else {
                    notification.setStyleName(ValoTheme.NOTIFICATION_WARNING);
                    notification.setCaption("Sikertelen rendelés!");
                    notification.show(getUI().getPage());
                }
            }
        });

        sendOrderBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
        sendOrderBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        formLayout.addComponents(sequence, purification, scale, type, orderDate, sendOrderBtn);
        formLayout.setComponentAlignment(sendOrderBtn, Alignment.BOTTOM_CENTER);

        orderWindow.setContent(formLayout);


        //The Vaadin binder only supports String as the type of the binded data, so double and date cannot be binded,
        //but I have a double and a date type field, so somehow I have to pass these parameters as well.
        //order.setOrderDate(Date.from(orderDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        return orderWindow;
    }

    //The form, which appears when the user clicks the "Multiply order" button.
    private Window buildMultiplyOrderForm() {
        Window orderWindow = new Window();
        orderWindow.setSizeUndefined();
        orderWindow.setModal(true);
        orderWindow.setResizable(false);
        orderWindow.setCaption("Rendelés leadása");

        VerticalLayout formLayout = addControlls();

        Button fileUploadBtn = new Button("Fájl feltöltés", e -> {
            getUI().addWindow(addFileUploadWindow());
        });

        fileUploadBtn.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);

        Button sendOrderBtn = new Button("Rendelés leadása", e -> {

        });

        sendOrderBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
        sendOrderBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        formLayout.addComponents(fileUploadBtn, sendOrderBtn);
        formLayout.setComponentAlignment(sendOrderBtn, Alignment.BOTTOM_CENTER);
        formLayout.setComponentAlignment(fileUploadBtn, Alignment.BOTTOM_CENTER);

        orderWindow.setContent(formLayout);

        return orderWindow;
    }
    
    private Window addFileUploadWindow() {
        Window fileUploadWindow = new Window();
        fileUploadWindow.setCaption("Fájl feltöltése");
        fileUploadWindow.setModal(true);
        fileUploadWindow.setResizable(false);

        VerticalLayout layout = new VerticalLayout();

        Upload fileUpload = new Upload(null, new FileReceiver());
        fileUpload.setImmediateMode(false);
        fileUpload.setButtonCaption("Feltöltés");
        fileUpload.setCaption("Fájl feltöltése");

        fileUpload.addStartedListener(e -> {
            Notification.show("Fájl feltöltése!");
        });

        fileUpload.addFinishedListener(e -> {
            Notification.show("Fájl feltöltése befejezve!");
        });

        layout.addComponent(fileUpload);
        fileUploadWindow.setContent(layout);

        return fileUploadWindow;
    }

    private boolean validateOrder() {
        try {
            dataBinder.writeBean(order);
            return true;
        } catch(ValidationException e) {
            Notification failOrderNotification = new Notification("A megadott adatok nem megfelelőek!");
            failOrderNotification.setStyleName(ValoTheme.NOTIFICATION_ERROR);
            failOrderNotification.setPosition(Position.TOP_CENTER);
            failOrderNotification.show(getUI().getPage());
            return false;
        }
    }

    private class FileReceiver implements Upload.Receiver {

        @Override
        public OutputStream receiveUpload(String s, String s1) {
            return null;
        }
    }
}
