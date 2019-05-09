package com.treabear.readinglist.web;

import java.util.List;

import com.treabear.readinglist.domain.Book;
import com.treabear.readinglist.domain.Reader;
import com.treabear.readinglist.repository.ReadingListRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * ReadingListController
 * 
 *
 * associatedId 값을 어디서 얻을까? 
 * @ConfigurationProperties(prefix="amazon") 이 애너테이션은 구성 프로퍼티에 있는 값을 빈의 프로퍼티에 주입시킨다.
 * prefix속성은 amazon 접두어가 붙은 프로퍼티를 ReadingListController 빈에 주입시킨다.
 */
@Controller
@RequestMapping("/")
@ConfigurationProperties(prefix="amazon")   // 프로퍼티 주입
public class ReadingListController {

    private ReadingListRepository readingListRepository;
    private String associateId;

    @Autowired
    public ReadingListController(ReadingListRepository readingListRepository) {
        this.readingListRepository = readingListRepository;
    }
    
    // 제휴 ID의 세터 메소드
    public void setAssociateId(String associateId) { 
        this.associateId = associateId;
    }

    @RequestMapping(method=RequestMethod.GET)
    public String readersBooks(Reader reader, Model model) {
        List<Book> readingList = readingListRepository.findByReader(reader);
        if(readingList != null) {
            model.addAttribute("books", readingList);
            model.addAttribute("reader", reader);
            model.addAttribute("amazonID", associateId);
        }
        return "readingList";
    }

    @RequestMapping(method = RequestMethod.POST) 
    public String addToReadingList(Reader reader, Book book) {
        book.setReader(reader);
        readingListRepository.save(book);
        return "redirect:/";
    }
}