/*
 * Created by Osman Balci on 2021.11.11
 * Copyright © 2021 Osman Balci. All rights reserved.
 */

package edu.vt.galleria;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;

@Named(value = "photoService")
@ApplicationScoped
public class PhotoService {
    /*
    ============================
    Instance Variable (Property)
    ============================
     */
    private List<Photo> listOfPhotos;

    /*
    The PostConstruct annotation is used on a method that needs to be executed after
    dependency injection is done to perform any initialization. The initialization
    method init() is the first method invoked before this class is put into service.
     */
    @PostConstruct
    public void init() {
        listOfPhotos = new ArrayList<>();

        listOfPhotos.add(new Photo("/resources/images/slider/photo1.jpg", "/resources/images/slider/photo1.jpg",
                "Description for Photo 1", "Health and Fitness are Key to a Happy Life"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo2.jpg", "/resources/images/slider/photo2.jpg",
                "Description for Photo 2", "Apple Fitness+ is the first Fitness Experience built around Apple Watch"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo3.jpg", "/resources/images/slider/photo3.jpg",
                "Description for Photo 3", "Body Mass Index for Women"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo4.jpg", "/resources/images/slider/photo4.jpg",
                "Description for Photo 4", "Body Mass Index for Men"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo5.jpg", "/resources/images/slider/photo5.jpg",
                "Description for Photo 5", "Exercise for Good Health and Fitness"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo6.jpg", "/resources/images/slider/photo6.jpg",
                "Description for Photo 6", "Eat Fruits and Vegetables for Good Health"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo7.jpg", "/resources/images/slider/photo7.jpg",
                "Description for Photo 7", "A Weight Club Provides Exercise Equipment"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo8.jpg", "/resources/images/slider/photo8.jpg",
                "Description for Photo 8", "Apps Help You Lose Weight"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo9.jpg", "/resources/images/slider/photo9.jpg",
                "Description for Photo 9", "Many National Gym Chains are Available for Fitness"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo10.jpg", "/resources/images/slider/photo10.jpg",
                "Description for Photo 10", "Fruits and Vegetables are Key for Good Health and Fitness"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo11.jpg", "/resources/images/slider/photo11.jpg",
                "Description for Photo 11", "Use the Best Fitness Websites"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo12.jpg", "/resources/images/slider/photo12.jpg",
                "Description for Photo 12", "An Apple a Day Keeps the Doctor Away!"));
    }

    /*
    =============
    Getter Method
    =============
     */
    public List<Photo> getListOfPhotos() {
        return listOfPhotos;
    }
}
