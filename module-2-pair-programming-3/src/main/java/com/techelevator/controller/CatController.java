package com.techelevator.controller;

import com.techelevator.dao.CatCardDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.CatCard;
import com.techelevator.model.CatFact;
import com.techelevator.model.CatPic;
import com.techelevator.services.CatFactService;
import com.techelevator.services.CatPicService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@CrossOrigin
public class CatController {

    private CatCardDao catCardDao;
    private CatFactService catFactService;
    private CatPicService catPicService;

    public CatController(CatCardDao catCardDao, CatFactService catFactService, CatPicService catPicService) {
        this.catCardDao = catCardDao;
        this.catFactService = catFactService;
        this.catPicService = catPicService;
    }


    //****************************
    //Get random card
    //****************************

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/random", method = RequestMethod.GET)
    public CatCard getRandomCatCard(){
        try {
            return new CatCard(catFactService.getFact().getText(), catPicService.getPic().getFile());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    //****************************
    //Get list of all cat cards
    //****************************
    @GetMapping
    public List<CatCard> getCatCards(){
        return catCardDao.getCatCards();
    }

    //****************************
    //Get card with Id
    //****************************
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public CatCard get(@PathVariable int id){
        CatCard catCard = catCardDao.getCatCardById(id);
        if (catCard == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CatCard Not Found");
        } else {
            return catCardDao.getCatCardById(id);
        }
    }


    //****************************
    //Save a card to collection (create)
    //****************************
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping (path = "", method = RequestMethod.POST)
    public CatCard create(@Valid @RequestBody CatCard catCard){return catCardDao.createCatCard(catCard);}




    //****************************
    //Updates card in collection
    //****************************
    @PutMapping("/{id}")
    public CatCard update(@Valid @RequestBody CatCard catCard, @PathVariable int id) {
        catCard.setCatCardId(id);

        try {
            return catCardDao.updateCatCard(catCard);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not Found");
        }

    }

    //****************************
    //Deletes card from collection
    //****************************
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){
        catCardDao.deleteCatCardById(id);
    }

}
