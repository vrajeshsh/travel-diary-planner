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
                "Description for Photo 1", "Travel Diary for all your adventures"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo2.jpg", "/resources/images/slider/photo2.jpg",
                "Description for Photo 2", "Store your precious photos, videos and other files as well!"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo3.jpg", "/resources/images/slider/photo3.jpg",
                "Description for Photo 3", "Search hotel all around the world"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo4.jpg", "/resources/images/slider/photo4.jpg",
                "Description for Photo 4", "See current weather at different corners of the world"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo5.jpg", "/resources/images/slider/photo5.jpg",
                "Description for Photo 5", "Record your travel notes and view them on the go!"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo6.jpg", "/resources/images/slider/photo6.jpg",
                "Description for Photo 6", "Tag the location to your travel notes."));
        listOfPhotos.add(new Photo("/resources/images/slider/photo7.jpg", "/resources/images/slider/photo7.jpg",
                "Description for Photo 7", "Your personal travel diary."));
        listOfPhotos.add(new Photo("/resources/images/slider/photo8.jpg", "/resources/images/slider/photo8.jpg",
                "Description for Photo 8", "See future weather predictions to plan your trip accordingly."));
        listOfPhotos.add(new Photo("/resources/images/slider/photo9.jpg", "/resources/images/slider/photo9.jpg",
                "Description for Photo 9", "Create an account right and start recording your travels!"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo10.jpg", "/resources/images/slider/photo10.jpg",
                "Description for Photo 10", "Store files just like a Cloud Storage."));
        listOfPhotos.add(new Photo("/resources/images/slider/photo11.jpg", "/resources/images/slider/photo11.jpg",
                "Description for Photo 11", "Find the best hotels"));
        listOfPhotos.add(new Photo("/resources/images/slider/photo12.jpg", "/resources/images/slider/photo12.jpg",
                "Description for Photo 12", "Plan your next vacation, right now!                                                                                                                                                                            "));
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
