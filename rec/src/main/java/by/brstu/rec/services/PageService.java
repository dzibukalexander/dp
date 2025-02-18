package by.brstu.rec.services;

import by.brstu.rec.entities.Page;
import by.brstu.rec.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class PageService {
    @Autowired
    private PageRepository pageRepository;

    public Page findById(Long id) {
        return pageRepository.findById(id).orElse(null);
    }

    public Page uploadPage(MultipartFile file) throws IOException {
        Page avatar = new Page();
        avatar.setOriginalFileName(file.getOriginalFilename());
        avatar.setContentType(file.getContentType());
        avatar.setSize(file.getSize());
        avatar.setBytes(file.getBytes());
        return pageRepository.save(avatar);
    }
}
