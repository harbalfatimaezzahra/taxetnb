/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.TauxTaxe;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author simob
 */
@Stateless
public class TauxTaxeFacade extends AbstractFacade<TauxTaxe> {

    @PersistenceContext(unitName = "taxeTNBPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TauxTaxeFacade() {
        super(TauxTaxe.class);
    }
    
}
