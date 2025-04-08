package by.brstu.rec.services;

import by.brstu.rec.entities.Page;
import by.brstu.rec.repositories.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PageService {
    @Autowired
    private PageRepository pageRepository;

    public Page findById(Long id) throws NoSuchElementException {
        return pageRepository.findById(id).orElseThrow();
    }

    public byte[] getPhotoBytes(Long fileId) throws IOException {
        Optional<Page> page = pageRepository.findById(fileId);
        return page.map(Page::getBytes).orElse(null);
    }

    public Page uploadPage(MultipartFile file) throws IOException {
        Page page = new Page();
        page.setOriginalFileName(file.getOriginalFilename());
        page.setContentType(file.getContentType());
        page.setSize(file.getSize());

        // Если это TIFF, конвертируем в JPEG
        if (file.getContentType().equals("image/tiff")) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BufferedImage image = ImageIO.read(file.getInputStream());
            ImageIO.write(image, "jpg", outputStream);
            page.setBytes(outputStream.toByteArray());
            //page.setContentType("image/jpeg");
        } else {
            page.setBytes(file.getBytes());
        }

        return pageRepository.save(page);
    }
}
