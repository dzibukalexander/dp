package by.brstu.rec.services;

import by.brstu.rec.entities.Page;
import by.brstu.rec.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
public class PageService {
    @Autowired
    private PageRepository pageRepository;

    public Page findById(Long id) throws NoSuchElementException {
        return pageRepository.findById(id).orElseThrow();
    }

    public Page uploadPage(MultipartFile file) throws IOException {
        Page page = new Page();
        page.setOriginalFileName(file.getOriginalFilename());
        page.setContentType(file.getContentType());
        page.setSize(file.getSize());
        page.setBytes(file.getBytes());
        return pageRepository.save(page);
    }
}
