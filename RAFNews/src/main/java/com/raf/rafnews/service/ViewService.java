package com.raf.rafnews.service;

import com.raf.rafnews.entities.View;
import com.raf.rafnews.repository.view.ViewRepository;

import javax.inject.Inject;

public class ViewService {

    @Inject
    ViewRepository viewRepository;
    public View addView(int newsId, String viewerIp){
        return this.viewRepository.addView(newsId,viewerIp);
    }

    public int countOfViews(int newsId){
        return this.viewRepository.countOfViews(newsId);
    }
}
