package com.raf.rafnews.service;

import com.raf.rafnews.entities.Tag;
import com.raf.rafnews.repository.tag.TagRepository;

import javax.inject.Inject;
import java.util.List;

public class TagService {
    @Inject
    TagRepository tagRepository;
    public List<Tag> allTags(){
        return this.tagRepository.allTags();
    }
    public Tag addTag(Tag tag){
        return this.tagRepository.addTag(tag);
    }
    public Tag findTag(String tag){
        return this.tagRepository.findTag(tag);
    }
    public Tag findTagById(Integer id){
        return this.tagRepository.findTagById(id);
    }
    public void deleteTag(Integer id){
        this.tagRepository.deleteTag(id);
    }
}
