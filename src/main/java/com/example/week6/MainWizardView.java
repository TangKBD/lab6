package com.example.week6;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Route(value = "mainPage.it")
public class MainWizardView extends VerticalLayout {
    private TextField fullname, dollars;
    private RadioButtonGroup<String> gender;
    private ComboBox<String> position, school, house;
    private Button prev, create, update, delete, next;
    private Wizards wizards;
    private int index;

    public MainWizardView() {
        fullname = new TextField();
        dollars = new TextField("Dollars");
        gender = new RadioButtonGroup<>();
        position = new ComboBox<>();
        school = new ComboBox<>();
        house = new ComboBox<>();
        prev = new Button("<<");
        create = new Button("Create");
        update = new Button("Update");
        delete = new Button("Delete");
        next = new Button(">>");
        wizards = new Wizards();

        fullname.setPlaceholder("Fullname");
        dollars.setPrefixComponent(new Span("$"));
        gender.setLabel("Gender :");
        gender.setItems("m", "f");

        position.setPlaceholder("Position");
        position.setItems("student", "teacher");

        school.setPlaceholder("School");
        school.setItems("Hogwarts", "Beauxbatons", "Durmstrang");

        house.setPlaceholder("House");
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slyther");

        HorizontalLayout button = new HorizontalLayout();
        button.add(prev, create, update, delete, next);
        this.add(fullname, gender, position, dollars, school, house, button);
        load();

        create.addClickListener(event -> {
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard?sex="+this.gender.getValue()+
                            "&name="+this.fullname.getValue()+
                            "&school="+this.school.getValue()+
                            "&house="+this.house.getValue()+
                            "&money="+this.dollars.getValue()+
                            "&position="+this.position.getValue())
                    .retrieve()
                    .bodyToMono(Wizard.class)
                    .block();
            Notification notification = Notification.show("Wizard has been created");
            notification.setPosition(Notification.Position.BOTTOM_START);
            load();
        });

        update.addClickListener(event -> {
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateWizard?sex=" + this.gender.getValue() +
                            "&newName=" + this.fullname.getValue() +
                            "&school=" + this.school.getValue() +
                            "&house=" + this.house.getValue() +
                            "&money=" + this.dollars.getValue() +
                            "&position=" + this.position.getValue() +
                            "&oldName=" + this.wizards.model.get(index).getName())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            Notification notification = Notification.show("Wizard has been updated");
            notification.setPosition(Notification.Position.BOTTOM_START);
            load();
        });

        delete.addClickListener(event -> {
            WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteWizard?name=" + this.wizards.model.get(index).getName())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            Notification notification = Notification.show("Wizard has been deleted");
            notification.setPosition(Notification.Position.BOTTOM_START);
            load();
            show();
        });

        prev.addClickListener(event -> {
            if(index - 1 < 0){
                index = 0;
                show();
            } else {
                index -= 1;
                show();
            }
        });

        next.addClickListener(event -> {
            if(index + 1 >= this.wizards.model.size()){
                index = this.wizards.model.size() - 1;
                show();
            } else {
                index += 1;
                show();
            }
        });
    }

    private void show(){
        if(this.wizards.model.size() != 0){
            this.fullname.setValue(this.wizards.model.get(index).getName());
            this.dollars.setValue(this.wizards.model.get(index).getMoney());
            this.position.setValue(this.wizards.model.get(index).getPosition());
            this.school.setValue(this.wizards.model.get(index).getSchool());
            this.house.setValue(this.wizards.model.get(index).getHouse());
            this.gender.setValue(this.wizards.model.get(index).getSex());
        } else{
            this.fullname.setValue("");
            this.dollars.setValue("");
            this.position.setValue("");
            this.school.setValue("");
            this.house.setValue("");
            this.gender.setValue("");
        }
    }

    private void load(){
        this.wizards.model = WebClient
                .create()
                .get()
                .uri("http://localhost:8080/wizards")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Wizard>>() {})
                .block();
    }
}
