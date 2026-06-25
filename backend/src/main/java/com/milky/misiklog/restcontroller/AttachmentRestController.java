package com.milky.misiklog.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.milky.misiklog.service.AttachmentService;
import com.milky.misiklog.dao.AttachmentDao;
import com.milky.misiklog.dto.AttachmentDto;

@RestController
@RequestMapping("/attachment")
@CrossOrigin(origins = "http://localhost:5173")
public class AttachmentRestController {

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private AttachmentDao attachmentDao;

    @GetMapping("/{attachmentNo}")
    public ResponseEntity<ByteArrayResource> fileDownload(
            @PathVariable int attachmentNo) throws IOException {

        AttachmentDto attachmentDto = attachmentDao.selectOne(attachmentNo);
        if (attachmentDto == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = attachmentService.load(attachmentNo);

        return ResponseEntity.ok()
        	    .contentType(MediaType.parseMediaType(attachmentDto.getAttachmentType()))
        	    .body(resource);
    }
}
