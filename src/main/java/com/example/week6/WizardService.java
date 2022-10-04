package com.example.week6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WizardService {
    @Autowired
    private WizardRepository repository;

    public List<Wizard> retrieveWizards(){
        return repository.findAll();
    }

    public Wizard createWizard(Wizard wizard){
        return  repository.save(wizard);
    }

    public boolean deleteWizard(Wizard wizard){
        try{
            repository.delete(wizard);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Wizard updateWizard(Wizard wizard){
        return repository.save(wizard);
    }

    public Wizard retrieveByName(String name){
        return repository.findByName(name);
    }

    public Wizard findById(String _id){
        return repository.findId(_id);
    }
}

