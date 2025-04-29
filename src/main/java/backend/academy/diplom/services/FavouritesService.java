package backend.academy.diplom.services;

import backend.academy.diplom.DTO.search.ProjectsDTO;
import backend.academy.diplom.entities.Project;
import backend.academy.diplom.entities.User;
import backend.academy.diplom.repositories.FavouritesRepository;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.utils.JwtUtils;
import backend.academy.diplom.utils.ProjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouritesService {
    private final FavouritesRepository favouritesRepository;
    private final JwtUtils jwtUtils;
    private final ProjectUtils projectUtils;

    public void updateFavourites(String authHeader, Long projectId) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);

        favouritesRepository.updateFavourites(user.getId(), projectId);
    }

    public List<ProjectsDTO> getFavourites(String authHeader) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);

        List<Project> projects = favouritesRepository.getAllByUserId(user.getId());

        return projectUtils.projectsToDTO(projects);
    }

    public Boolean checkFavourites(String authHeader, Long projectId) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        return favouritesRepository.checkFavourites(user.getId(), projectId);
    }
}
