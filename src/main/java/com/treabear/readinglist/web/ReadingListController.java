package com.treabear.readinglist.web;

import java.util.List;

import com.treabear.readinglist.config.AmazonProperties;
import com.treabear.readinglist.domain.Book;
import com.treabear.readinglist.domain.Reader;
import com.treabear.readinglist.repository.ReadingListRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.AllArgsConstructor;

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
@AllArgsConstructor // 생성자 주입
public class ReadingListController {

    private ReadingListRepository readingListRepository;
    private AmazonProperties amazonProperties;

    /*
    @Autowired
    public ReadingListController(ReadingListRepository readingListRepository, AmazonProperties amazonProperties) {
        // AmazonProperties 주입
        this.readingListRepository = readingListRepository;
        this.AmazonProperties = amazonProperties;
    }
    */
    
    @RequestMapping(method=RequestMethod.GET)
    public String readersBooks(Reader reader, Model model) {
        List<Book> readingList = readingListRepository.findByReader(reader);
        if(readingList != null) {
            model.addAttribute("books", readingList);
            model.addAttribute("reader", reader);
            model.addAttribute("amazonID", amazonProperties.getAssociateId());
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