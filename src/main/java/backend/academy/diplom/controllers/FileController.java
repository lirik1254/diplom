package backend.academy.diplom.controllers;

import backend.academy.diplom.DTO.PutLinkDTO;
import backend.academy.diplom.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/put-link")
    public PutLinkDTO putLink(@RequestParam String filePath) {
        return fileService.putLink(filePath);
    }

    @PostMapping("/put-link")
    public void putLinkSave(@RequestParam String filePath, String type,
                            @RequestHeader("Authorization") String authHeader) {
        fileService.saveLink(type, filePath, authHeader);
    }

    @GetMapping("/get-link")
    public String getLink(@RequestHeader("Authorization") String authHeader,
                          @RequestParam String type) {
        return fileService.getLink(authHeader, type);
    }

}
