package backend.academy.diplom.controllers;

import backend.academy.diplom.DTO.search.ProjectsDTO;
import backend.academy.diplom.entities.Project;
import backend.academy.diplom.services.FavouritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favourites")
public class FavouritesController {
    private final FavouritesService favouritesService;

    @PostMapping("/{projectId}")
    public void updateFavourites(@RequestHeader("Authorization") String authHeader,
                              @PathVariable Long projectId) {
        favouritesService.updateFavourites(authHeader, projectId);
    }

    @GetMapping("/check/{projectId}")
    public Boolean checkFavourites(@RequestHeader("Authorization") String authHeader,
                                   @PathVariable Long projectId) {
        return favouritesService.checkFavourites(authHeader, projectId);
    }

//    @GetMapping
//    public List<ProjectsDTO> getFavourites(@RequestHeader("Authorization") String authHeader) {
//        return favouritesService.getFavourites(authHeader);
//    }
}
