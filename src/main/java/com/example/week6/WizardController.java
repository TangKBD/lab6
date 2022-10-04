package com.example.week6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class WizardController {
    @Autowired
    private WizardService wizardService;

    @RequestMapping(value = "/wizards", method = RequestMethod.GET)
    public ResponseEntity<?> getWizards(){
        List<Wizard> wizards = wizardService.retrieveWizards();
        return ResponseEntity.ok(wizards);
    }

    @RequestMapping(value = "/addWizard", method = RequestMethod.POST)
    public ResponseEntity<?> addWizard(@RequestParam("sex") String sex,
                                       @RequestParam("name") String name,
                                       @RequestParam("school") String school,
                                       @RequestParam("house") String house,
                                       @RequestParam("money") String money,
                                       @RequestParam("position") String position){
        Wizard wizard = wizardService.createWizard(new Wizard(null, sex, name, school, house, money, position));
        return ResponseEntity.ok(wizard);
    }

    @RequestMapping(value = "/updateWizard", method = RequestMethod.POST)
    public boolean updateWizard(@RequestParam("sex") String sex,
                                @RequestParam("newName") String newName,
                                @RequestParam("school") String school,
                                @RequestParam("house") String house,
                                @RequestParam("money") String money,
                                @RequestParam("position") String position,
                                @RequestParam("oldName") String oldName){
        Wizard wizard = wizardService.retrieveByName(oldName);
        if(wizard != null){
            wizardService.updateWizard(new Wizard(wizard.get_id(), sex, newName, school, house, money, position));
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping(value = "/deleteWizard", method = RequestMethod.POST)
    public boolean deleteWizard(@RequestParam("name") String name){
        Wizard wizard = wizardService.retrieveByName(name);
        return wizardService.deleteWizard(wizard);
    }
}
