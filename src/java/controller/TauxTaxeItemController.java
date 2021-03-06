package controller;

import bean.TauxTaxe;
import bean.TauxTaxeItem;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.TauxTaxeItemFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import service.TauxTaxeFacade;

@Named("tauxTaxeItemController")
@SessionScoped
public class TauxTaxeItemController implements Serializable {

    @EJB
    private service.TauxTaxeItemFacade ejbFacade;
    private List<TauxTaxeItem> items = null;
    private TauxTaxeItem selected;
    private Double tauxMin;
    private Double tauxMax;
    private TauxTaxe tauxTaxe;
   
    @EJB
    private TauxTaxeFacade tauxTaxeFacade;
    
     private Date dateApplication;
    
    public TauxTaxeItemController() {
      
    }

    public Date getDateApplication() {
        return dateApplication;
    }

    public void setDateApplication(Date dateApplication) {
        if(dateApplication==null){
            dateApplication=new Date();
        }
        this.dateApplication = dateApplication;
    }

    public String cree(){
       int res= tauxTaxeFacade.ajouter(items,dateApplication);
       if(res==1){
           return "ResultatDeCreaate";
       }else return null;
    }
   public void fillInList(){ 
       System.out.println("hanidateApplication");
       //if(dateApplication!=null){
       items=ejbFacade.Add(selected,items);
       //}
   }
 public void findByTauMxMn(){
     items=ejbFacade.findByTauMxMn(tauxMin, tauxMax);
 }
    public TauxTaxeItem getSelected() {
        if(selected==null){
            selected=new TauxTaxeItem();
        }
        return selected;
    }

    public TauxTaxeItemFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(TauxTaxeItemFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public TauxTaxe getTauxTaxe() {
        if(tauxTaxe==null){
            tauxTaxe=new TauxTaxe();
      tauxTaxe.setDateApplication(new Date());
      tauxTaxe.setId(tauxTaxeFacade.generate("TauxTaxe","id"));
        }
        return tauxTaxe;
    }

    public void setTauxTaxe(TauxTaxe tauxTaxe) {
        this.tauxTaxe = tauxTaxe;
    }

    public TauxTaxeFacade getTauxTaxeFacade() {
        return tauxTaxeFacade;
    }

    public void setTauxTaxeFacade(TauxTaxeFacade tauxTaxeFacade) {
        this.tauxTaxeFacade = tauxTaxeFacade;
    }
    

    public void setSelected(TauxTaxeItem selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    public Double getTauxMin() {
        return tauxMin;
    }

    public void setTauxMin(Double tauxMin) {
        this.tauxMin = tauxMin;
    }

    public Double getTauxMax() {
        return tauxMax;
    }

    public void setTauxMax(Double tauxMax) {
        this.tauxMax = tauxMax;
    }

    private TauxTaxeItemFacade getFacade() {
        return ejbFacade;
    }

    public TauxTaxeItem prepareCreate() {
        selected = new TauxTaxeItem();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TauxTaxeItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TauxTaxeItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TauxTaxeItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<TauxTaxeItem> getItems() {
        if (items == null) {
            items = new ArrayList();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public TauxTaxeItem getTauxTaxeItem(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<TauxTaxeItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<TauxTaxeItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = TauxTaxeItem.class)
    public static class TauxTaxeItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TauxTaxeItemController controller = (TauxTaxeItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tauxTaxeItemController");
            return controller.getTauxTaxeItem(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TauxTaxeItem) {
                TauxTaxeItem o = (TauxTaxeItem) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), TauxTaxeItem.class.getName()});
                return null;
            }
        }

    }

}
