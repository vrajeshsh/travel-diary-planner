/*
 * Created by Osman Balci on 2021.11.11
 * Copyright © 2021 Osman Balci. All rights reserved.
 */

package edu.vt.galleria;

import org.primefaces.model.ResponsiveOption;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

@Named(value = "galleriaManager")
@ViewScoped

public class GalleriaManager implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private List<Photo> listOfPhotos;
    private List<ResponsiveOption> responsiveOptions1;
    private List<ResponsiveOption> responsiveOptions2;
    private List<ResponsiveOption> responsiveOptions3;
    private int activeIndex = 0;

    /*
    The @Inject annotation directs the CDI Container Manager to inject (store) the
    object reference of the CDI container-managed PhotoService bean into the
    instance variable 'photoService' after it is instantiated at runtime.
     */
    @Inject
    private PhotoService photoService;

    /*
    The PostConstruct annotation is used on a method that needs to be executed after
    dependency injection is done to perform any initialization. The initialization
    method init() is the first method invoked before this class is put into service.
     */
    @PostConstruct
    public void init() {
        listOfPhotos = photoService.getListOfPhotos();

        responsiveOptions1 = new ArrayList<>();
        responsiveOptions1.add(new ResponsiveOption("1024px", 5));
        responsiveOptions1.add(new ResponsiveOption("768px", 3));
        responsiveOptions1.add(new ResponsiveOption("560px", 1));

        responsiveOptions2 = new ArrayList<>();
        responsiveOptions2.add(new ResponsiveOption("1024px", 5));
        responsiveOptions2.add(new ResponsiveOption("960px", 4));
        responsiveOptions2.add(new ResponsiveOption("768px", 3));
        responsiveOptions2.add(new ResponsiveOption("560px", 1));

        responsiveOptions3 = new ArrayList<>();
        responsiveOptions3.add(new ResponsiveOption("1500px", 5));
        responsiveOptions3.add(new ResponsiveOption("1024px", 3));
        responsiveOptions3.add(new ResponsiveOption("768px", 2));
        responsiveOptions3.add(new ResponsiveOption("560px", 1));
    }

    /*
    ===============
    Instance Method
    ===============
     */
    public void changeActiveIndex() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        this.activeIndex = Integer.parseInt(params.get("index"));
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public List<Photo> getListOfPhotos() {
        return listOfPhotos;
    }

    public List<ResponsiveOption> getResponsiveOptions1() {
        return responsiveOptions1;
    }

    public List<ResponsiveOption> getResponsiveOptions2() {
        return responsiveOptions2;
    }

    public List<ResponsiveOption> getResponsiveOptions3() {
        return responsiveOptions3;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }

    public void setPhotoService(PhotoService photoService) {
        this.photoService = photoService;
    }

}
