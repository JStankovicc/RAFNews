package com.raf.rafnews.repository.view;

import com.raf.rafnews.entities.View;

public interface ViewRepository {

    View addView(int newsId, String viewerIp);
    int countOfViews(int newsId);
}
